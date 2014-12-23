package com.maurice.virolLibgdx.ZBHelpers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.maurice.virolLibgdx.GameWorld.GameWorld;
import com.maurice.virolLibgdx.ui.SimpleButton;

import java.util.List;

public class InputHandler implements InputProcessor {
	private GameWorld myWorld;

	private List<SimpleButton> menuButtons;
//    GameScreen gameScreen;
	private SimpleButton playButton;

	private float scaleFactorX;
	private float scaleFactorY;

	public InputHandler(float scaleFactorX,
			float scaleFactorY) {
		this.myWorld = GameWorld.getInstance();

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;

//		menuButtons = new ArrayList<SimpleButton>();
//		playButton = new SimpleButton(
//				136 / 2 - (AssetLoader.playButtonUp.getRegionWidth() / 2),
//				midPointY + 50, 29, 16, AssetLoader.playButtonUp,
//				AssetLoader.playButtonDown);
//		menuButtons.add(playButton);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
        System.out.println("TouchDown...!"+myWorld.isMenu()+""+myWorld.isReady()+""+myWorld.isRunning());
		if (myWorld.isMenu()) {
			//playButton.isTouchDown(screenX, screenY);
			myWorld.ready();
            myWorld.start();
		} else if (myWorld.isReady()) {
//            gameScreen.getSettingsMenu();
//            myWorld.isMenu();
//			myWorld.start();
		} else if (myWorld.isRunning()) {
            myWorld.getCircleController().onclick(screenX,screenY);
		}

		if (myWorld.isGameOver() || myWorld.isHighScore()) {
			myWorld.restart();
		}

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		/*if (myWorld.isMenu()) {
			if (playButton.isTouchUp(screenX, screenY)) {
				myWorld.ready();
				return true;
			}
		}*/

		return false;
	}

	@Override
	public boolean keyDown(int keycode) {

		// Can now use Space Bar to play the game
		if (keycode == Keys.SPACE) {

			if (myWorld.isMenu()) {
				myWorld.ready();
			} else if (myWorld.isReady()) {
				myWorld.start();
			}


			if (myWorld.isGameOver() || myWorld.isHighScore()) {
				myWorld.restart();
			}

		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}

	public List<SimpleButton> getMenuButtons() {
		return menuButtons;
	}
}
