package de.jdungeon.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.item.ItemPool;
import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.spell.TimedSpellInstance;


public class DungeonGameLoop {

	private int round = 0;

	private final Dungeon derDungeon;

	private final Map<Figure, JDGUI> guiFigures = new HashMap<Figure, JDGUI>();

	private boolean running = true;

	private Thread loop;

	public DungeonGameLoop(Dungeon derDungeon) {
		this.derDungeon = derDungeon;
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

	private void spellsTurn() {
		List<TimedSpellInstance> spells = AbstractSpell.timedSpells;
		for (int i = 0; i < spells.size(); i++) {
			((Turnable) spells.get(i)).turn(this.round);
		}
	}

	public void worldTurn(int round) {
		//Log.info("Starting world turn for round: "+round);
		derDungeon.turn(round);
		spellsTurn();
	}

	public void init(Dungeon d) {
		ItemPool.setGame(this);
		this.loop = new Thread(new Loop());
		loop.start();
	}

	public void putGuiFigure(Hero held, JDGUI gui) {
		guiFigures.put(held, gui);
	}

	private class Loop implements Runnable {
		@Override
		public void run() {
			while (running) {
				checkGuiFigures();
				if (guiFigures.isEmpty()) {
					break;
				}
				//Log.info(System.currentTimeMillis() + " " + round + " start round");
				worldTurn(round);
				tickGuis(round);
				//Log.info(System.currentTimeMillis() + " " + round + " completed round");
				round++;
			}

		}
	}
}