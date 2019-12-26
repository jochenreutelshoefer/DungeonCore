package de.jdungeon.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class WorldRenderer implements Disposable {

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private final WorldController worldController;

	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();

		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true);
		camera.update();
	}

	public void render() {
		renderTestObjects();
		renderGUI(batch);
	}

	private void renderTestObjects() {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Sprite sprite : worldController.testSprites) {
			sprite.draw(batch);
		}
		batch.end();
	}

	private void renderGUI(SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		renderGuiScore(batch);
		renderFPSCounter(batch);
		batch.end();
	}

	private void renderGuiScore(SpriteBatch batch) {
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.bucket.bucket,x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, ""+worldController.selectedSprite, x + 75, y + 37);
	}

	private void renderFPSCounter(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight -15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if(fps >= 45) {
			fpsFont.setColor(0,1,0,1);
		} else if(fps >= 30) {
			fpsFont.setColor(1,1,0,1);
		} else  {
			fpsFont.setColor(1,0,0,1);
		}
		fpsFont.draw(batch, "FPS: "+fps, x, y);
		fpsFont.setColor(1,1,1,1); //white
	}

	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();

		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth =  (Constants.VIEWPORT_GUI_HEIGHT / height) * width;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight/2, 0);
		cameraGUI.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
