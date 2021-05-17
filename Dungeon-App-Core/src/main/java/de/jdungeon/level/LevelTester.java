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
		JDEnv.init(new MyResourceBundle() {
			@Override
			public String get(String key) {
				return null;
			}

			@Override
			public String format(String key, String... inserts) {
				return null;
			}
		});
		StartLevelOLD startLevelFactory = new StartLevelOLD();

		for(int i = 0; i < RUNS; i++) {
			System.out.println("Run "+i);
			startLevelFactory.create();
			Dungeon dungeon = startLevelFactory.getDungeon();
			dungeon.prepare();

		}

		System.out.println("LevelTester completed");

	}
}
