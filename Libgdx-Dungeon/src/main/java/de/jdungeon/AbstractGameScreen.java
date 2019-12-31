package de.jdungeon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.19.
 */
public abstract class AbstractGameScreen implements Screen {

	protected Game game;

	public AbstractGameScreen(Game game) {
		this.game = game;
	}


	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		Assets.instance.dispose();
	}
}
