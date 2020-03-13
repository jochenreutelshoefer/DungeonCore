package de.jdungeon;

import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.ExitUsedEvent;
import event.PlayerDiedEvent;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import game.DungeonGame;
import game.JDEnv;
import level.DungeonFactory;
import level.DungeonSelectedEvent;
import level.DungeonStartEvent;
import level.stageone.StartLevel;
import log.Log;
import spell.Spell;
import user.DefaultDungeonSession;
import user.DungeonSession;

import de.jdungeon.app.SessionEvents;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.QuitGameEvent;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.app.gui.dungeonselection.DungeonSelectionScreen;
import de.jdungeon.app.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.app.gui.skillselection.SkillSelectionScreen;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.FileIO;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.Screen;
import de.jdungeon.game.ScreenContext;
import de.jdungeon.io.ResourceBundleLoader;
import de.jdungeon.stage.StageSelectionScreen;
import de.jdungeon.user.Session;
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

	public LibgdxDungeonMain(ResourceBundleLoader resourceBundleLoader) {
		this.resourceBundleLoader = resourceBundleLoader;
	}

	@Override
	public void create() {

		Gdx.app.log(TAG,"Creating application");

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		ResourceBundle textsBundle = resourceBundleLoader.getBundle(JDEnv.TEXTS_BUNDLE_BASENAME, Locale.GERMAN, this.getClass().getClassLoader());
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
	public Session getSession() {
		return (DefaultDungeonSession)dungeonSession;
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
			((DefaultDungeonSession)dungeonSession).setSelectedHeroType(Hero.HeroCategory.Warrior.getCode());

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
			DungeonGame.getInstance().stopRunning();

			// pause screen rendering
			this.getScreen().pause();

			// change screen to skill selection
			this.dungeonSession.notifyExit(((ExitUsedEvent)event).getExit(), ((ExitUsedEvent)event).getFigure());
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

			PlayerController controller = new PlayerController();
			// create new controller


			HeroInfo heroInfo = this.dungeonSession.initDungeon(dungeonFactory, controller);

			getCurrentScreen().pause();

			// create and set new GameScreen
			GameScreen gameScreen = new GameScreen(this, controller, DungeonGame.getInstance().getDungeon().getSize());
			setCurrentScreen(gameScreen);

			getCurrentScreen().resume();

			// start world game loop
			((DefaultDungeonSession)this.dungeonSession).startGame(controller);
			DungeonGame.getInstance().restartRunning();
		}
		if(event instanceof PlayerDiedEvent) {
			this.dungeonSession.revertHero();
			StageSelectionScreen screen = new StageSelectionScreen(this);
			this.setCurrentScreen(screen);
		}
	}
}
