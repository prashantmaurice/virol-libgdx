package com.maurice.virolLibgdx.ZBHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture circleTexture1,circleTexture2,circleTexture3,circleTexture0,
            logoTexture,blastTexture1;
	public static TextureRegion circle1, circle2, circle3, circle0,
            logo, blast1;
	public static Animation birdAnimation;
	public static Sound dead, flap, coin, fall;
	public static BitmapFont font, shadow, whiteFont;
	private static Preferences prefs;

    private static int CIRCLES_DIA = 96;

	public static void load() {

		logoTexture = new Texture(Gdx.files.internal("data/logo.png"));
		logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//
		logo = new TextureRegion(logoTexture, 0, 0, 512, 114);

        //FOR CIRCLES
        circleTexture0 = new Texture(Gdx.files.internal("data/circles/circle0.png"));
        circle0 = new TextureRegion(circleTexture0, 0, 0, CIRCLES_DIA, CIRCLES_DIA);
        circle0.flip(false, true);

        circleTexture1 = new Texture(Gdx.files.internal("data/circles/circle1.png"));
        circle1 = new TextureRegion(circleTexture1, 0, 0, CIRCLES_DIA, CIRCLES_DIA);
        circle1.flip(false, true);

        circleTexture2 = new Texture(Gdx.files.internal("data/circles/circle2.png"));
        circle2 = new TextureRegion(circleTexture2, 0, 0, CIRCLES_DIA, CIRCLES_DIA);
        circle2.flip(false, true);

        circleTexture3 = new Texture(Gdx.files.internal("data/circles/circle3.png"));
        circle3 = new TextureRegion(circleTexture3, 0, 0, CIRCLES_DIA, CIRCLES_DIA);
        circle3.flip(false, true);

        //BLAST
        blastTexture1 = new Texture(Gdx.files.internal("data/circles/adder.png"));
        blast1 = new TextureRegion(blastTexture1, 0, 0, CIRCLES_DIA, CIRCLES_DIA);
        blast1.flip(false, true);

//		texture = new Texture(Gdx.files.internal("data/texture3.png"));
//		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
//		texture2 = new Texture(Gdx.files.internal("data/texture2.png"));
//		texture2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
//		texture4 = new Texture(Gdx.files.internal("data/scoreboard.png"));
//		texture4.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
//
//		playButtonUp = new TextureRegion(texture2, 0, 83*2, 29*2, 16*2);
//		playButtonDown = new TextureRegion(texture2, 29*2, 83*2, 29*2, 16*2);
//		playButtonUp.flip(false, true);
//		playButtonDown.flip(false, true);
//
//		scoreboard = new TextureRegion(texture4, 0, 0, 404, 246);
//		scoreboard.flip(false, true);
//
//		ready = new TextureRegion(texture4, 0, 0, 10, 12);
//		ready.flip(false, true);
//
//		retry = new TextureRegion(texture4, 0, 42, 170, 42);
//		retry.flip(false, true);
//
//		gameOver = new TextureRegion(texture4, 0, 84, 201, 42);
//		gameOver.flip(false, true);
//
//
//		noStar = new TextureRegion(texture2, 25, 231, 25, 25);
//		star = new TextureRegion(texture2, 0, 231, 25, 25);
//
//		star.flip(false, true);
//		noStar.flip(false, true);
//
//		highScore = new TextureRegion(texture4, 0, 126, 201, 42);
//		highScore.flip(false, true);
//
//		zbLogo = new TextureRegion(texture2, 0, 120, 320, 40);
//		zbLogo.flip(false, true);
//
//		bg = new TextureRegion(texture2, 0, 0, 266, 110);
//		bg.flip(false, true);
//
//		grass = new TextureRegion(texture2,52, 234, 143*2, 11*2);
//		grass.flip(false, true);
//
//		birdDown = new TextureRegion(texture2, 362, 0, 47, 34);
//		birdDown.flip(false, true);
//
//		bird = new TextureRegion(texture2, 315, 0, 47, 34);
//		bird.flip(false, true);
//
//		birdUp = new TextureRegion(texture2, 268, 0, 47, 34);
//		birdUp.flip(false, true);
//
//		TextureRegion[] birds = { birdDown, bird, birdUp };
//		birdAnimation = new Animation(0.06f, birds);
//		birdAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
//
//		skullUp = new TextureRegion(texture, 192, 0, 24, 14);
//		// Create by flipping existing skullUp
//		skullDown = new TextureRegion(skullUp);
//		skullDown.flip(false, true);
//
//		bar = new TextureRegion(texture2, 411, 0, 102, 256);
//		bar.flip(false, true);
//		barflip = new TextureRegion(texture2, 411, 0, 102, 256);
//		barflip.flip(false, false);

		dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
		flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
		coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		fall = Gdx.audio.newSound(Gdx.files.internal("data/fall.wav"));

		font = new BitmapFont(Gdx.files.internal("data/aharoni_blue.fnt"));
		font.setScale(0.25f, -0.25f);
		
		whiteFont = new BitmapFont(Gdx.files.internal("data/aharoni_white.fnt"));
		whiteFont.setScale(0.25f, -0.25f);
		


		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("ZombieBird");

		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}

        System.out.println("All assets loaded");
	}

	public static void setHighScore(int val) {
		prefs.putInteger("highScore", val);
		prefs.flush();
	}

	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}

	public static void dispose() {
		// We must dispose of the texture when we are finished.
//		texture.dispose();
//		texture2.dispose();

		// Dispose sounds
		dead.dispose();
		flap.dispose();
		coin.dispose();

		font.dispose();
		shadow.dispose();
	}

}