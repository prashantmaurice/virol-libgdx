package com.maurice.virolLibgdx.GameWorld;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.maurice.virolLibgdx.GameObjects.CircleController;
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
	private GameRenderer renderer;
	private GameState currentState;
    public static int ROWS = 4;
    public static int COLUMNS = 6;
    private int CIRCLE_DIAMETER;

    public void setDimensions(Vector2 gameDimensions) {
        CIRCLE_DIAMETER = (int)(((gameDimensions.x/ROWS)<(gameDimensions.y/COLUMNS))?(gameDimensions.x/ROWS):(gameDimensions.y/COLUMNS));
    }

    public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
	}

	public GameWorld(ZBGame game) {
		currentState = GameState.READY;
        circleController = new CircleController(ROWS,COLUMNS);
        this.game = game;
	}
    public static GameWorld getInstance(){
        if(instance==null) instance = new GameWorld(ZBGame.getInstance());
        return instance;
    }
    public void createBoard(int boardX,int boardY){
        circleController.createBoard(boardX,boardY);
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
			break;
		default:
			break;
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


    public void menu() {
        currentState = GameState.MENU;
        game.setScreen(new MenuScreen(game));
    }
    public void ready() {
		currentState = GameState.READY;
        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.UP, false, Interpolation.sineOut);
        game.setScreen(new GameScreen(game),transition);
	}
    public void start() {
        currentState = GameState.RUNNING;
    }

	public void restart() {
		score = 0;
//		bird.onRestart(midPointY - 5);
		ready();
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

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}

}
