package de.jdungeon.androidapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.util.Log;
import android.view.Menu;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.ExitUsedEvent;
import event.PlayerDiedEvent;
import figure.hero.Hero;
import game.DungeonGame;
import level.DungeonStartEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import spell.Spell;
import user.DungeonSession;
import user.DefaultDungeonSession;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.event.QuitGameEvent;
import de.jdungeon.androidapp.event.StartNewGameEvent;
import de.jdungeon.androidapp.gui.dungeonselection.DungeonSelectionScreen;
import de.jdungeon.androidapp.gui.skillselection.SkillSelectedEvent;
import de.jdungeon.androidapp.gui.skillselection.SkillSelectionScreen;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.androidapp.screen.start.HeroCategorySelectedEvent;
import de.jdungeon.androidapp.screen.start.WelcomeScreen;
import de.jdungeon.game.Configuration;
import de.jdungeon.util.DefaultConfiguration;
import de.jdungeon.game.Screen;
import de.jdungeon.implementation.AndroidGame;
import de.jdungeon.user.User;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class JDungeonApp extends AndroidGame implements EventListener {

	private boolean firstTimeCreate = true;
	private final DungeonSession dungeonSession;
	private  AndroidScreenJDGUI gui;
	private  GameScreen gamescreen;

	public JDungeonApp() {
		super(new DefaultDungeonSession(new User("Hans Meiser")));
		this.dungeonSession = (DungeonSession)super.session;
		EventManager.getInstance().registerListener(this);



		LogConfigurator logConfigurator = new LogConfigurator();
		//logConfigurator.setFileName(Environment.getExternalStorageDirectory()
		//		+ File.separator + "MyApp" + File.separator + "logs"
		//		+ File.separator + "log4j.txt");
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.setLevel("org.apache", Level.ERROR);
		//logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
		//logConfigurator.setMaxFileSize(1024 * 1024 * 5);
		logConfigurator.setUseFileAppender(false);//setzt ob das Log in ein File gespeichert werden soll (Ja das geht)
		logConfigurator.setImmediateFlush(true);
		logConfigurator.setUseLogCatAppender(true);
		logConfigurator.configure();
		Logger log = Logger.getLogger(JDungeonApp.class);
		Log.i("Initialization","Dungeon App Created");
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		List<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>();
		events.add(ExitUsedEvent.class);
		events.add(PlayerDiedEvent.class);
		events.add(StartNewGameEvent.class);
		events.add(QuitGameEvent.class);
		events.add(DungeonStartEvent.class);
		events.add(HeroCategorySelectedEvent.class);
		events.add(SkillSelectedEvent.class);
		return events;
	}

	@Override
	protected void onCreateHook() {
		// TODO: solve this weird bidirectional dependency in a better way..
		gui = AndroidScreenJDGUI.getInstance(this);
		gamescreen = new GameScreen(this, gui);
		gui.setPerceptHandler(gamescreen);
		Log.i("Initialization","App on CreateHook done");
	}

	@Override
	public void onConfigurationChanged(android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void notify(Event event) {

		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		if(event instanceof StartNewGameEvent) {
			((DefaultDungeonSession)dungeonSession).setSelectedHeroType(Hero.HeroCategory.Thief.getCode());
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this);
			this.setScreen(screen);
		}
		if(event instanceof QuitGameEvent) {
			this.finish();
		}
		if(event instanceof ExitUsedEvent) {
			DungeonGame.getInstance().stopRunning();
			this.renderView.pause();

			this.dungeonSession.notifyExit(((ExitUsedEvent)event).getExit(), ((ExitUsedEvent)event).getFigure());
			SkillSelectionScreen screen = new SkillSelectionScreen(this);
			this.setScreen(screen);
			this.renderView.resume();

		}
		if(event instanceof SkillSelectedEvent) {
			Spell spell = ((SkillSelectedEvent) event).getSpell();
			dungeonSession.learnSkill(spell);
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this);
			this.setScreen(screen);
		}
		if(event instanceof DungeonStartEvent) {
			Log.i("Initialization","App: processing DungeonStartEvent");
			// initialize new dungeon
			this.dungeonSession.initDungeon(((DungeonStartEvent)event).getEvent().getDungeon());
			DungeonGame.getInstance().restartRunning();
			setScreen(gamescreen);
		}
		if(event instanceof PlayerDiedEvent) {
			this.dungeonSession.revertHero();
			setScreen(new DungeonSelectionScreen(this));
		}
		/*
		if(event instanceof HeroCategorySelectedEvent) {
			((DefaultDungeonSession)dungeonSession).setSelectedHeroType(((HeroCategorySelectedEvent)event).getHeroType());
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this, dungeonSession);
			this.setScreen(screen);
			//setScreen(new GameScreen(this));
		}
		*/
	}

	enum GameState {
		Ready, Running, Paused, GameOver
	}

	private GameState state = GameState.Ready;

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	@Override
	public Configuration getConfiguration() {
		return new DefaultConfiguration();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jdungeon, menu);
		return true;
	}

	@Override
	public Screen getInitScreen() {
		 if (firstTimeCreate ) {
	            Assets.load(this);
	            firstTimeCreate = false;
	        }
		return new WelcomeScreen(this);
	}

}
