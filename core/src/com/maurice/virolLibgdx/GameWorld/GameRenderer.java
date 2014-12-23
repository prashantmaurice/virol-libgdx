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
import com.maurice.virolLibgdx.TweenAccessors.Value;
import com.maurice.virolLibgdx.TweenAccessors.ValueAccessor;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;
import com.maurice.virolLibgdx.ZBHelpers.InputHandler;
import com.maurice.virolLibgdx.ui.SimpleButton;

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
    private Vector2 gameDimensions;


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
    private Color bluePlayer = new Color(51f / 255f, 181f / 255f, 229f / 255f, 1);

	public GameRenderer(GameWorld world, int gameHeight, int gameWidth) {
		myWorld = world;
        gameDimensions = new Vector2(gameWidth, gameHeight);
        myWorld.createBoard((int)gameDimensions.x,(int)gameDimensions.y);

        myWorld.setDimensions(gameDimensions);
		this.midPointY = (int) (gameDimensions.y/2);
		this.menuButtons = ((InputHandler) Gdx.input.getInputProcessor())
				.getMenuButtons();

		cam = new OrthographicCamera(gameDimensions.x, gameDimensions.y);
		cam.setToOrtho(true, gameDimensions.x, gameDimensions.y);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		initGameObjects();
		initAssets();
        cam.update();

		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);
	}
    public static GameRenderer getInstance(){
        return instance;
    }

	private void initGameObjects() {
        circleController = myWorld.getCircleController();
	}

	private void initAssets() {
        circleMap[0] = AssetLoader.circle0;
        circleMap[1] = AssetLoader.circle1;
        circleMap[2] = AssetLoader.circle2;
        circleMap[3] = AssetLoader.circle3;
        blast1 = AssetLoader.blast1;
	}

    public void render(float delta, float runTime) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeType.Filled);

        // Draw Background color
        shapeRenderer.setColor(80 / 255.0f, 88 / 255.0f, 91 / 255.0f, 1);
        shapeRenderer.rect(0, 0, gameDimensions.x, gameDimensions.y);

        shapeRenderer.end();

        batcher.begin();
        batcher.disableBlending();

//        enable this to set background
//        batcher.draw(bg, 0, midPointY + 23, 136, 43);


        batcher.enableBlending();
        //drawSkulls();

        if (myWorld.isRunning()) {
            drawCircles(runTime);
        } else if (myWorld.isReady()) {
            drawReady();
        } else if (myWorld.isMenu()) {
        } else if (myWorld.isGameOver()) {
            drawGameOver();
            drawRetry();
        } else if (myWorld.isHighScore()) {
            drawHighScore();
            drawRetry();
        }


        batcher.end();
        drawTransition(delta);

    }

    private void drawCircles(float runTime){
        circlesArray = circleController.getCiclesArray();
        for(int i=0;i<GameWorld.ROWS;i++){
            for(int j=0;j<GameWorld.COLUMNS;j++){
                if(circlesArray[i][j].getValue()==0)batcher.setColor(Color.GRAY);
                else batcher.setColor(bluePlayer);
                batcher.draw(circleMap[circlesArray[i][j].getValue()],
                        circlesArray[i][j].getActualPosition().x ,
                        circlesArray[i][j].getActualPosition().y,
                        circlesArray[i][j].getCircleDia()/2, circlesArray[i][j].getCircleDia()/2,
                        circlesArray[i][j].getCircleDia(), circlesArray[i][j].getCircleDia(),
                        1, 1, circlesArray[i][j].getRotation());

                //draw blasts
                if(circlesArray[i][j].inBlast()) {

                    float alpha = 2 * circlesArray[i][j].getBlastRadius() / circlesArray[i][j].getCircleDia();
                    if (alpha > 1) alpha = 2 - alpha;
                    batcher.setColor(bluePlayer.r, bluePlayer.g, bluePlayer.b, alpha);
                    batcher.draw(blast1,
                            circlesArray[i][j].getActualPosition().x + circlesArray[i][j].getBlastRadius(),
                            circlesArray[i][j].getActualPosition().y,
                            circlesArray[i][j].getCircleDia() / 2, circlesArray[i][j].getCircleDia() / 2,
                            circlesArray[i][j].getCircleDia(), circlesArray[i][j].getCircleDia(),
                            1, 1, 0);
                    batcher.draw(blast1,
                            circlesArray[i][j].getActualPosition().x,
                            circlesArray[i][j].getActualPosition().y + circlesArray[i][j].getBlastRadius(),
                            circlesArray[i][j].getCircleDia() / 2, circlesArray[i][j].getCircleDia() / 2,
                            circlesArray[i][j].getCircleDia(), circlesArray[i][j].getCircleDia(),
                            1, 1, 90);
                    batcher.draw(blast1,
                            circlesArray[i][j].getActualPosition().x - circlesArray[i][j].getBlastRadius(),
                            circlesArray[i][j].getActualPosition().y,
                            circlesArray[i][j].getCircleDia() / 2, circlesArray[i][j].getCircleDia() / 2,
                            circlesArray[i][j].getCircleDia(), circlesArray[i][j].getCircleDia(),
                            1, 1, 180);
                    batcher.draw(blast1,
                            circlesArray[i][j].getActualPosition().x,
                            circlesArray[i][j].getActualPosition().y - circlesArray[i][j].getBlastRadius(),
                            circlesArray[i][j].getCircleDia() / 2, circlesArray[i][j].getCircleDia() / 2,
                            circlesArray[i][j].getCircleDia(), circlesArray[i][j].getCircleDia(),
                            1, 1, 270);

                }
            }
        }

    }

	private void drawRetry() {
		//batcher.draw(retry, 136/2-28, midPointY + 10, 56, 14);
		AssetLoader.font.setScale(0.06f, -0.06f);
		AssetLoader.font.draw(batcher, "TAP TO RETRY",
				34, midPointY + 90);
	}

	private void drawReady() {
		//batcher.draw(ready, 136/2-28, midPointY - 50, 57, 14);
		AssetLoader.font.setScale(0.06f, -0.06f);
		AssetLoader.font.draw(batcher, "READY",
				48, midPointY - 70);
	}

	private void drawGameOver() {
		//batcher.draw(gameOver, 136/2-33, midPointY - 50, 67, 14);
	}

	private void drawScore() {
		int length = ("" + myWorld.getScore()).length();
		//AssetLoader.shadow.draw(batcher, "" + myWorld.getScore(),
			//	68 - (3 * length), midPointY - 82);
		AssetLoader.font.setScale(0.25f, -0.25f);
		AssetLoader.font.draw(batcher, "" + myWorld.getScore(),
				68 - (3 * length), midPointY - 90);
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

    public Vector2 getGameDimensions() {
        return gameDimensions;
    }

}
