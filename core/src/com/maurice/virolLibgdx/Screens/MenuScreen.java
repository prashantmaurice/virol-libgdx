package com.maurice.virolLibgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.maurice.virolLibgdx.GameWorld.GameRenderer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.Transitions.ScreenTransition;
import com.maurice.virolLibgdx.Transitions.ScreenTransitionSlide;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;
import com.maurice.virolLibgdx.ZBHelpers.InputHandler;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;

public class MenuScreen extends AbstractGameScreen{

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;
    private static int GAME_WIDTH = 136;
    InputHandler input;
    SpriteBatch batcher;
    Sprite logoSprite;
    private int screenHeight, screenWidth;

	// This is the constructor, not the class declaration
	public MenuScreen(ZBGame zbgame) {
        super(zbgame);
        this.game = zbgame;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		float gameWidth = GAME_WIDTH;
		float gameHeight = screenHeight / (screenWidth / gameWidth);

        input = new InputHandler(screenWidth / gameWidth, screenHeight / gameHeight);
		Gdx.input.setInputProcessor(input);



        logoSprite = new Sprite(AssetLoader.virollogo);
//        logoSprite.setColor(1, 1, 1, 0);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        float desiredWidth = width * .9f;
        float scale = desiredWidth / logoSprite.getWidth();

        logoSprite.setSize(logoSprite.getWidth() * scale, logoSprite.getHeight() * scale);
        logoSprite.setPosition((width / 2) - (logoSprite.getWidth() / 2), (height / 2)
                - (logoSprite.getHeight() / 2));
        batcher = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(40f/225f, 62f/225f, 119f/225f, 1);//faint blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        logoSprite.draw(batcher);
        drawReady();
        batcher.end();

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
        System.out.println("Menu screen input processor requested");
        return input;
    }
    private void drawReady() {
        //batcher.draw(ready, 136/2-28, midPointY - 50, 57, 14);
        AssetLoader.font.setScale(0.06f, -0.06f);
        AssetLoader.font.draw(batcher, "TAP TO START",
                34, screenHeight/2 + 70);
    }
}
