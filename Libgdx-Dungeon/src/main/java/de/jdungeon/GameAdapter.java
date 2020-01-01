package de.jdungeon;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.apache.log4j.Logger;

import de.jdungeon.game.Audio;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.libgdx.LibgdxAudio;
import de.jdungeon.libgdx.LibgdxConfiguration;
import de.jdungeon.libgdx.LibgdxFileIO;
import de.jdungeon.libgdx.LibgdxGraphics;
import de.jdungeon.libgdx.LibgdxInput;
import de.jdungeon.libgdx.LibgdxScreenContext;
import de.jdungeon.libgdx.MyInputProcessor;
import de.jdungeon.user.Session;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.20.
 */
public class GameAdapter implements Game {

	private final LibgdxDungeonMain game;
	private LibgdxGraphics graphics;
	private final LibgdxAudio audio = new LibgdxAudio();
	private final LibgdxInput input = new LibgdxInput();
	private final LibgdxFileIO fileIO = new LibgdxFileIO();
	private final LibgdxConfiguration configuration = new LibgdxConfiguration();


	public GameAdapter(LibgdxDungeonMain game) {
		this.game = game;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics(ScreenContext context) {
		return new LibgdxGraphics(((AbstractGameScreen)getCurrentScreen()).getCamera(context));
	}

	@Override
	public void setCurrentScreen(Screen screen) {

	}

	@Override
	public Screen getCurrentScreen() {
		return game.getCurrentScreen();
	}

	@Override
	public Screen getInitScreen() {
		return null;
	}

	@Override
	public int getScreenWidth() {
		return 0;
	}

	@Override
	public int getScreenHeight() {
		return 0;
	}

	@Override
	public Configuration getConfiguration() {
		return null;
	}

	@Override
	public Session getSession() {
		return null;
	}
}
