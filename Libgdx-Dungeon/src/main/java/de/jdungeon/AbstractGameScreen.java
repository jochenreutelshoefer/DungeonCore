package de.jdungeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.jdungeon.asset.Assets;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.libgdx.LibgdxScreenContext;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public abstract class AbstractGameScreen extends de.jdungeon.game.Screen implements Screen {

	protected LibgdxDungeonMain game;
	protected boolean paused = false;

	public AbstractGameScreen(LibgdxDungeonMain game) {
		super(game);
		this.game = game;
	}


	@Override
	public void resume() {
		Assets.instance.init(new AssetManager(), game);
		paused = false;
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void dispose() {
		Assets.instance.dispose();
	}


	@Override
	public void init() {

	}

	@Override
	public void backButton() {

	}


	@Override
	public void paint(float deltaTime) {
		this.render(deltaTime);
	}

	public abstract OrthographicCamera getCamera(ScreenContext context);
}
