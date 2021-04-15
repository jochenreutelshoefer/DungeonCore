package de.jdungeonx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDGUI;
import de.jdungeon.item.ItemPool;


public class DungeonGameLoop {

	private int round = 0;

	private final Dungeon derDungeon;
	private DungeonWorldUpdater delegateUpdater;

	private final Map<Figure, JDGUI> guiFigures = new HashMap<>();

	private boolean running = true;

	private Thread loop;

	public DungeonGameLoop(Dungeon derDungeon, DungeonWorldUpdater updater) {
		this.derDungeon = derDungeon;
		this.delegateUpdater = updater;
	}

	public int getRound() {
		return round;
	}

	private void checkGuiFigures() {
		Collection<Figure> l = guiFigures.keySet();
		List<Figure> toDelete = new LinkedList<Figure>();
		for (Figure element : l) {
			if (element.isDead()) {
				toDelete.add(element);
			}
		}

		for (Object element : toDelete) {
			guiFigures.remove(element);
		}
	}

	private void tickGuis(int round) {
		Collection<Figure> l = guiFigures.keySet();

		for (Figure element : l) {
			JDGUI gui = guiFigures.get(element);
			gui.gameRoundEnded();
		}
	}

	public void stopRunning() {
		running = false;
	}

	public void worldTurn(int round) {
		derDungeon.turn(round, delegateUpdater);
	}

	public void init() {
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
					// our stop criteria
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