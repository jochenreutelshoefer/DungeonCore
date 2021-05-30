package de.jdungeon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpRequestHeader;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.game.AbstractScreen;
import de.jdungeon.game.GameAdapter;
import de.jdungeon.app.event.LevelAbortEvent;
import de.jdungeon.game.*;
import de.jdungeon.io.FilenameLister;
import de.jdungeon.game.LibgdxConfiguration;
import de.jdungeon.game.LibgdxLogger;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.ExitUsedEvent;
import de.jdungeon.event.PlayerDiedEvent;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.level.DungeonManager;
import de.jdungeon.log.Log;
import de.jdungeon.score.SessionScore;
import de.jdungeon.util.MyResourceBundle;
import de.jdungeon.dungeon.builder.DungeonFactory;
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
import de.jdungeon.util.UUIDGenerator;
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
    private FileHandleResolver resolver;
    private UUIDGenerator uuidGenerator;
    private DungeonManager dungeonManager;

    private boolean pause;

    private GameAdapter adapter;

    private DungeonSession dungeonSession;

    private Logger gdxLogger;

    public LibgdxDungeonFullMain(ResourceBundleLoader resourceBundleLoader,
                                 FilenameLister filenameLister,
                                 DungeonWorldUpdaterInitializer worldUpdaterInitializer,
                                 FileHandleResolver resolver,
                                 UUIDGenerator uuidGenerator,
                                 DungeonManager dungeonManager
    ) {
        this.resourceBundleLoader = resourceBundleLoader;
        this.filenameLister = filenameLister;
        this.worldUpdaterInitializer = worldUpdaterInitializer;
        this.resolver = resolver;
        this.uuidGenerator = uuidGenerator;
        this.dungeonManager = dungeonManager;
    }

    @Override
    public void create() {

        Gdx.app.log(TAG, "Creating application");

        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        gdxLogger = new LibgdxLogger();

        Map<String, String> configValues = new HashMap<>();
        configValues.put(GameLoopMode.GAME_LOOP_MODE_KEY, worldUpdaterInitializer.getMode().name());
        adapter = new GameAdapter(this, filenameLister, new LibgdxConfiguration(configValues));

        AssetManager assetManager = null;
        if (resolver != null) {
            // we need to add the assets prefix relative to resource folder
            assetManager = new AssetManager(resolver);
        } else {
            assetManager = new AssetManager();
        }
        Assets.instance.init(assetManager, getAudio(), getFileIO());

        MyResourceBundle textsBundle = resourceBundleLoader.getBundle(MyResourceBundle.TEXTS_BUNDLE_BASENAME, Locale.GERMAN, this);
        if (textsBundle == null) {
            Gdx.app.error(TAG, "Could not load resource bundle for texts");
        }
        JDEnv.init(textsBundle);


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
        super.setScreen((AbstractScreen) screen);
    }

    @Override
    @Deprecated // still in use in old compatibility mode
    public Screen getCurrentScreen() {
        return (AbstractScreen) this.getScreen();
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
            dungeonSession = new DefaultDungeonSession(new User("Hans Meiser"), uuidGenerator, dungeonManager);
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

            // set resources free
            destroyDungeon();


            sendHighscoreRequest();


            // change screen to de.jdungeon.skill selection
            // this.dungeonSession.notifyExit(((ExitUsedEvent)event).getExit(), ((ExitUsedEvent)de.jdungeon.event).getFigure());

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
            PlayerController controller = new PlayerController(this.dungeonSession, this.adapter);

            try {
                HeroInfo heroInfo = this.dungeonSession.initDungeon(dungeonFactory, controller);
            }
            catch (DungeonGenerationException e) {
                Log.severe("Dungeon generation error!");
                e.printStackTrace();
            }

            getCurrentScreen().pause();

            ((DefaultDungeonSession) this.dungeonSession).setGUIController(controller);

            sendHighscoreRequest();

            // create and set new
            GameScreen gameScreen = new GameScreen(this, controller, dungeonSession.getCurrentDungeon().getSize(), worldUpdaterInitializer);
            setCurrentScreen(gameScreen);

            getCurrentScreen().resume();


        }
        if (event instanceof PlayerDiedEvent) {
            // load hero from backup
            this.dungeonSession.restoreHero();

            // set resources free
            destroyDungeon();

            // show stage selection
            StageSelectionScreen screen = new StageSelectionScreen(this);
            this.setCurrentScreen(screen);
        }
    }

    private void destroyDungeon() {
        Dungeon currentDungeon = dungeonSession.getCurrentDungeon();
        if (currentDungeon != null) {
            currentDungeon.destroy();
        }
        dungeonSession.getCurrentHero().setActualDungeon(null);
        EventManager.getInstance().clearAllListeners();
        EventManager.getInstance().registerListener(this); // we need this one again
    }

    synchronized
    public void sendHighscoreRequest() {
        SessionScore score = SessionScore.create(getSession());
        Json json = new Json();

        String jsonStringScore = json.toJson(score);
        Log.info("Score as json: " + jsonStringScore);

        Gdx.app.log(TAG, "Sending highscore request.....");
        Gdx.net.sendHttpRequest(new HttpRequestBuilder().newRequest()
                        .method(Net.HttpMethods.POST)
                        .url("http://185.143.45.113:8080/Highscore/Highscore")
                        .jsonContent(score)
                        .timeout(10000)
                        .header(HttpRequestHeader.CacheControl, "no-cache")
                        .build(),
                new Net.HttpResponseListener() {
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        HttpStatus status = httpResponse.getStatus();
                        Log.info("Test request code was: " + status.getStatusCode());
                        String resultAsString = httpResponse.getResultAsString();
                        Log.info("highscore request result content was: " + resultAsString);
                        if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
                            // it was successful
                            Log.info("highscore request status code successful");
                        } else {
                            // do something else

                        }
                    }

                    @Override
                    public void failed(Throwable t) {
                        Log.info("highscore request failed: " + t.getMessage());
                    }

                    @Override
                    public void cancelled() {
                        Log.info("highscore request canceled");
                    }
                });
    }
}
