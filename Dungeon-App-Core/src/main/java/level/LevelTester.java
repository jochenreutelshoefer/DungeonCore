package level;

import dungeon.Dungeon;
import game.JDEnv;
import level.stageone.StartLevelOLD;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class LevelTester {

	private  static final int RUNS = 50;

	public static void main(String[] args) {
		JDEnv.init();
		StartLevelOLD startLevelFactory = new StartLevelOLD();

		for(int i = 0; i < RUNS; i++) {
			System.out.println("Run "+i);
			Dungeon dungeon = startLevelFactory.createDungeon();

		}

		System.out.println("LevelTester completed");

	}
}
