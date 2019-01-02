package game;

import dungeon.util.InfoUnitUnwrapper;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.ExitUsedEvent;
import event.PlayerDiedEvent;
import item.Item;
import item.ItemPool;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import spell.AbstractSpell;
import spell.TimedSpellInstance;
import test.TestTracker;
import dungeon.Dungeon;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;


/**
 * Die Klasse Game verwaltet den ganzen Spielablauf. Sie verwaltet Dungeon, Held
 * und GUI. Abwechselnd bekommt der Held und dann wieder der Dungeon eine
 * Spielrunde. Sie enthaelt die Methoden, die aus den GUI-Befehlen entsprechende
 * Aktionen (Klasse Action) erstellen und auffuehren.
 * 
 */
public class DungeonGame implements Runnable {

	private int round = 1;

	private boolean gameOver = false;
	private boolean heroLeft = false;

	private Dungeon derDungeon;

	private final Map<Figure, JDGUI> guiFigures = new HashMap<Figure, JDGUI>();

	private final long startTime;

	private TestTracker tracker;

	private static DungeonGame instance = null;

	//TODO: this should certainly NOT be a singleton!

	@Deprecated
	public static DungeonGame getInstance() {
		if(instance == null) {
			instance = new DungeonGame();
		}
		return instance;
	}


	private DungeonGame() {
		startTime = System.currentTimeMillis();
		tracker = new TestTracker();
	}

	public Dungeon getDungeon() {
		return derDungeon;
	}

	public int getRound() {
		return round;
	}


	private void checkGuiFigures() {
		Collection<Figure> l = guiFigures.keySet();
		List<Figure> toDelete = new LinkedList<Figure>();
		for (Iterator<Figure> iter = l.iterator(); iter.hasNext();) {
			Figure element = iter.next();
			if (element.isDead()) {
				toDelete.add(element);
			}

		}

		for (Iterator<Figure> iter = toDelete.iterator(); iter.hasNext();) {
			Object element = iter.next();
			guiFigures.remove(element);
		}
	}

	private void tickGuis() {
		Collection<Figure> l = guiFigures.keySet();

		for (Iterator<Figure> iter = l.iterator(); iter.hasNext();) {
			Figure element = iter.next();
			JDGUI gui = guiFigures.get(element);
			gui.gameRoundEnded();

		}
	}

	@Override
	public void run() {
		while (!gameOver && !heroLeft) {
			checkGuiFigures();
			if (guiFigures.isEmpty()) {
				break;
			}
			worldTurn();
			tickGuis();
		}
	}

	private void spellsTurn() {
		List<TimedSpellInstance> spells = AbstractSpell.timedSpells;
		for (int i = 0; i < spells.size(); i++) {
			((Turnable) spells.get(i)).turn(this.round);
		}
	}

	private final List<Turnable> turnableItems = new ArrayList<Turnable>();

	public void addTurnableItem(Item i) {
		if (i instanceof Turnable) {
			turnableItems.add((Turnable)i);
		}
	}

	private void itemsTurn() {
		for (Iterator<Turnable> iter = turnableItems.iterator(); iter.hasNext();) {
			Turnable element = iter.next();
			element.turn(round);

		}
	}

	public void worldTurn() {
		derDungeon.turn(round);
		spellsTurn();
		itemsTurn();
		round++;
	}



	public void init(Dungeon d) {
		this.round = 0;
		ItemPool.setGame(this);
		Figure.createVisibilityMaps(derDungeon);
		Figure.setMonsterControls();
	}


	public void setTestTracker(TestTracker tracker) {
		this.tracker = tracker;
	}


	public void putGuiFigure(Hero held, JDGUI gui) {
		guiFigures.put(held, gui);

	}

	public void setDungeon(Dungeon d) {
		this.derDungeon = d;

	}

}