package de.jdungeon.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import de.jdungeon.Constants;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class WorldRenderer implements Disposable {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private final WorldController worldController;

	public WorldRenderer(WorldController worldController, OrthographicCamera camera) {
		this.worldController = worldController;
		this.camera = camera;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		camera.update();

	}

	public void render() {
		renderTestObjects();
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


	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();

	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
