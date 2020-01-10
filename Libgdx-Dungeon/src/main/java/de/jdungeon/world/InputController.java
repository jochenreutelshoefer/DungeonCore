package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import dungeon.util.RouteInstruction;
import figure.action.Action;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.ActionController;
import de.jdungeon.asset.Assets;
import de.jdungeon.CameraHelper;
import de.jdungeon.welcome.StartScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class InputController extends InputAdapter {

	private static final String TAG = InputController.class.getName();

	public CameraHelper cameraHelper;
	private final LibgdxDungeonMain game;
	private final PlayerController playerController;
	private final GameScreen gameScreen;

	public InputController(LibgdxDungeonMain game, PlayerController playerController, GameScreen gameScreen) {
		this.game = game;
		this.playerController = playerController;
		this.gameScreen = gameScreen;
		init();
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
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

}
