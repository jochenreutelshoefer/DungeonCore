package de.jdungeon.world;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import de.jdungeon.app.event.LevelAbortEvent;
import de.jdungeon.event.EventManager;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class GameScreenInputProcessor extends GestureDetector {

	private static final String TAG = GameScreenInputProcessor.class.getName();
	private final Game game;

	private final PlayerController playerController;
	private CameraHelper cameraHelper = null;

	private final GameScreen gameScreen;
	private final KeyboardControl keyboardControl;

	public GameScreenInputProcessor(Game game, PlayerController playerController, GameScreen gameScreen) {
		super(new MyGestureListener(gameScreen));
		cameraHelper = gameScreen.getCameraHelper();
		this.game = game;
		this.playerController = playerController;
		this.gameScreen = gameScreen;
		keyboardControl = new KeyboardControl(playerController, cameraHelper);

	}


	public PlayerController getPlayerController() {
		return playerController;
	}

	public Game getGame() {
		return game;
	}


	private void backToMenu() {
		EventManager.getInstance().fireEvent(new LevelAbortEvent());
	}

	@Override
	public boolean keyUp(int keycode) {
		 if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
			backToMenu();
		}
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return gameScreen.clicked(screenX, screenY, pointer, button);
	}

	public void update(float deltaTime) {
		handleControlEvents(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void handleControlEvents(float deltaTime) {
		// we do not need to do anything on mobile devices
		if (Gdx.app.getType() != Application.ApplicationType.Desktop &&  Gdx.app.getType() != Application.ApplicationType.WebGL) return;

		keyboardControl.handleKeyEvents(deltaTime);

	}

	public void zoomIn() {
		cameraHelper.calibrateUserZoomLevel();
		cameraHelper.addUserSelectedZoomLevel(-0.1f);
		scrollToUserSelectedZoomLevel();
	}

	public void zoomOut() {
		cameraHelper.calibrateUserZoomLevel();
		cameraHelper.addUserSelectedZoomLevel(0.1f);
		scrollToUserSelectedZoomLevel();
	}

	public void scrollToPlayer() {
		gameScreen.scollToPlayerPosition(0.8f);
	}

	private void scrollToUserSelectedZoomLevel() {
		gameScreen.scrollToScale(0.3f, cameraHelper.getUserSelectedZoomLevel());
	}



	static class MyGestureListener implements GestureDetector.GestureListener {

		public GameScreen gameScreen;

		public MyGestureListener(GameScreen gameScreen) {
			this.gameScreen = gameScreen;
		}

		@Override
		public boolean touchDown(float v, float v1, int i, int i1) {
			return false;
		}

		@Override
		public boolean tap(float v, float v1, int i, int i1) {
			return false;
		}

		@Override
		public boolean longPress(float v, float v1) {
			return false;
		}

		@Override
		public boolean fling(float v, float v1, int i) {
			return false;
		}

		@Override
		public boolean pan(float x, float y, float dx, float dy) {
			int threshold = 100;
			if(Math.abs(dx) > threshold || Math.abs(dy) > threshold) return false;
			return gameScreen.pan(x, y, dx, dy);
		}

		@Override
		public boolean panStop(float x, float y, int a, int b) {
			return false;
		}

		@Override
		public boolean zoom(float v, float v1) {
			gameScreen.zoom(v, v1);
			return true;
		}

		@Override
		public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23) {
			return false;
		}

		@Override
		public void pinchStop() {

		}
	}

}
