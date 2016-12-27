package de.jdungeon.androidapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.view.Menu;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.ExitUsedEvent;
import event.GameOverEvent;
import level.DungeonSelectedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import user.DungeonSession;
import user.DefaultDungeonSession;

import de.jdungeon.androidapp.gui.dungeonselection.DungeonSelectionScreen;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.androidapp.screen.start.HeroCategorySelectedEvent;
import de.jdungeon.androidapp.screen.start.HeroSelectionScreen;
import de.jdungeon.game.Screen;
import de.jdungeon.implementation.AndroidGame;
import de.jdungeon.user.User;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class JDungeonApp extends AndroidGame implements EventListener {

	private boolean firstTimeCreate = true;
	private final DungeonSession dungeonSession;

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
		log.info("My Application Created");
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		List<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>();
		events.add(ExitUsedEvent.class);
		events.add(GameOverEvent.class);
		events.add(DungeonSelectedEvent.class);
		events.add(HeroCategorySelectedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if(event instanceof ExitUsedEvent) {
			this.dungeonSession.notifyExit(((ExitUsedEvent)event).getExit(), ((ExitUsedEvent)event).getFigure());
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this, dungeonSession);
			this.setScreen(screen);
		}
		if(event instanceof DungeonSelectedEvent) {
			this.dungeonSession.initDungeon(((DungeonSelectedEvent)event).getDungeon());
			setScreen(new GameScreen(this));
		}
		if(event instanceof GameOverEvent) {
			// TODO: implement
		}
		if(event instanceof HeroCategorySelectedEvent) {
			((DefaultDungeonSession)dungeonSession).setSelectedHeroType(((HeroCategorySelectedEvent)event).getHeroType());
			DungeonSelectionScreen screen = new DungeonSelectionScreen(this, dungeonSession);
			this.setScreen(screen);
			//setScreen(new GameScreen(this));
		}
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
		return new HeroSelectionScreen(this);
	}

}
