package de.jdungeonx;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;

public class DungeonGameLoop {

	private int round = 0;

	private final Dungeon derDungeon;
	private DungeonWorldUpdater delegateUpdater;

	private final Map<Figure, ControlUnit> guiFigures = new HashMap<>();

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

	public void putGuiFigure(Hero held, ControlUnit gui) {
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
				//Log.info(System.currentTimeMillis() + " " + round + " completed round");
				round++;
			}
		}
	}
}