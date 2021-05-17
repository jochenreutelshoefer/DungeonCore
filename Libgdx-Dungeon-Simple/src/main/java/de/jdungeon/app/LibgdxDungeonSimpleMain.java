package de.jdungeon.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;

import de.jdungeon.dungeon.builder.*;
import de.jdungeon.dungeon.level.Level16x16;
import de.jdungeon.dungeon.level.LevelX;
import de.jdungeon.game.AbstractScreen;
import de.jdungeon.game.GameAdapter;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.LevelAbortEvent;
import de.jdungeon.app.event.QuitGameEvent;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.app.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.asset.Assets;
import de.jdungeon.event.*;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.game.*;
import de.jdungeon.graphics.GraphicObjectRenderer;
import de.jdungeon.io.FilenameLister;
import de.jdungeon.io.ResourceBundleLoader;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonStartEvent;
import de.jdungeon.game.LibgdxConfiguration;
import de.jdungeon.game.LibgdxLogger;
import de.jdungeon.level.SingleDungeonManager;
import de.jdungeon.log.Log;
import de.jdungeon.spell.Spell;
import de.jdungeon.user.DefaultDungeonSession;
import de.jdungeon.user.DungeonSession;
import de.jdungeon.user.User;
import de.jdungeon.util.MyResourceBundle;
import de.jdungeon.util.UUIDGenerator;
import de.jdungeon.world.CameraHelper;
import de.jdungeon.world.GameScreen;
import de.jdungeon.world.PlayerController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This app is a Spike. It should test out whether the dungeon
 * can be refactored in a way that the Dungeon World Thread
 * and the Render Thread can be unified to a single Thread.
 * That is: The render thread also triggers the dungeon.
 * -> that would be necessary to compile the dungeon app to GWT.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.21.
 */
public class LibgdxDungeonSimpleMain extends Game implements de.jdungeon.game.Game, EventListener {

	private static final String TAG = LibgdxDungeonSimpleMain.class.getName();
	private final ResourceBundleLoader resourceBundleLoader;
	private FilenameLister filenameLister;

	private boolean pause;

	private GameAdapter adapter;

	private DungeonSession dungeonSession;
	private final DungeonWorldUpdaterInitializer worldUpdaterInitializer;
	private FileHandleResolver resolver;
	private UUIDGenerator uuidGenerator;

	private Logger gdxLogger;
	private SingleDungeonManager simpleDungeonManager;

	public LibgdxDungeonSimpleMain(ResourceBundleLoader resourceBundleLoader,
								   FilenameLister filenameLister,
								   DungeonWorldUpdaterInitializer renderLoopDungeonWorldUpdaterInitializer,
								   FileHandleResolver resolver,
								   UUIDGenerator uuidGenerator
	) {
		this.resourceBundleLoader = resourceBundleLoader;
		this.filenameLister = filenameLister;
		this.worldUpdaterInitializer = renderLoopDungeonWorldUpdaterInitializer;
		this.resolver = resolver;
		this.uuidGenerator = uuidGenerator;
	}

	@Override
	public void create() {

		Gdx.app.log(TAG, "Creating application");

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		gdxLogger = new LibgdxLogger();

		Map<String, String> configValues = new HashMap<>();
		configValues.put(GameLoopMode.GAME_LOOP_MODE_KEY, worldUpdaterInitializer.getMode().name());
		configValues.put(GraphicObjectRenderer.DEBUG_MODE, "true");
		configValues.put(CameraHelper.INIT_ZOOM_VALUE, "1.3f");
		adapter = new GameAdapter(this, filenameLister, new LibgdxConfiguration(configValues));

		AssetManager assetManager = null;
		if (resolver != null) {
			// we need to add the assets prefix relative to resource folder
			assetManager = new AssetManager(resolver);
		}
		else {
			assetManager = new AssetManager();
		}
		Assets.instance.init(assetManager, getAudio(), getFileIO());

		MyResourceBundle textsBundle = resourceBundleLoader.getBundle(MyResourceBundle.TEXTS_BUNDLE_BASENAME, Locale.GERMAN, this);
		if (textsBundle == null) {
			Gdx.app.error(TAG, "Could not load resource bundle for texts");
		}
		JDEnv.init(textsBundle);

		EventManager.getInstance().registerListener(this);

		//simpleDungeonManager = SingleDungeonManager.create(new Level16x16());
		simpleDungeonManager = SingleDungeonManager.create(new LevelX());
		dungeonSession = new DefaultDungeonSession(new User("Hans Meiser"), uuidGenerator, simpleDungeonManager);
		((DefaultDungeonSession) dungeonSession).setSelectedHeroType(Hero.HeroCategory.Druid.getCode());
		Gdx.app.log(TAG, "App: processing DungeonStartEvent");

		// initialize new dungeon
		DungeonFactory dungeonFactory = dungeonSession.getDungeonManager()
				.getDungeonOptions(dungeonSession.getCurrentStage())
				.get(0);

		if (dungeonFactory != null) {

			// create new controller
			PlayerController controller = new PlayerController(this.dungeonSession, this.adapter);

			try {
				this.dungeonSession.initDungeon(dungeonFactory, controller);
			}
			catch (DungeonGenerationException e) {
				Log.severe("Could not create level: " + e.getMessage());
				e.printStackTrace();
			}
			//dungeonSession.getCurrentHero().setVisAll();

			// start world -> do NOT start the game loop in a distinct thread!
			((DefaultDungeonSession) this.dungeonSession).setGUIController(controller);

			// create and set new GameScreen
			GameScreen gameScreen = new GameScreen(this, controller, dungeonSession.getCurrentDungeon()
					.getSize(), worldUpdaterInitializer);
			setCurrentScreen(gameScreen);
			gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			pause = false;
		}
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
			dungeonSession = new DefaultDungeonSession(new User("Hans Meiser"), uuidGenerator, simpleDungeonManager);
			((DefaultDungeonSession) dungeonSession).setSelectedHeroType(Hero.HeroCategory.Druid.getCode());

			/*
			EventManager.getInstance().fireEvent(new DungeonStartEvent(new DungeonSelectedEvent(new StartLevel())));
			*/

			//StageSelectionScreen screen = new StageSelectionScreen(this);
			//this.setCurrentScreen(screen);

		}
		if (event instanceof QuitGameEvent) {
			this.dispose();
		}
		if (event instanceof ExitUsedEvent) {

			// pause screen rendering
			this.getScreen().pause();

			// change screen to de.jdungeon.skill selection
			//this.dungeonSession.notifyExit(((ExitUsedEvent)event).getExit(), ((ExitUsedEvent)event).getFigure());
			// SkillSelectionScreen screen = new de.jdungeon.skillselection.SkillSelectionScreen(this);
			//this.setCurrentScreen(screen);

			// resume/start rendering of screen
			this.getScreen().resume();
		}
		if (event instanceof LevelAbortEvent) {
			int foo = 0;
			//setCurrentScreen(new StartScreen(this));
		}
		if (event instanceof SkillSelectedEvent) {
			Spell spell = ((SkillSelectedEvent) event).getSpell();
			dungeonSession.learnSkill(spell);
			// StageSelectionScreen screen = new StageSelectionScreen(this);
			//this.setCurrentScreen(screen);
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
				Log.severe("Could not create level: " + e.getMessage());
				e.printStackTrace();
			}

			getCurrentScreen().pause();

			// create and set new GameScreen
			GameScreen gameScreen = new GameScreen(this, controller, dungeonSession.getCurrentDungeon()
					.getSize(), new RenderLoopWorldUpdateInitializer());
			setCurrentScreen(gameScreen);

			getCurrentScreen().resume();
		}
		if (event instanceof PlayerDiedEvent) {
			this.dungeonSession.restoreHero();
			//StageSelectionScreen screen = new StageSelectionScreen(this);
			//this.setCurrentScreen(screen);
		}
	}
}
