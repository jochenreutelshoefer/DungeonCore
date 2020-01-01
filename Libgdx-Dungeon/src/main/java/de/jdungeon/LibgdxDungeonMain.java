package de.jdungeon;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.jdungeon.asset.Assets;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.user.Session;
import de.jdungeon.welcome.StartScreen;
import de.jdungeon.world.WorldController;
import de.jdungeon.world.WorldRenderer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class LibgdxDungeonMain extends Game implements de.jdungeon.game.Game {

	private static final String TAG = LibgdxDungeonMain.class.getName();

	private WorldController worldController;
	private boolean pause;

	private GameAdapter adapter;

	@Override
	public void create() {

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Assets.instance.init(new AssetManager());

		worldController = new WorldController(this);

		adapter = new GameAdapter(this);

		super.setScreen(new StartScreen(this));

		pause = false;

	}

	@Override
	public Audio getAudio() {
		return adapter.getAudio();
	}

	@Override
	public Input getInput() {
		return adapter.getInput();
	}

	@Override
	public FileIO getFileIO() {
		return adapter.getFileIO();
	}

	@Override
	public Graphics getGraphics(ScreenContext context) {
		return adapter.getGraphics(context);
	}

	@Override
	public void setCurrentScreen(Screen screen) {
		super.setScreen((AbstractGameScreen)screen);
	}

	@Override
	public Screen getCurrentScreen() {
		return (AbstractGameScreen)this.getScreen();
	}

	@Override
	public Screen getInitScreen() {
		return new StartScreen(this);
	}

	@Override
	public int getScreenWidth() {
		return Gdx.app.getGraphics().getWidth();
	}

	@Override
	public int getScreenHeight() {
		return Gdx.app.getGraphics().getHeight();
	}

	@Override
	public Configuration getConfiguration() {
		return adapter.getConfiguration();
	}

	@Override
	public Session getSession() {
		return adapter.getSession();
	}
}
