package de.jdungeon.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public class MenuScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();

	SpriteBatch batch = new SpriteBatch();

	public MenuScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float v) {
		Gdx.gl.glClearColor(0.0f,0.0f,0.0f,1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
		}

		batch.begin();
		batch.draw(Assets.instance.bucket.bucket, 100, 100);
		batch.end();
	}

	@Override
	public void resize(int i, int i1) {

	}

	@Override
	public void pause() {

	}
}
