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

	public Sprite[] testSprites;
	public int selectedSprite;

	public CameraHelper cameraHelper;
	private final LibgdxDungeonMain game;
	private final PlayerController playerController;

	public InputController(LibgdxDungeonMain game, PlayerController playerController) {
		this.game = game;
		this.playerController = playerController;
		init();
	}

	private void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initTestObjects();
	}

	private void backToMenu() {
		game.setScreen(new StartScreen(game));
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game World resetted");
		}
		else if (keycode == Input.Keys.SPACE) {
			selectedSprite = (selectedSprite + 1) % testSprites.length;
			if (cameraHelper.hasTarget()) {
				cameraHelper.setTarget(testSprites[selectedSprite]);
			}
			Gdx.app.debug(TAG, "Sprite #" + selectedSprite + " selected");
		}
		else if (keycode == Input.Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : testSprites[selectedSprite]);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		} else if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
			backToMenu();
		}
		return false;
	}

	private void initTestObjects() {

		testSprites = new Sprite[5];

		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.bucket.bucket);
		regions.add(Assets.instance.drop.drop);

		for (int i = 0; i < testSprites.length; i++) {
			Sprite spr = new Sprite(regions.random());
			spr.setSize(100, 100);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);

			float randomX = MathUtils.random(-200, 200);
			float randomY = MathUtils.random(-200, 200);
			spr.setPosition(randomX, randomY);

			testSprites[i] = spr;
		}

		selectedSprite = 0;

		/*
		Pixmap pixmap = createProceduralPixMap(width, height);

		Texture texture = new Texture(pixmap);

		for (int i = 0; i < testSprites.length; i++) {
			Sprite spr = new Sprite(texture);
			spr.setSize(1, 1);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);

			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
			spr.setPosition(randomX, randomY);

			testSprites[i] = spr;
		}

		selectedSprite = 0;
		*/
	}

	/*
	private Pixmap createProceduralPixMap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);

		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);

		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
*/
	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) {
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

	private void moveSelectedSprite(float x, float y) {
		testSprites[selectedSprite].translate(x, y);
	}

	private void updateTestObjects(float deltaTime) {
		float rotation = testSprites[selectedSprite].getRotation();

		rotation += 90 * deltaTime;

		rotation %= 360;

		testSprites[selectedSprite].setRotation(rotation);
	}
}
