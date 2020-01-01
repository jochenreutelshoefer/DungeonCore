package de.jdungeon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import de.jdungeon.asset.Assets;
import de.jdungeon.welcome.StartScreen;
import de.jdungeon.world.WorldController;
import de.jdungeon.world.WorldRenderer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class LibgdxDungeonMain extends Game  {

	private static final String TAG = LibgdxDungeonMain.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	private boolean pause;

	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Assets.instance.init(new AssetManager());

		worldController = new WorldController(this);
		worldRenderer = new WorldRenderer(worldController);


		this.setScreen(new StartScreen(this));

		pause = false;

	}

}
