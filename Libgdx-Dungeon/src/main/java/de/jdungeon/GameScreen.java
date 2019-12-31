package de.jdungeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class GameScreen extends AbstractGameScreen{

	private final static String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;

	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
	}

	@Override
	public void render(float deltaTime) {
		if(!paused) {
			worldController.update(deltaTime);
		}

		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		super.resume();
		paused = false;
	}
}
