package com.maurice.virolLibgdx.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.maurice.virolLibgdx.GameObjects.Circle;
import com.maurice.virolLibgdx.GameObjects.CircleController;
import com.maurice.virolLibgdx.GameObjects.Point;
import com.maurice.virolLibgdx.TweenAccessors.Value;
import com.maurice.virolLibgdx.TweenAccessors.ValueAccessor;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;
import com.maurice.virolLibgdx.ui.SimpleButton;
import com.maurice.virolLibgdx.ui.UIColors;

import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;


public class GameRenderer {

	private GameWorld myWorld;
    private static GameRenderer instance;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;


    //DIMENSIONS
    private Vector2 gameScreenDim;
    private Point boardDimensions;
    private Point tileDimensions;
    private int circleDia;


	private SpriteBatch batcher;

	private int midPointY;

	// Game Objects
	private CircleController circleController;
    private Circle[][] circlesArray;

	// Game Assets
	private TextureRegion[] circleMap = new TextureRegion[4];
    private TextureRegion blast1;
	private Animation birdAnimation;

	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private List<SimpleButton> menuButtons;
	private Color transitionColor;

    //colors
    private Color bluePlayer = UIColors.GAME_PLAYER_BLUE;
    private Color redPlayer = UIColors.GAME_PLAYER_RED;
    private Color greenText = UIColors.GREEN;
    private Color whiteText = UIColors.WHITE;


	public GameRenderer(GameWorld world) {
		myWorld = world;
        gameScreenDim = new Vector2(ZBGame.GAME_WIDTH, ZBGame.GAME_HEIGHT);
        boardDimensions = new Point(ZBGame.GAME_WIDTH,ZBGame.GAME_HEIGHT-ZBGame.GAME_DASHBOARD_HEIGHT);
        tileDimensions = new Point(boardDimensions.x/GameWorld.ROWS, boardDimensions.y/GameWorld.COLUMNS);
        circleDia = Math.min(tileDimensions.x,tileDimensions.y);
		midPointY = (int) (gameScreenDim.y/2);

        //setup render settings
		cam = new OrthographicCamera(gameScreenDim.x, gameScreenDim.y);
		cam.setToOrtho(true, gameScreenDim.x, gameScreenDim.y);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
        cam.update();

        //get additional variables from myworld and assets
		initGameObjects();
		initAssets();

        //Start Transition
		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);
        instance = this;
	}
    public static GameRenderer getInstance(){
        return instance;
    }

	private void initGameObjects() {
        circleController = myWorld.getCircleController();
        circlesArray = circleController.getCiclesArray();
	}

	private void initAssets() {
        circleMap[0] = AssetLoader.circle0;
        circleMap[1] = AssetLoader.circle1;
        circleMap[2] = AssetLoader.circle2;
        circleMap[3] = AssetLoader.circle3;
        blast1 = AssetLoader.blast1;
	}

    public void render(float delta, float runTime) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // Draw Background color
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(UIColors.GAME_BG);
        shapeRenderer.rect(0, 0, boardDimensions.x, boardDimensions.y);
        if(GameWorld.currPlayState== GameWorld.PlayState.PLAYER)
            shapeRenderer.setColor(UIColors.GAME_PLAYER_BLUE);
        else if(GameWorld.currPlayState== GameWorld.PlayState.OPPONENT)
            shapeRenderer.setColor(UIColors.GAME_PLAYER_RED);
        else
            shapeRenderer.setColor(UIColors.GAME_ANIM_LINE_COLOR);
        shapeRenderer.rect(0, boardDimensions.y-1, gameScreenDim.x, 1);
        shapeRenderer.setColor(UIColors.GAME_BG_BOTTOM);
        shapeRenderer.rect(0, boardDimensions.y, gameScreenDim.x, gameScreenDim.y-boardDimensions.y);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.set(ShapeType.Line);
        drawTerritory();
        shapeRenderer.end();


        batcher.begin();
//        enable this to set background
//        batcher.disableBlending();
//        batcher.draw(bg, 0, midPointY + 23, 136, 43);


        batcher.enableBlending();

        if (myWorld.isRunning()) {
            drawCircles(runTime);
        } else if (myWorld.isReady()) {
            drawReady();
        } else if (myWorld.isMenu()) {
        } else if (myWorld.isGameOver()) {
            drawCircles(runTime);
//            drawBottom();
            drawGameOver();
            drawRetry();
        } else if (myWorld.isHighScore()) {
            drawHighScore();
            drawRetry();
        }


        batcher.end();

        drawTransition(delta);

    }

    private void drawTerritory() {
        int SEGMENTS = 10;

        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                Circle circle_temp = circlesArray[i][j];
                Point actualPosition = new Point((i*tileDimensions.x)+(tileDimensions.x-circleDia)/2,
                        (j*tileDimensions.y)+(tileDimensions.y-circleDia)/2);
                boolean hasLeft = circleController.hasSimilarLeft(i, j);
                boolean hasRight = circleController.hasSimilarRight(i, j);
                boolean hasTop = circleController.hasSimilarTop(i, j);
                boolean hasBottom = circleController.hasSimilarBottom(i, j);

                if(circlesArray[i][j].getValue()==0){

                }
                else if(circlesArray[i][j].isOpponent()){

                }else{
                    int dia = circleDia;
                    int curve_radius = (int) (dia*Math.sqrt(2)/4);
                    Point x1,c1,c2,x2,x3,c3,x4,c4,x5,c5,x6,c6,x7,c7,x8,c8;

                    if(hasLeft){
                        x1 = new Point(-1,curve_radius);
                        c1 = new Point(dia/4,curve_radius);
                        x8 = new Point(-1,dia-curve_radius);
                        c8 = new Point(dia/4,dia-curve_radius);
                    }else{
                        x1 = new Point(((dia/2)-curve_radius),dia/2);
                        c1 = new Point(((dia/2)-curve_radius),dia/4);
                        x8 = new Point(((dia/2)-curve_radius),dia/2);
                        c8 = new Point(((dia/2)-curve_radius),dia*3/4);
                    }
                    if(hasRight){
                        x4 = new Point(dia,curve_radius);
                        c4 = new Point(dia*3/4,curve_radius);
                        x5 = new Point(dia,dia-curve_radius);
                        c5 = new Point(dia*3/4,dia-curve_radius);
                    }else{
                        x4 = new Point(((dia/2)+curve_radius),dia/2);
                        c4 = new Point(((dia/2)+curve_radius),dia/4);
                        x5 = new Point(((dia/2)+curve_radius),dia/2);
                        c5 = new Point(((dia/2)+curve_radius),dia*3/4);
                    }
                    if(hasTop){
                        x2 = new Point(curve_radius,0);
                        c2 = new Point(curve_radius,dia/4);
                        x3 = new Point(dia-curve_radius,0);
                        c3 = new Point(dia-curve_radius,dia/4);
                    }else{
                        x2 = new Point(dia/2,((dia/2)-curve_radius));
                        c2 = new Point(dia/4,((dia/2)-curve_radius));
                        x3 = new Point(dia/2,((dia/2)-curve_radius));
                        c3 = new Point(dia*3/4,((dia/2)-curve_radius));
                    }
                    if(hasBottom){
                        x6 = new Point(dia-curve_radius,dia);
                        c6 = new Point(dia-curve_radius,dia*3/4);
                        x7 = new Point(curve_radius,dia);
                        c7 = new Point(curve_radius,dia*3/4);
                    }else{
                        x6 = new Point(dia/2,((dia/2)+curve_radius));
                        c6 = new Point(dia*3/4,((dia/2)+curve_radius));
                        x7 = new Point(dia/2,((dia/2)+curve_radius));
                        c7 = new Point(dia/4,((dia/2)+curve_radius));
                    }

                    x1.addPoint(actualPosition);
                    x2.addPoint(actualPosition);
                    x3.addPoint(actualPosition);
                    x4.addPoint(actualPosition);
                    x5.addPoint(actualPosition);
                    x6.addPoint(actualPosition);
                    x7.addPoint(actualPosition);
                    x8.addPoint(actualPosition);
                    c1.addPoint(actualPosition);
                    c2.addPoint(actualPosition);
                    c3.addPoint(actualPosition);
                    c4.addPoint(actualPosition);
                    c5.addPoint(actualPosition);
                    c6.addPoint(actualPosition);
                    c7.addPoint(actualPosition);
                    c8.addPoint(actualPosition);

                    //remove corners when fill and final draw
                    if(!(circleController.hasSimilarGeneric(i,j,i-1,j-1)&(hasLeft)&(hasTop)))
                        shapeRenderer.curve(x1.x,x1.y,c1.x,c1.y,c2.x,c2.y,x2.x,x2.y,SEGMENTS);
                    if(!(circleController.hasSimilarGeneric(i,j,i+1,j-1)&(hasRight)&(hasTop)))
                        shapeRenderer.curve(x3.x,x3.y,c3.x,c3.y,c4.x,c4.y,x4.x,x4.y,SEGMENTS);
                    if(!(circleController.hasSimilarGeneric(i,j,i+1,j+1)&(hasRight)&(hasBottom)))
                        shapeRenderer.curve(x5.x,x5.y,c5.x,c5.y,c6.x,c6.y,x6.x,x6.y,SEGMENTS);
                    if(!(circleController.hasSimilarGeneric(i,j,i-1,j+1)&(hasLeft)&(hasBottom)))
                        shapeRenderer.curve(x7.x,x7.y,c7.x,c7.y,c8.x,c8.y,x8.x,x8.y,SEGMENTS);
                }

            }
        }
    }

    private void drawCircles(float runTime){
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                if(circlesArray[i][j].getValue()==0)batcher.setColor(Color.GRAY);
                else if(circlesArray[i][j].isOpponent()){
                    batcher.setColor(redPlayer);
                }else{
                    batcher.setColor(bluePlayer);
                }
                batcher.draw(circleMap[circlesArray[i][j].getValue()],
                        (i*tileDimensions.x)+(tileDimensions.x-circleDia)/2,
                        (j*tileDimensions.y)+(tileDimensions.y-circleDia)/2,
                        circleDia/2,circleDia/2,circleDia,circleDia,
                        0.5f, 0.5f, circlesArray[i][j].getRotation());

                //draw blasts
                if(circlesArray[i][j].inBlast()) {

                    float alpha = 2 * circlesArray[i][j].getBlastRadius();
                    if (alpha > 1) alpha = 2 - alpha;
                    if(circlesArray[i][j].isOpponent()) batcher.setColor(redPlayer.r, redPlayer.g, redPlayer.b, alpha);
                    else batcher.setColor(bluePlayer.r, bluePlayer.g, bluePlayer.b, alpha);
                    if(circlesArray[i][j].rightArrowOn)
                        batcher.draw(blast1,
                            (i*tileDimensions.x)+(tileDimensions.x-circleDia)/2 + circlesArray[i][j].getBlastRadius()*circleDia,
                            (j*tileDimensions.y)+(tileDimensions.y-circleDia)/2,
                            circleDia / 2, circleDia/ 2, circleDia,circleDia,
                            1, 1, 0);
                    if(circlesArray[i][j].bottomArrowOn)
                        batcher.draw(blast1,
                                (i*tileDimensions.x)+(tileDimensions.x-circleDia)/2,
                                (j*tileDimensions.y)+(tileDimensions.y-circleDia)/2+ circlesArray[i][j].getBlastRadius()*circleDia,
                                circleDia / 2, circleDia/ 2, circleDia,circleDia,
                            1, 1, 90);
                    if(circlesArray[i][j].leftArrowOn)
                        batcher.draw(blast1,
                                (i*tileDimensions.x)+(tileDimensions.x-circleDia)/2- circlesArray[i][j].getBlastRadius()*circleDia,
                                (j*tileDimensions.y)+(tileDimensions.y-circleDia)/2,
                                circleDia / 2, circleDia/ 2, circleDia,circleDia,
                            1, 1, 180);
                    if(circlesArray[i][j].topArrowOn)
                        batcher.draw(blast1,
                                (i*tileDimensions.x)+(tileDimensions.x-circleDia)/2,
                                (j*tileDimensions.y)+(tileDimensions.y-circleDia)/2- circlesArray[i][j].getBlastRadius()*circleDia,
                                circleDia / 2, circleDia/ 2, circleDia,circleDia,
                            1, 1, 270);

                }
            }
        }
//        drawDebug();

        drawBottom();

    }

	private void drawRetry() {
		//batcher.draw(retry, 136/2-28, midPointY + 10, 56, 14);
		AssetLoader.whiteFont.setScale(0.06f, -0.06f);
		AssetLoader.whiteFont.draw(batcher, "TAP TO RETRY",
				34, midPointY + 90);
	}

	private void drawReady() {
		//batcher.draw(ready, 136/2-28, midPointY - 50, 57, 14);
		AssetLoader.helvetica.setScale(0.06f, -0.06f);
		AssetLoader.helvetica.draw(batcher, "TAP TO START",
				44, midPointY - 70);
	}

	private void drawGameOver() {

        AssetLoader.whiteFont.setScale(0.12f, -0.12f);
        AssetLoader.whiteFont.draw(batcher, "GAMEOVER",
                14, midPointY - 10);
        AssetLoader.whiteFont.setScale(0.06f, -0.06f);

        String text  = ((myWorld.LAST_WON_OPPONENT)?"REDS":"BLUES")+" WON";
        AssetLoader.whiteFont.draw(batcher, text,
                34, midPointY + 10);
	}

	private void drawScore() {
		int length = ("" + myWorld.getScore()).length();
		//AssetLoader.shadow.draw(batcher, "" + myWorld.getScore(),
			//	68 - (3 * length), midPointY - 82);
		AssetLoader.whiteFont.setScale(0.25f, -0.25f);
		AssetLoader.whiteFont.draw(batcher, "" + myWorld.getScore(),
				68 - (3 * length), midPointY - 90);
	}
    private void drawBottom(){
        String text2 = "TAP TO PAUSE";
        if((GameWorld.currPlayMode == GameWorld.PlayMode.ONLINE)||(GameWorld.currPlayMode == GameWorld.PlayMode.MULTIPLAYER)){
            if(GameWorld.currPlayState== GameWorld.PlayState.PLAYER)
                text2 = "YOUR MOVE";
            else if(GameWorld.currPlayState== GameWorld.PlayState.OPPONENT)
                text2 = "OPPONENTS MOVE";
        }

        AssetLoader.whiteFont.setColor(whiteText);
        AssetLoader.whiteFont.setScale(0.03f*ZBGame.FONT_SCALE, -0.03f*ZBGame.FONT_SCALE);
        AssetLoader.whiteFont.draw(batcher, text2,
                boardDimensions.x/2-(text2.length()*4), midPointY + 98);
        AssetLoader.whiteFont.draw(batcher, ""+GameWorld.GAME_SCORE,
                10, midPointY + 80);


        AssetLoader.whiteFont.setScale(0.04f*ZBGame.FONT_SCALE, -0.04f*ZBGame.FONT_SCALE);
        String text = "";
        for(int i=0;i<GameWorld.GAME_SCORE/10;i++){
            text+="-";
        }
        if(GameWorld.GAME_SCORE>0){
            AssetLoader.whiteFont.setColor(whiteText);
            AssetLoader.whiteFont.draw(batcher, text,
                boardDimensions.x/2, midPointY + 84);
        }else{
            AssetLoader.whiteFont.setColor(redPlayer);
            AssetLoader.whiteFont.draw(batcher, text,
                boardDimensions.x/2, midPointY + 84);
        }


    }

    private void drawDebug(){

        AssetLoader.font.setColor(whiteText);
        AssetLoader.font.setScale(0.06f, -0.06f);

        AssetLoader.font.draw(batcher, "" + GameWorld.GAME_SCORE,
                0, midPointY + 90);

        AssetLoader.whiteFont.setScale(0.04f, -0.04f);
        int length = ("" + GameWorld.currPlayState).length();
        AssetLoader.font.draw(batcher, ""+GameWorld.currPlayState,
                gameScreenDim.x- (6 * length), midPointY + 93);
    }

	private void drawHighScore() {
		//batcher.draw(highScore, 22, midPointY - 50, 96, 14);
	}



	public void prepareTransition(int r, int g, int b, float duration) {
		transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
		alpha.setValue(1);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, duration).target(0)
				.ease(TweenEquations.easeOutQuad).start(manager);
	}

	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(transitionColor.r, transitionColor.g,
					transitionColor.b, alpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

		}
	}

    public void onclick(int screenX, int screenY) {

        if(circleController.runningAnimations>0) return;
        int i= screenX/tileDimensions.x;
        int j= screenY/tileDimensions.y;
        if((i>= GameWorld.ROWS)||(j>= GameWorld.COLUMNS)){
            //clicked on bottom buttons
            if(GameWorld.currPlayMode != GameWorld.PlayMode.ONLINE) circleController.pauseGame();
        }else{
            if(GameWorld.currPlayMode == GameWorld.PlayMode.ONLINE){
                myWorld.sendMoveServer(i,j);
            }else{
                circleController.move(i,j);
            }
        }

    }
}
