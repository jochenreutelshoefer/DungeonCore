package de.jdungeon.stage;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.jdungeon.AbstractGameScreen;
import de.jdungeon.CameraHelper;
import de.jdungeon.LibgdxDungeonFullMain;
import de.jdungeon.app.gui.dungeonselection.DungeonSelectionScreen;
import de.jdungeon.app.gui.dungeonselection.LevelIconImageManager;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.libgdx.LibgdxInput;
import de.jdungeon.libgdx.MyInputProcessor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.19.
 */
public class StageSelectionScreen extends AbstractGameScreen {

	private final OrthographicCamera camera;
	private final CameraHelper cameraHelper;
	private final DungeonSelectionScreen selectionScreen;

	public StageSelectionScreen(LibgdxDungeonFullMain game) {
		super(game);

		// TODO: factor out path
		LevelIconImageManager.getInstance().init(game, "pics/levelIcons");
		selectionScreen = new DungeonSelectionScreen(game);

		int camStartPosX = Gdx.app.getGraphics().getWidth()/2;
		int camStartPosY = Gdx.app.getGraphics().getHeight()/2;

		// init world camera and world renderer
		camera = new OrthographicCamera(10, 10);
		camera.setToOrtho(true);
		camera.position.set(camStartPosX, camStartPosY, 0);
		camera.update();

		cameraHelper = new CameraHelper(camStartPosX, camStartPosY);

		Gdx.input.setInputProcessor(new MyInputProcessor((LibgdxInput)game.getInput()));

	}

	@Override
	public boolean clicked(int screenX, int screenY, int pointer, int button) {
		// uses legacy input processor
		return false;
	}

	@Override
	public boolean pan(float x, float y, float dx, float dy) {
		return false;
	}

	@Override
	public boolean zoom(float v1, float v2) {
		return false;
	}

	@Override
	public void update(float deltaTime) {
		cameraHelper.update(deltaTime);
		selectionScreen.update(deltaTime);
		checkControls(deltaTime);
	}

	private void checkControls(float deltaTime) {
		if (Gdx.app.getType() != Application.ApplicationType.Desktop) return;


		// camera controls move
		float camMoveSpeed = 50 * deltaTime;
		float cameraMoveSpeedAcceleratorFactor = 5;
		if (Gdx.input.isKeyPressed((Input.Keys.SHIFT_LEFT))) camMoveSpeed *= cameraMoveSpeedAcceleratorFactor;
		if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.RIGHT))) moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.DOWN))) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed((Input.Keys.LEFT))) moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed((Input.Keys.UP))) moveCamera(0, -camMoveSpeed);
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


	@Override
	public OrthographicCamera getCamera(ScreenContext context) {
		return camera;
	}

	@Override
	public void show() {
		selectionScreen.resume();
		//MusicManager.getInstance().stopCurrentMusic();
		//Music music = getGame().getAudio().createMusic("music/" + "For_the_Fallen.mp3");
		//MusicManager.getInstance().playMusic(music);
	}

	@Override
	public void render(float deltaTime) {
		if(paused) return;
		update(deltaTime);
		cameraHelper.applyTo(camera);
		//((LibgdxGraphics)de.jdungeon.game.getGraphics(null)).beginSpriteBatch();
		selectionScreen.paint(deltaTime);
		//((LibgdxGraphics)de.jdungeon.game.getGraphics(null)).endSpriteBatch();
	}

	@Override
	public void resize(int width, int height) {
		selectionScreen.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
		selectionScreen.pause();
	}

	@Override
	public void resume() {
		super.resume();
		selectionScreen.resume();
	}
}
