package de.jdungeon.world;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import de.jdungeon.asset.Assets;
import de.jdungeon.CameraHelper;
import de.jdungeon.welcome.StartScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();

	public Sprite[] testSprites;
	public int selectedSprite;

	public CameraHelper cameraHelper;
	private final Game game;

	public WorldController(Game game) {
		this.game = game;
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

		int width = 32;
		int height = 32;

		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.bucket.bucket);
		regions.add(Assets.instance.drop.drop);

		for (int i = 0; i < testSprites.length; i++) {
			Sprite spr = new Sprite(regions.random());

			spr.setSize(1, 1);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);

			float randomX = MathUtils.random(-2.0f, 2.0f);
			float randomY = MathUtils.random(-2.0f, 2.0f);
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

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;

		float sprMovedSpeed = 5 * deltaTime;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) moveSelectedSprite(-sprMovedSpeed, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.D)) moveSelectedSprite(sprMovedSpeed, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) moveSelectedSprite(0, sprMovedSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.S)) moveSelectedSprite(0, -sprMovedSpeed);

		// camera controls move
		float camMoveSpeed = 5 * deltaTime;
		float cameraMoveSpeedAcceleratorFactor = 5;
		if (Gdx.input.isKeyPressed((Input.Keys.SHIFT_LEFT))) camMoveSpeed *= cameraMoveSpeedAcceleratorFactor;
		if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.UP))) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed((Input.Keys.BACKSPACE))) cameraHelper.setPosition(0, 0);

		// camera controls zoom
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccel = 5;
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccel;
		if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) cameraHelper.setZoom(1);
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
