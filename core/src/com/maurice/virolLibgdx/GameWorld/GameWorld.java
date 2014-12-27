package com.maurice.virolLibgdx.GameWorld;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Timer;
import com.maurice.virolLibgdx.GameObjects.Circle;
import com.maurice.virolLibgdx.GameObjects.CircleController;
import com.maurice.virolLibgdx.GameObjects.Point;
import com.maurice.virolLibgdx.OpponentIntelligence.AI;
import com.maurice.virolLibgdx.Screens.GameScreen;
import com.maurice.virolLibgdx.Screens.MenuScreen;
import com.maurice.virolLibgdx.Transitions.ScreenTransition;
import com.maurice.virolLibgdx.Transitions.ScreenTransitionSlide;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;

public class GameWorld {

    private CircleController circleController;
	private int score = 0;
    private static GameWorld instance;
	private float runTime = 0;
    private ZBGame game;
	private GameState currentState;
    public static int ROWS = 4;
    public static int COLUMNS = 6;
    public boolean LAST_WON_OPPONENT = false;
    public float GAME_SCORE=0;
    public PlayState currPlayState = PlayState.PLAYER;
    public PlayMode currPlayMode = PlayMode.SINGLEPLAYER;
    private Circle[][] resumeCirclesArray;
    private boolean AIMoveRequested = false;

    public enum GameState {MENU, READY, RUNNING, GAMEOVER, HIGHSCORE,ABOUT}
    public enum PlayMode {SINGLEPLAYER, ONLINE, PAUSE_SINGLE,PAUSE_MULTI, MULTIPLAYER}
    public enum PlayState {PLAYER,ANIM_PLAYER,OPPONENT,ANIM_OPPONENT}

	public GameWorld(ZBGame game) {
		currentState = GameState.READY;
        circleController = new CircleController(ROWS,COLUMNS);
        this.game = game;
	}
    public static GameWorld getInstance(){
        if(instance==null) instance = new GameWorld(ZBGame.getInstance());
        return instance;
    }
	public void update(float delta) {
		runTime += delta;

		switch (currentState) {
		case READY:
		case MENU:
			updateReady(delta);
			break;

		case RUNNING:
			updateRunning(delta);
            checkGameOver();
			break;
        case GAMEOVER:
            updateRunning(delta);
            break;
		default:
			break;
		}
        checkForAI();
	}

    private void checkForAI(){
        if(currPlayMode==PlayMode.SINGLEPLAYER){
            if(currPlayState==PlayState.OPPONENT){
                Timer.Task task = new Timer.Task() {
                    @Override
                    public void run() {
                        System.out.println("AI Checked For AI");
                        AIMoveRequested=true;
                        Point nextMove = AI.getNextMove(circleController);
                        GAME_SCORE = AI.calculateScore(circleController);
                        circleController.move(nextMove.x,nextMove.y);
                        AIMoveRequested=false;
                    }
                };
                if(!AIMoveRequested){
                    Timer y = new Timer();
                    y.scheduleTask(task,0);
                }
            }
        }
    }

	private void updateReady(float delta) {
	}

	public void updateRunning(float delta) {
		if (delta > .15f) {
			delta = .15f;
		}
        circleController.update(delta);

	}



    public CircleController getCircleController() { return circleController;}
	public int getScore() {
		return score;
	}

	public void addScore(int increment) {
		score += increment;
	}

    public void startSinglePlayerGame(){
        currPlayMode = PlayMode.SINGLEPLAYER;
        ready();
        start();
    }
    public void startMultiPlayerGame(){
        currPlayMode = PlayMode.MULTIPLAYER;
        ready();
        start();
    }
    public void pausePlayerGame() {
        if(currPlayMode == PlayMode.SINGLEPLAYER)
            currPlayMode = PlayMode.PAUSE_SINGLE;
        if(currPlayMode == PlayMode.MULTIPLAYER)
            currPlayMode = PlayMode.PAUSE_MULTI;
        resumeCirclesArray = circleController.getCiclesArray();
        System.out.println("GW "+resumeCirclesArray.length);
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.DOWN, false, Interpolation.sineOut);
        game.setScreen(new MenuScreen(game),transition);
    }
    public void resumeGame() {
        if(currPlayMode == PlayMode.PAUSE_SINGLE)
            currPlayMode = PlayMode.SINGLEPLAYER;
        if(currPlayMode == PlayMode.PAUSE_MULTI)
            currPlayMode = PlayMode.MULTIPLAYER;
        System.out.println("GW "+resumeCirclesArray.length);
        circleController.setCiclesArray(resumeCirclesArray);
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.UP, false, Interpolation.sineOut);
        game.setScreen(new GameScreen(game),transition);
    }
    public void menu() {
        currentState = GameState.MENU;
        game.setScreen(new MenuScreen(game));
    }
    public void ready() {
        circleController.restart();
		currentState = GameState.READY;
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.UP, false, Interpolation.sineOut);
        game.setScreen(new GameScreen(game),transition);
	}
    public void gameover(boolean lastWonIsOpponent) {
        currentState = GameState.GAMEOVER;
        LAST_WON_OPPONENT = lastWonIsOpponent;
    }
    public void start() {
        currentState = GameState.RUNNING;
    }
    public void restart(){
        startSinglePlayerGame();
    }

    public void checkGameOver(){
        circleController.checkGameOver();
    }

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public boolean isHighScore() {
		return currentState == GameState.HIGHSCORE;
	}

	public boolean isMenu() {
		return currentState == GameState.MENU;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}


}
