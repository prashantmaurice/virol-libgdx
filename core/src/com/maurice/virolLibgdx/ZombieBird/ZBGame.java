package com.maurice.virolLibgdx.ZombieBird;

import com.badlogic.gdx.Game;
import com.maurice.virolLibgdx.Screens.SplashScreen;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;

public class ZBGame extends Game {

	@Override
	public void create() {
		AssetLoader.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}

}