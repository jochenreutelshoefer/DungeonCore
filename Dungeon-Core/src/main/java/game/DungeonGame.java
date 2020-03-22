package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dungeon.Dungeon;
import figure.Figure;
import figure.hero.Hero;
import item.Item;
import item.ItemPool;
import log.Log;
import spell.AbstractSpell;
import spell.TimedSpellInstance;
import test.TestTracker;

/**
 * Die Klasse Game verwaltet den ganzen Spielablauf. Sie verwaltet Dungeon, Held
 * und GUI. Abwechselnd bekommt der Held und dann wieder der Dungeon eine
 * Spielrunde. Sie enthaelt die Methoden, die aus den GUI-Befehlen entsprechende
 * Aktionen (Klasse Action) erstellen und auffuehren.
 */
public class DungeonGame implements Runnable {

	private int round = 0;

	private Dungeon derDungeon;

	private final Map<Figure, JDGUI> guiFigures = new HashMap<Figure, JDGUI>();


	private TestTracker tracker;

	private static DungeonGame instance = null;
	private boolean running = true;

	//TODO: this should certainly NOT be a singleton!

	@Deprecated
	public static DungeonGame getInstance() {
		if (instance == null) {
			instance = new DungeonGame();
		}
		return instance;
	}

	private DungeonGame() {
		tracker = new TestTracker();
	}

	public Dungeon getDungeon() {
		return derDungeon;
	}

	public void setDungeon(Dungeon d) {
		this.derDungeon = d;
	}

	public int getRound() {
		return round;
	}

	private void checkGuiFigures() {
		Collection<Figure> l = guiFigures.keySet();
		List<Figure> toDelete = new LinkedList<Figure>();
		for (Iterator<Figure> iter = l.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			if (element.isDead()) {
				toDelete.add(element);
			}
		}

		for (Iterator<Figure> iter = toDelete.iterator(); iter.hasNext(); ) {
			Object element = iter.next();
			guiFigures.remove(element);
		}
	}

	private void tickGuis(int round) {
		Collection<Figure> l = guiFigures.keySet();

		for (Iterator<Figure> iter = l.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			JDGUI gui = guiFigures.get(element);
			gui.gameRoundEnded();
		}
	}

	public void stopRunning() {
		running = false;
	}

	public void restartRunning() {
		running = true;
	}

	@Override
	public void run() {
		while (true) {
			if (running) {
				checkGuiFigures();
				if (guiFigures.isEmpty()) {
					break;
				}
				Log.info(System.currentTimeMillis() + " " + round + " start round");
				worldTurn(round);
				tickGuis(round);
				Log.info(System.currentTimeMillis() + " " + round + " completed round");
				round++;
			} else {
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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
			turnableItems.add((Turnable) i);
		}
	}

	private void itemsTurn() {
		for (Iterator<Turnable> iter = turnableItems.iterator(); iter.hasNext(); ) {
			Turnable element = iter.next();
			element.turn(round);
		}
	}

	public void worldTurn(int round) {
		//Log.info("Starting world turn for round: "+round);
		derDungeon.turn(round);
		spellsTurn();
		itemsTurn();

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


}