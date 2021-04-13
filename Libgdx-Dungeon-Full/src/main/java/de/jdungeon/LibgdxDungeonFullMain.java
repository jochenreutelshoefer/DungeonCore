package de.jdungeon;

import java.util.Collection;
import java.util.Locale;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import de.jdungeon.app.event.LevelAbortEvent;
import de.jdungeon.game.*;
import de.jdungeon.io.FilenameLister;
import de.jdungeon.libgdx.LibgdxConfiguration;
import de.jdungeon.libgdx.LibgdxLogger;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.ExitUsedEvent;
import de.jdungeon.event.PlayerDiedEvent;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.util.MyResourceBundle;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonStartEvent;
import de.jdungeon.spell.Spell;
import de.jdungeon.user.DefaultDungeonSession;
import de.jdungeon.user.DungeonSession;

import de.jdungeon.app.SessionEvents;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.QuitGameEvent;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.app.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.asset.Assets;
import de.jdungeon.io.ResourceBundleLoader;
import de.jdungeon.stage.StageSelectionScreen;
import de.jdungeon.user.User;
import de.jdungeon.welcome.StartScreen;
import de.jdungeon.world.GameScreen;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class LibgdxDungeonFullMain extends Game implements de.jdungeon.game.Game, EventListener {

    private static final String TAG = LibgdxDungeonFullMain.class.getName();
    private final ResourceBundleLoader resourceBundleLoader;
    private FilenameLister filenameLister;
    private DungeonWorldUpdaterInitializer worldUpdaterInitializer;

    private boolean pause;

    private GameAdapter adapter;

    private DungeonSession dungeonSession;

    private Logger gdxLogger;

    public LibgdxDungeonFullMain(ResourceBundleLoader resourceBundleLoader, FilenameLister filenameLister, DungeonWorldUpdaterInitializer worldUpdaterInitializer) {
        this.resourceBundleLoader = resourceBundleLoader;
        this.filenameLister = filenameLister;
        this.worldUpdaterInitializer = worldUpdaterInitializer;
    }

    @Override
    public void create() {

        Gdx.app.log(TAG, "Creating application");

        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        gdxLogger = new LibgdxLogger();

        MyResourceBundle textsBundle = resourceBundleLoader.getBundle(MyResourceBundle.TEXTS_BUNDLE_BASENAME, Locale.GERMAN, this);
        if (textsBundle == null) {
            Gdx.app.error(TAG, "Could not load resource bundle for texts");
        }
        JDEnv.init(textsBundle);

        adapter = new GameAdapter(this, filenameLister, new LibgdxConfiguration());

        Assets.instance.init(new AssetManager(), getAudio(), getFileIO());

        EventManager.getInstance().registerListener(this);

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
        // this is actually the important point setting the new Screen into the Libgdx app
        super.setScreen((AbstractGameScreen) screen);
    }

    @Override
    @Deprecated // still in use in old compatibility mode
    public Screen getCurrentScreen() {
        return (AbstractGameScreen) this.getScreen();
    }

    @Override
    @Deprecated // not used
    public Screen getInitScreen() {
        return null;
        //return new StartScreen(this);
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
    public DefaultDungeonSession getSession() {
        return (DefaultDungeonSession) dungeonSession;
    }

    @Override
    public Logger getLogger() {
        return gdxLogger;
    }

    @Override
    public Collection<Class<? extends Event>> getEvents() {
        return SessionEvents.getSessionEventClasses();
    }

    @Override
    public void notify(Event event) {
        AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
        if (event instanceof StartNewGameEvent) {
            dungeonSession = new DefaultDungeonSession(new User("Hans Meiser"));
            ((DefaultDungeonSession) dungeonSession).setSelectedHeroType(Hero.HeroCategory.Druid.getCode());

			/*
			EventManager.getInstance().fireEvent(new DungeonStartEvent(new DungeonSelectedEvent(new StartLevel())));
			*/

            StageSelectionScreen screen = new StageSelectionScreen(this);
            this.setCurrentScreen(screen);

        }
        if (event instanceof QuitGameEvent) {
            this.dispose();
        }
        if (event instanceof ExitUsedEvent) {

            // pause screen rendering
            this.getScreen().pause();

            // change screen to de.jdungeon.skill selection
            //this.dungeonSession.notifyExit(((ExitUsedEvent)de.jdungeon.event).getExit(), ((ExitUsedEvent)de.jdungeon.event).getFigure());

            de.jdungeon.skillselection.SkillSelectionScreen screen = new de.jdungeon.skillselection.SkillSelectionScreen(this);
            this.setCurrentScreen(screen);

            // resume/start rendering of screen
            this.getScreen().resume();

        }
        if (event instanceof LevelAbortEvent) {
            setCurrentScreen(new StartScreen(this));
        }
        if (event instanceof SkillSelectedEvent) {
            Spell spell = ((SkillSelectedEvent) event).getSpell();
            dungeonSession.learnSkill(spell);
            StageSelectionScreen screen = new StageSelectionScreen(this);
            this.setCurrentScreen(screen);
        }
        if (event instanceof DungeonStartEvent) {
            Gdx.app.log(TAG, "App: processing DungeonStartEvent");
            // initialize new dungeon
            DungeonFactory dungeonFactory = ((DungeonStartEvent) event).getEvent().getDungeon();

            // create new controller
            PlayerController controller = new PlayerController(this.dungeonSession);

            HeroInfo heroInfo = this.dungeonSession.initDungeon(dungeonFactory, controller);

            getCurrentScreen().pause();

            ((DefaultDungeonSession) this.dungeonSession).setGUIController(controller);

            // create and set new GameScreen
            GameScreen gameScreen = new GameScreen(this, controller, dungeonSession.getCurrentDungeon().getSize(), worldUpdaterInitializer);
            setCurrentScreen(gameScreen);

            getCurrentScreen().resume();


        }
        if (event instanceof PlayerDiedEvent) {
            this.dungeonSession.revertHero();
            StageSelectionScreen screen = new StageSelectionScreen(this);
            this.setCurrentScreen(screen);
        }
    }
}
