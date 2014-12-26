package com.maurice.virolLibgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
//    private Stage stage = new Stage();
//    Skin skin;

    //MENU
    private Stage stage = new Stage();
    private Table table = new Table();
    private ShapeRenderer shapeRenderer;
    private Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    private TextButton buttonPlay = new TextButton("Play", skin),
            buttonExit = new TextButton("Exit", skin);
    private Label title = new Label("Game Title",skin);

	// This is the constructor, not the class declaration
	public MenuScreen(ZBGame zbgame) {
        super(zbgame);
        this.game = zbgame;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		float gameWidth = GAME_WIDTH;
		float gameHeight = screenHeight / (screenWidth / gameWidth);

        input = new InputHandler(screenWidth / gameWidth, screenHeight / gameHeight);
//		Gdx.input.setInputProcessor(input);



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

        //MENU
        shapeRenderer = new ShapeRenderer();
        //Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
//        AssetLoader.font.setColor(Color.BLACK);
        skin.add("default", AssetLoader.font);

        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);



        //SUBMIT BUTTON
        table.row().padTop(screenHeight/15);
        final TextButton button = new TextButton("SAVE", skin);
        button.pad(20);
        button.setColor(colorFromHex(0xFF419FFFL));
        table.add(button);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("slider position: Set");
            }
        });
	}

	@Override
	public void render(float delta) {
//        Gdx.gl.glClearColor(40f/225f, 62f/225f, 119f/225f, 1);//faint blue
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        batcher.begin();
//        logoSprite.draw(batcher);
//        drawReady();
//        batcher.end();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        //DRAW BACKGROUND
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(colorFromHex(0xFF222222L));
//        shapeRenderer.rect(0,0, screenWidth, screenHeight);
//        shapeRenderer.setColor(colorFromHex(0xFF007DFDL));
//        shapeRenderer.rect(0,screenHeight-4, screenWidth, 4);
//        shapeRenderer.end();
//
//        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//        stage.draw();
//        Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
        buttonPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("slider position: Clicked Play");
            }
        });
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("slider position: Clicked Exit");
                Gdx.app.exit();
                // or System.exit(0);
            }
        });

        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).padBottom(40).row();
        table.add(buttonPlay).size(150,60).padBottom(20).row();
        table.add(buttonExit).size(150,60).padBottom(20).row();

        table.setFillParent(true);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
        System.out.println("setInputProcessor stage");
	}

	@Override
	public void hide() {
        dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
        stage.dispose();
        skin.dispose();
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
        AssetLoader.font.setScale(0.22f, 0.22f);
        AssetLoader.font.draw(batcher, "TAP TO START",
                200, (screenHeight/2) - 140);
    }
    private Color colorFromHex(long hex){
        float a = (hex & 0xFF000000L) >> 24;
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);
        return new Color(r/255f, g/255f, b/255f, a/255f);
    }
}
