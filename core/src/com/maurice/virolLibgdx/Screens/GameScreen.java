package com.maurice.virolLibgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.maurice.virolLibgdx.GameWorld.GameRenderer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.Networking.NetworkManager;
import com.maurice.virolLibgdx.ZBHelpers.InputHandler;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;

public class GameScreen extends AbstractGameScreen{

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;
    private InputHandler inputHandler;


	// This is the constructor, not the class declaration
	public GameScreen(ZBGame zbgame) {
        super(zbgame);
        this.game = zbgame;
        this.world = zbgame.world;
        renderer = new GameRenderer(world);

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        float gameWidth = ZBGame.GAME_WIDTH;
        float gameHeight = screenHeight / (screenWidth / ZBGame.GAME_WIDTH);
        inputHandler = new InputHandler(screenWidth / gameWidth, screenHeight / gameHeight);
        Gdx.input.setInputProcessor(inputHandler);
        System.out.println("setInputProcessor screenWidth");

        NetworkManager networkManager = new NetworkManager();
	}

	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta);
		renderer.render(delta, runTime);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

    @Override
    public InputProcessor getInputProcessor () {
        System.out.println("GameScreen input processor requested");
        return inputHandler;
    }
}
