package de.jdungeon.example;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class CanyonBunnyMain implements ApplicationListener {

	private static final String TAG = CanyonBunnyMain.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean pause;

	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Assets.instance.init(new AssetManager());

		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);


		pause = false;

		/*
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera(1, height / width);
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("data/droplet.png"));
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, 64, 64);
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(10, 10);
		sprite.setPosition(50, 50);
		*/
	}

	@Override
	public void resize(int x, int y) {
		worldRenderer.resize(x, y);
	}

	@Override
	public void render() {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.

		if(!pause) {
			worldController.update(Gdx.graphics.getDeltaTime());
		}

		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();

		// tell the camera to update its matrices.
		//camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		//batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bucket and
		// all drops
		//batch.begin();
		//batch.draw(texture, 50, 50);
		//sprite.draw(batch);
		//batch.end();
	}

	@Override
	public void pause() {
		pause = true;
	}

	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
		pause = false;

	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
}
