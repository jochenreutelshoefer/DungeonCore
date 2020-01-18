package de.jdungeon;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.apache.log4j.Logger;

import de.jdungeon.asset.Assets;
import de.jdungeon.asset.LibgdxAssetImageLoader;
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
	private final LibgdxFileIO fileIO = new LibgdxFileIO(new LibgdxAssetImageLoader());
	private final LibgdxConfiguration configuration = new LibgdxConfiguration();

	Map<AbstractGameScreen, LibgdxGraphics> graphicsMap = new HashMap<>();

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
		Screen currentScreen = getCurrentScreen();
		LibgdxGraphics graphics = graphicsMap.get(currentScreen);
		if(graphics == null) {
			graphics = new LibgdxGraphics(((AbstractGameScreen) currentScreen).getCamera(context), Assets.instance.fonts.defaultSmallFlipped);
			graphicsMap.put((AbstractGameScreen)currentScreen, graphics);
		}
		return graphics;
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
		return new LibgdxConfiguration();
	}

	@Override
	public Session getSession() {
		return null;
	}
}
