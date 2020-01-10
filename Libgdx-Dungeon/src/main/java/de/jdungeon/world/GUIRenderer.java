package de.jdungeon.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import de.jdungeon.Constants;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.19.
 */
public class GUIRenderer implements Disposable {

	private final InputController worldController;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;

	public GUIRenderer(InputController worldController, OrthographicCamera cameraGUI) {
		this.worldController = worldController;
		this.cameraGUI = cameraGUI;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		cameraGUI.update();
	}

	public void render() {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		renderGuiScore(batch);
		renderFPSCounter(batch);
		batch.end();
	}

	private void renderGuiScore(SpriteBatch batch) {
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.bucket.bucket, 110, 110);
		batch.draw(Assets.instance.drop.drop,x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		AssetFonts.instance.defaultBigFlipped.draw(batch, "0", x + 75, y + 37);
	}

	private void renderFPSCounter(SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 55;
		float y = cameraGUI.viewportHeight -15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = AssetFonts.instance.defaultNormalFlipped;
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
