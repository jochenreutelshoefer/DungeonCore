package de.jdungeon;

import java.util.HashMap;
import java.util.Map;

import de.jdungeon.asset.Assets;
import de.jdungeon.asset.LibgdxAssetImageLoader;
import de.jdungeon.game.*;
import de.jdungeon.io.FilenameLister;
import de.jdungeon.libgdx.LibgdxAudio;
import de.jdungeon.libgdx.LibgdxConfiguration;
import de.jdungeon.libgdx.LibgdxFileIO;
import de.jdungeon.libgdx.LibgdxGraphics;
import de.jdungeon.libgdx.LibgdxInput;
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
    private final LibgdxFileIO fileIO;
    private final LibgdxConfiguration configuration = new LibgdxConfiguration();

    Map<AbstractGameScreen, LibgdxGraphics> graphicsMap = new HashMap<>();

    GameAdapter(LibgdxDungeonMain game, FilenameLister filenameLister) {
        this.game = game;
        fileIO = new LibgdxFileIO(new LibgdxAssetImageLoader(), filenameLister);
        GameEnv.getInstance().setGame(this);
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
        if (graphics == null) {
            graphics = new LibgdxGraphics(((AbstractGameScreen) currentScreen).getCamera(context), Assets.instance.fonts.defaultSmallFlipped);
            graphicsMap.put((AbstractGameScreen) currentScreen, graphics);
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

    @Override
    public Logger getLogger() {
        return game.getLogger();
    }
}
