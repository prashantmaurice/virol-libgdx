package com.maurice.virolLibgdx.GameWorld;

import com.badlogic.gdx.math.Vector2;
import com.maurice.virolLibgdx.GameObjects.CircleController;

public class GameWorld {




    private CircleController circleController;
	private int score = 0;
	private float runTime = 0;
	private int midPointY;
	private GameRenderer renderer;
	int WIDTH = 100;
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

	public GameWorld(int midPointY) {
		currentState = GameState.MENU;
		this.midPointY = midPointY;
        circleController = new CircleController(ROWS,COLUMNS);
//		bird = new Bird(33, midPointY - 5, 17, 12);
		// The grass should start 66 pixels below the midPointY
//		scroller = new ScrollHandler(this, midPointY + 66);
//		ground = new Rectangle(0, midPointY + 56, 137, 11);
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


	public int getMidPointY() {
		return midPointY;
	}

    public CircleController getCircleController() { return circleController;}
	public int getScore() {
		return score;
	}

	public void addScore(int increment) {
		score += increment;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}


    public void ready() {
		currentState = GameState.READY;
		renderer.prepareTransition(0, 0, 0, 1f);
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
