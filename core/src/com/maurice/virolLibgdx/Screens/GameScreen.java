package com.maurice.virolLibgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Interpolation;
import com.maurice.virolLibgdx.GameWorld.GameRenderer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.Networking.NetworkManager;
import com.maurice.virolLibgdx.Transitions.ScreenTransition;
import com.maurice.virolLibgdx.Transitions.ScreenTransitionSlide;
import com.maurice.virolLibgdx.ZBHelpers.InputHandler;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;

public class GameScreen extends AbstractGameScreen{

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;
    private static int GAME_WIDTH = 136;
    InputHandler input;

	// This is the constructor, not the class declaration
	public GameScreen(ZBGame zbgame) {
        super(zbgame);
        this.game = zbgame;
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float gameWidth = GAME_WIDTH;
		float gameHeight = screenHeight / (screenWidth / gameWidth);
		int midPointY = (int) (gameHeight / 2);

		world = new GameWorld(midPointY);
        input = new InputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight, this);
		Gdx.input.setInputProcessor(input);
		renderer = new GameRenderer(world, (int) gameHeight,(int)gameWidth);
		world.setRenderer(renderer);
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

    public void getSettingsMenu(){
        //game.setScreen(new testtwo(game));
        //ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        //game.setScreen(new testtwo(game), transition);

        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.UP, false, Interpolation.sineOut);
        game.setScreen(new SettingsScreen(game), transition);
        System.out.println("changescreen called");
    }
    @Override
    public InputProcessor getInputProcessor () {
        System.out.println("input processor 1 requested");
        return input;
    }
}
