package de.jdungeon.game;

import java.util.HashMap;
import java.util.Map;

import de.jdungeon.asset.Assets;
import de.jdungeon.asset.LibgdxAssetImageLoader;
import de.jdungeon.io.FilenameLister;
import de.jdungeon.user.Session;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.20.
 */
public class GameAdapter implements Game {

    private final Game game;
    private LibgdxGraphics graphics;
    private final LibgdxAudio audio = new LibgdxAudio();
    private final LibgdxInput input = new LibgdxInput();
    private final LibgdxFileIO fileIO;
    private final Configuration configuration;

    //Map<AbstractScreen, LibgdxGraphics> graphicsMap = new HashMap<>();

    public GameAdapter(Game game, FilenameLister filenameLister, Configuration configuration) {
        this.game = game;
        fileIO = new LibgdxFileIO(new LibgdxAssetImageLoader(), filenameLister);
        this.configuration = configuration;
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
        if (graphics == null) {
            graphics = new LibgdxGraphics(((AbstractScreen) currentScreen).getCamera(context), Assets.instance.fonts.defaultSmallFlipped);
        }
        return graphics;
    }

    @Override
    public void setCurrentScreen(Screen screen) {
        game.setCurrentScreen(screen);
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
        return configuration;
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
