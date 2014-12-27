package com.maurice.virolLibgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.maurice.virolLibgdx.GameWorld.GameRenderer;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.Transitions.ScreenTransition;
import com.maurice.virolLibgdx.Transitions.ScreenTransitionSlide;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;
import com.maurice.virolLibgdx.ZBHelpers.InputHandler;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;
import com.maurice.virolLibgdx.ui.UIColors;

public class AboutScreen extends AbstractGameScreen{

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

    private TextArea textArea = new TextArea("Hola I am Prashant Maurice",skin);
    private Label title = new Label("Hola,\n I am Prashant Maurice,\n an Energy drink addicted\n{Web/Android} Developer \nfrom Bangalore",skin);
    private Image logoImage = new Image(AssetLoader.virollogo);
    private TextButton buttonBack = new TextButton("Back", skin);

	// This is the constructor, not the class declaration
	public AboutScreen(ZBGame zbgame) {
        super(zbgame);
        this.game = zbgame;
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		float gameWidth = GAME_WIDTH;
		float gameHeight = screenHeight / (screenWidth / gameWidth);

        OrthographicCamera cam = new OrthographicCamera(ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT);
        cam.setToOrtho(true, ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        cam.update();

        skin.add("default", AssetLoader.font);

        title.setColor(Color.WHITE);
        title.scaleBy(2);

        logoSprite = new Sprite(AssetLoader.virollogo);
        float desiredWidth = ZBGame.GAME_WIDTH * 1.4f;
        float scale = desiredWidth / logoSprite.getWidth();
        logoSprite.setFlip(false, true);
        logoSprite.setSize(logoSprite.getWidth() * scale, logoSprite.getHeight() * scale);
        logoSprite.setPosition((ZBGame.GAME_WIDTH/2)-(logoSprite.getWidth()/2), (ZBGame.GAME_HEIGHT/6)-(logoSprite.getHeight()/2));
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //DRAW BACKGROUND
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(UIColors.ABOUT_BG);
        shapeRenderer.rect(0, 0, ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT);
//        shapeRenderer.setColor(UIColors.MENU_WHITE);
//        shapeRenderer.rect(0, ZBGame.GAME_HEIGHT / 3, ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT * 2 / 3);
//        shapeRenderer.setColor(UIColors.MENU_LIGHTBLUE_LINE);
//        shapeRenderer.rect(0, ZBGame.GAME_HEIGHT / 3,ZBGame.GAME_WIDTH, 3);
        shapeRenderer.end();


        batcher.begin();
        batcher.enableBlending();
        batcher.setColor(Color.RED);
//        logoSprite.draw(batcher);
//        drawReady();
        batcher.end();

        //DRAW TABLE
        stage.act();
        stage.draw();
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
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                        ScreenTransitionSlide.RIGHT, false, Interpolation.sineOut);
                game.setScreen(new MenuScreen(game),transition);
            }
        });

        buttonBack.setColor(UIColors.MENU_BUTTON);
        buttonBack.pad(30).setWidth(screenWidth / 2);



        //TEXT
        Label.LabelStyle style = title.getStyle();
        style.font = AssetLoader.DINcondensed;
        style.fontColor = Color.WHITE;
        title.setStyle(style);
        title.setFontScale(2);
        title.setAlignment(Align.right,Align.right);



        //FINAL INSERTION IN TABLE
        int padBottom = 20;
        int buttonHeight = 100;
        int buttonWidth = (int) (screenWidth*0.8f);
        table.add(title).align(Align.right).padRight(30).size(screenWidth, screenHeight - buttonHeight-padBottom).row();
        table.add(buttonBack).size(buttonWidth, buttonHeight).padBottom(padBottom).row();
        table.setFillParent(true);
        stage.addActor(table);
//        stage.addActor(textArea);

        Gdx.input.setInputProcessor(stage);
        System.out.println("setInputProcessor stage");
	}

	@Override
	public void hide() {
//        dispose();
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
//        return input;
        return stage;
    }
    private void drawReady() {
        //batcher.draw(ready, 136/2-28, midPointY - 50, 57, 14);
        AssetLoader.font.setColor(Color.RED);
        AssetLoader.font.setScale(0.22f, 0.22f);
        AssetLoader.font.draw(batcher, "TAP TO START",
                0, 0);
    }
}
