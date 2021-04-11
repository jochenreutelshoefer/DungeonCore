package de.jdungeon;

import java.util.Collection;
import java.util.Locale;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import de.jdungeon.game.*;
import de.jdungeon.libgdx.LibgdxLogger;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.ExitUsedEvent;
import de.jdungeon.event.PlayerDiedEvent;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.game.MyResourceBundle;
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
public class LibgdxDungeonMain extends Game implements de.jdungeon.game.Game, EventListener {

	private static final String TAG = LibgdxDungeonMain.class.getName();
	private final ResourceBundleLoader resourceBundleLoader;

	private boolean pause;

	private GameAdapter adapter;

	private DungeonSession dungeonSession;

	private Logger gdxLogger;

	public LibgdxDungeonMain(ResourceBundleLoader resourceBundleLoader) {
		this.resourceBundleLoader = resourceBundleLoader;
	}

	@Override
	public void create() {

		Gdx.app.log(TAG,"Creating application");

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		gdxLogger = new LibgdxLogger();

		MyResourceBundle textsBundle = resourceBundleLoader.getBundle(JDEnv.TEXTS_BUNDLE_BASENAME, Locale.GERMAN, this);
		if(textsBundle == null) {
			Gdx.app.error(TAG, "Could not load resource bundle for texts");
		}
		JDEnv.init(textsBundle);

		adapter = new GameAdapter(this);

		Assets.instance.init(new AssetManager(), this);

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
	public DefaultDungeonSession getSession() {
		return (DefaultDungeonSession)dungeonSession;
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
		if(event instanceof StartNewGameEvent) {
			dungeonSession = new DefaultDungeonSession(new User("Hans Meiser"));
			((DefaultDungeonSession)dungeonSession).setSelectedHeroType(Hero.HeroCategory.Druid.getCode());

			/*
			EventManager.getInstance().fireEvent(new DungeonStartEvent(new DungeonSelectedEvent(new StartLevel())));
			*/

			StageSelectionScreen screen = new StageSelectionScreen(this);
			this.setCurrentScreen(screen);

		}
		if(event instanceof QuitGameEvent) {
			this.dispose();
		}
		if(event instanceof ExitUsedEvent) {

			// pause screen rendering
			this.getScreen().pause();

			// change screen to de.jdungeon.skill selection
			//this.dungeonSession.notifyExit(((ExitUsedEvent)de.jdungeon.event).getExit(), ((ExitUsedEvent)de.jdungeon.event).getFigure());
			de.jdungeon.skillselection.SkillSelectionScreen screen = new de.jdungeon.skillselection.SkillSelectionScreen(this);
			this.setCurrentScreen(screen);

			// resume/start rendering of screen
			this.getScreen().resume();

		}
		if(event instanceof SkillSelectedEvent) {
			Spell spell = ((SkillSelectedEvent) event).getSpell();
			dungeonSession.learnSkill(spell);
			StageSelectionScreen screen = new StageSelectionScreen(this);
			this.setCurrentScreen(screen);
		}
		if(event instanceof DungeonStartEvent) {
			Gdx.app.log(TAG, "App: processing DungeonStartEvent");
			// initialize new dungeon
			DungeonFactory dungeonFactory = ((DungeonStartEvent) event).getEvent().getDungeon();

			// create new controller
			PlayerController controller = new PlayerController(this.dungeonSession);

			HeroInfo heroInfo = this.dungeonSession.initDungeon(dungeonFactory, controller);

			getCurrentScreen().pause();

			// create and set new GameScreen
			GameScreen gameScreen = new GameScreen(this, controller, dungeonSession.getCurrentDungeon().getSize());
			setCurrentScreen(gameScreen);

			getCurrentScreen().resume();

			// start world de.jdungeon.game loop
			((DefaultDungeonSession)this.dungeonSession).startGame(controller);
		}
		if(event instanceof PlayerDiedEvent) {
			this.dungeonSession.revertHero();
			StageSelectionScreen screen = new StageSelectionScreen(this);
			this.setCurrentScreen(screen);
		}
	}
}
