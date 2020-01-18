package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import dungeon.util.RouteInstruction;
import figure.action.Action;

import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.ActionController;
import de.jdungeon.CameraHelper;
import de.jdungeon.welcome.StartScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class GameScreenInputController extends GestureDetector {

	private static final String TAG = GameScreenInputController.class.getName();

	public static CameraHelper cameraHelper = new CameraHelper();;

	private final LibgdxDungeonMain game;

	private final PlayerController playerController;

	private final GameScreen gameScreen;
	public GameScreenInputController(LibgdxDungeonMain game, PlayerController playerController, GameScreen gameScreen) {
		super(new MyGestureListener(cameraHelper));
		this.game = game;
		this.playerController = playerController;
		this.gameScreen = gameScreen;
		init();
	}


	public PlayerController getPlayerController() {
		return playerController;
	}

	public LibgdxDungeonMain getGame() {
		return game;
	}

	private void init() {
		Gdx.input.setInputProcessor(this);

	}

	private void backToMenu() {
		game.setScreen(new StartScreen(game));
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game World resetted");
		} else if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
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
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

		float sprMovedSpeed = 500 * deltaTime;
		/*
		if (Gdx.input.isKeyPressed(Input.Keys.A)) moveSelectedSprite(-sprMovedSpeed, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.D)) moveSelectedSprite(sprMovedSpeed, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.S)) moveSelectedSprite(0, sprMovedSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) moveSelectedSprite(0, -sprMovedSpeed);
		*/


		if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) plugWalkDirectionActions(RouteInstruction.Direction.East);
		if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) plugWalkDirectionActions(RouteInstruction.Direction.South);
		if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) plugWalkDirectionActions(RouteInstruction.Direction.West);
		if (Gdx.input.isKeyPressed((Input.Keys.UP))) plugWalkDirectionActions(RouteInstruction.Direction.North);

		// camera controls move
		float camMoveSpeed = 500 * deltaTime;
		float cameraMoveSpeedAcceleratorFactor = 5;
		if (Gdx.input.isKeyPressed((Input.Keys.SHIFT_LEFT))) camMoveSpeed *= cameraMoveSpeedAcceleratorFactor;

		if (Gdx.input.isKeyPressed((Input.Keys.D))) moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.S))) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed((Input.Keys.A))) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.W))) moveCamera(0, -camMoveSpeed);

		if (Gdx.input.isKeyPressed((Input.Keys.BACKSPACE))) cameraHelper.setPosition(0, 0);

		// camera controls zoom
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccel = 5;
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccel;
		if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);

	}

	private void plugWalkDirectionActions(RouteInstruction.Direction direction) {
		ActionController actionController = playerController.getActionController();
		List<Action> actions = actionController
				.getActionAssembler()
				.wannaWalk(direction.getValue());
		actionController.plugActions(actions);
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	public void zoomIn() {
		cameraHelper.addZoom(-0.1f);
	}

	public void zoomOut() {
		cameraHelper.addZoom(0.1f);
	}

	static class MyGestureListener implements GestureDetector.GestureListener {

		public CameraHelper cameraHelper;

		public MyGestureListener(CameraHelper cameraHelper) {
			this.cameraHelper = cameraHelper;
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

			float moveFactor = 0.4f;
			if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
				// on desktop we need a faster move factor than on android
				moveFactor = 0.7f;
			}
			if(Gdx.app.getType() == Application.ApplicationType.Android) {
				// on desktop we need a faster move factor than on android
				moveFactor = 0.4f;
			}


			cameraHelper.getPosition().x = cameraHelper.getPosition().x - (dx * moveFactor);
			cameraHelper.getPosition().y = cameraHelper.getPosition().y - (dy * moveFactor);
			return true;
		}

		@Override
		public boolean panStop(float x, float y, int a, int b) {
			return false;
		}

		@Override
		public boolean zoom(float v, float v1) {
			cameraHelper.addZoom(v+v1);
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
