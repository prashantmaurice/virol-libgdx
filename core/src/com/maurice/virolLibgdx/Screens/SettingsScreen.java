package com.maurice.virolLibgdx.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.Transitions.ScreenTransition;
import com.maurice.virolLibgdx.Transitions.ScreenTransitionSlide;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;
import com.maurice.virolLibgdx.ZBHelpers.UIObjectGenerater;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;
import com.maurice.virolLibgdx.ui.UIColors;

public class SettingsScreen extends AbstractGameScreen {

    SpriteBatch batcher;
    private static final String PREFS_NAME = "user";
    private int gameSpeed;
    private float screenHeight, screenWidth;
    private ZBGame game;
    private ShapeRenderer shapeRenderer;
    private Stage stage = new Stage();
    private Table table = new Table();
    Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
//    private final Slider gyroSensitivity;
    private TextButton buttonBackToMenu = new TextButton("back", skin);


    public SettingsScreen(ZBGame game){
        super(game);
        this.game=game;
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        System.out.println("SKIN:"+skin);
        OrthographicCamera cam = new OrthographicCamera(ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT);
        cam.setToOrtho(true, ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        cam.update();

        Gdx.input.setInputProcessor(stage);
        System.out.println("setInputProcessor screenWidstageth");
        System.out.println("screen width="+screenWidth);


//        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

//        AssetLoader.font2.setColor(Color.BLACK);
//        skin.add("default", AssetLoader.font2);

        // Create a table that fills the screen. Everything else will go inside this table.
//        Table table = new Table();
//        table.setFillParent(true);
//        stage.addActor(table);

//        Viewport temp  = new Viewport(){};
//        temp.setScreenHeight((int) screenHeight);
//        temp.setScreenWidth((int) screenHeight);
//        stage.setViewport(temp);

        //LOAD PREFERENCES


        //GYRO SENSITIVITY
//        Label nameLabel = new Label("Gyro Sensitivity:", skin);
//        nameLabel.setFontScale(0.7f);
//        gyroSensitivity = new Slider(2, 10, 1, false, skin);
//        gyroSensitivity.setValue(game.GYROSENSITIVITY);

//        table.add(nameLabel).padBottom(screenHeight/15);
//        table.row();
//        table.add(gyroSensitivity).width((int)screenWidth*0.8f);

        //SUBMIT BUTTON
//        table.row().padTop(screenHeight/15);
//        final TextButton button = new TextButton("SAVE", skin);
//        button.pad(20);
//        button.setColor(colorFromHex(0xFF419FFFL));
//        table.add(button);
//        button.addListener(new ChangeListener() {
//            public void changed(ChangeEvent event, Actor actor) {
//                updateSettings();
//                System.out.println("slider position: " + gyroSensitivity.getValue());
//
//                getMainGame();
//            }
//        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //DRAW BACKGROUND
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(colorFromHex(0xFF222222L));
        shapeRenderer.rect(0,0, screenWidth, screenHeight);
        shapeRenderer.setColor(colorFromHex(0xFF007DFDL));
        shapeRenderer.rect(0,0, screenWidth, 40);
        shapeRenderer.end();

        //DRAW TABLE
        stage.act();
        stage.draw();

    }

    public void loadSetings(){
        //Preferences prefs = Gdx.app.getPreferences( PREFS_NAME );
        //prefs.putInteger( "high1", 1000 );
        //getPrefs().flush();

        //gameSpeed=prefs.getInteger("speed");
        //System.out.println("GameSpeed="+gameSpeed);
        //getPrefs().putInteger( "speed", 23 );
        //getPrefs().flush();
    }
    public void updateSettings(){
//        Preferences prefs = Gdx.app.getPreferences( game.getPrefsName());
//
//        //UPDATE GYRO SETTINGS
//        prefs.putInteger( game.GYRO_KEY, (int) gyroSensitivity.getValue() );
//        game.GYROSENSITIVITY=(int) gyroSensitivity.getValue();
//
//        prefs.flush();
//        //game.GYROSENSITIVITY=prefs.getInteger(game.GYRO_KEY);
//        System.out.println("new Gyrosensitivity="+game.GYROSENSITIVITY);
//        //getPrefs().putInteger( "speed", 23 );
//        System.out.println( Gdx.app.getPreferences( game.getPrefsName()));
//	        /*System.out.println("high2="+getPrefs().getInteger( "high2", 0 ));
//	   	 if(highScore>yourScore){
//	   		 highScore=yourScore;
//	   		 prefs.putInteger( "high1", yourScore );
//	   	 }*/
    }
    protected Preferences getPrefs(){
        return Gdx.app.getPreferences( PREFS_NAME );
    }


    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void dispose () {
        stage.dispose();
        skin.dispose();
    }


    @Override
    public void show() {
        stage = new Stage();

        loadSetings();
        buttonBackToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Back button clicked");
                getMainMenu();
            }
        });

        //SETUP STYLES
        TextButton.TextButtonStyle tabOptionStyle = new TextButton.TextButtonStyle();
        tabOptionStyle.down = new TextureRegionDrawable(AssetLoader.blankBG);
        tabOptionStyle.up = new TextureRegionDrawable(AssetLoader.blankBG);
        tabOptionStyle.fontColor = Color.BLACK;
        tabOptionStyle.font = AssetLoader.whiteFont;


        TextButton.TextButtonStyle menuButtonStyle = buttonBackToMenu.getStyle();
        AssetLoader.whiteFont.setScale(0.08f*ZBGame.FONT_SCALE,0.08f*ZBGame.FONT_SCALE);
        menuButtonStyle.font = AssetLoader.whiteFont;
        menuButtonStyle.fontColor = Color.WHITE;
        buttonBackToMenu.setStyle(menuButtonStyle);
        buttonBackToMenu.setColor(UIColors.MENU_BUTTON);


        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.

//        float width = Gdx.graphics.getWidth();
//        float desiredWidth = width * 1.9f;
//        float scale = desiredWidth / logoImage.getWidth();
//        scale = 2.1f;
//        logoImage.scaleBy(scale,scale);
//        logoImage.setSize(logoImage.getWidth() * scale, logoImage.getHeight() * scale);
//        table.add(logoImage).size(logoImage.getWidth()*scale,logoImage.getHeight()*scale).row();
//        table.add(title).padBottom(40).row();
        int padBottom = (int) (3*ZBGame.FONT_SCALE);
        int buttonHeight = (int) (14*ZBGame.FONT_SCALE);
        int buttonWidth = (int) (screenWidth*0.8f);

        VerticalGroup gamePrefStack = new VerticalGroup();
        gamePrefStack.setHeight(screenHeight);
        gamePrefStack.setWidth(screenWidth);

        Table table1 = new Table();
        table1.setWidth(screenWidth);
        Table table2 = new Table();
        table2.setWidth(screenWidth);
        Table table3 = new Table();
        table3.setWidth(screenWidth);
        gamePrefStack.addActor(table1);
        gamePrefStack.addActor(table2);
        gamePrefStack.addActor(table3);

        Label label1  = UIObjectGenerater.generateLabel("SOUND");
        TextButton b1 = new TextButton("ON", tabOptionStyle);
        TextButton b2 = new TextButton("OFF", tabOptionStyle);
        b2.setColor(UIColors.SETTINGS_TAB_BLUE);

        Label label2 = UIObjectGenerater.generateLabel("SOUND2");
        TextButton b3 = new TextButton("ON", tabOptionStyle);
        TextButton b4 = new TextButton("OFF", tabOptionStyle);
        TextButton b5 = new TextButton("OFF", tabOptionStyle);
        b3.setColor(UIColors.SETTINGS_TAB_BLUE);

        table1.add(label1).size(screenWidth-200,100);
        table1.add(b1).size(100,100);
        table1.add(b2).size(100, 100);

        table2.add(label2).size(screenWidth-300,100);
        table2.add(b3).size(100,100);
        table2.add(b4).size(100, 100);
        table2.add(b5).size(100, 100);

        table3.add(buttonBackToMenu).size(buttonWidth, buttonHeight);
//        table.setFillParent(true);
//        table.setHeight(screenHeight);
//        table.setWidth(screenWidth);
        stage.addActor(gamePrefStack);

        Gdx.input.setInputProcessor(stage);
        System.out.println("setInputProcessor stage");
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }
    @Override
    public InputProcessor getInputProcessor () {
        System.out.println("Settings input processor requested");
        return stage;
    }
    public void getMainMenu(){
        //ScreenTransition transition = ScreenTransitionFade.init(0.75f);
        //game.setScreen(new testtwo(game), transition);

        ScreenTransition transition = ScreenTransitionSlide.init(0.75f,
                ScreenTransitionSlide.LEFT, false, Interpolation.sineOut);
        game.setScreen(new MenuScreen(game), transition);
        System.out.println("Menu Screen called");
    }
    private Color colorFromHex(long hex){
        float a = (hex & 0xFF000000L) >> 24;
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);
        return new Color(r/255f, g/255f, b/255f, a/255f);
    }
}