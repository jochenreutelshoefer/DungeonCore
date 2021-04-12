package de.jdungeon.level;

import java.util.Collections;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.game.JDEnv;
import de.jdungeon.util.MyResourceBundle;
import de.jdungeon.level.stagethree.StartLevelOLD;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class LevelTester {

	private  static final int RUNS = 50;

	public static void main(String[] args) {
		JDEnv.init(new MyResourceBundle(Collections.emptyMap()));
		StartLevelOLD startLevelFactory = new StartLevelOLD();

		for(int i = 0; i < RUNS; i++) {
			System.out.println("Run "+i);
			Dungeon dungeon = startLevelFactory.createDungeon();
			dungeon.prepare();

		}

		System.out.println("LevelTester completed");

	}
}
