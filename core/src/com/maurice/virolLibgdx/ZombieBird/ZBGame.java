package com.maurice.virolLibgdx.ZombieBird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.Screens.AbstractGameScreen;
import com.maurice.virolLibgdx.Screens.SplashScreen;
import com.maurice.virolLibgdx.Transitions.ScreenTransition;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;
import com.maurice.virolLibgdx.ZBHelpers.InputHandler;

public class ZBGame extends Game {
    private AbstractGameScreen currScreen;
    private AbstractGameScreen nextScreen;
    private boolean init;
    private static ZBGame instance;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float transitionTime;
    private ScreenTransition screenTransition;

    public GameWorld world;
    public static InputHandler inputHandler;
    public static int GAME_WIDTH = 136;
    public static int GAME_HEIGHT;
    public static int GAME_DASHBOARD_HEIGHT;

	@Override
	public void create() {
        AssetLoader.loadSplash();
        instance = this;
        currScreen=new SplashScreen(this);
        setupGameWorld();
        setScreen(currScreen);
		AssetLoader.load();
        System.out.println("Game created");
	}
    public static ZBGame getInstance(){
        return instance;
    }

    //SCREEN TRANSITION RELATED
    public void setScreen (AbstractGameScreen screen) {
        setScreen(screen, null);
        System.out.println("Screen set");
    }
    public void setScreen (AbstractGameScreen testtwo, ScreenTransition screenTransition) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (!init) {
            currFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            nextFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            batch = new SpriteBatch();
            init = true;
            System.out.println("framebuffer init");
        }
        // start new transition
        nextScreen = testtwo;
        nextScreen.show(); // activate next screen
        nextScreen.resize(w, h);
        nextScreen.render(0); // let screen update() once
        if (currScreen != null) currScreen.pause();
        nextScreen.pause();
        Gdx.input.setInputProcessor(null); // disable input
        System.out.println("Input processor diabled");
        this.screenTransition = screenTransition;
        transitionTime = 0;
        System.out.println("Screen set");
    }

    @Override
    public void render () {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null) {
            // no ongoing transition
            if (currScreen != null) currScreen.render(deltaTime);
        } else {
            // ongoing transition
            float duration = 0;
            if (screenTransition != null)
                duration = screenTransition.getDuration();
            // update progress of ongoing transition
            transitionTime = Math.min(transitionTime + deltaTime, duration);
            if (screenTransition == null || transitionTime >= duration) {
                //no transition effect set or transition has just finished
                if (currScreen != null) currScreen.hide();
                nextScreen.resume();
                // enable input for next screen
                Gdx.input.setInputProcessor(
                        nextScreen.getInputProcessor());
                // switch screens
                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // render screens to FBOs
                currFbo.begin();
                if (currScreen != null) currScreen.render(deltaTime);
                currFbo.end();
                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();
                // render transition effect to screen
                float alpha = transitionTime / duration;
                screenTransition.render(batch,
                        currFbo.getColorBufferTexture(),
                        nextFbo.getColorBufferTexture(),
                        alpha);
            }
        }
    }
    @Override
    public void resize (int width, int height) {
        if (currScreen != null) currScreen.resize(width, height);
        if (nextScreen != null) nextScreen.resize(width, height);
    }
    @Override
    public void pause () {
        if (currScreen != null) currScreen.pause();
    }
    @Override
    public void resume () {
        if (currScreen != null) currScreen.resume();
    }
    @Override
    public void dispose () {
        super.dispose();
        AssetLoader.dispose();
        if (currScreen != null) currScreen.hide();
        if (nextScreen != null) nextScreen.hide();
        if (init) {
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

    public void setupGameWorld(){
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        GAME_HEIGHT = (int) (screenHeight / (screenWidth / GAME_WIDTH));
        GAME_DASHBOARD_HEIGHT = (int)(GAME_HEIGHT*0.1f);

        world = GameWorld.getInstance();
        Gdx.input.setInputProcessor(inputHandler);
    }
}