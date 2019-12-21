package level;

import java.util.Locale;
import java.util.ResourceBundle;

import dungeon.Dungeon;
import game.JDEnv;
import level.stagethree.StartLevelOLD;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class LevelTester {

	private  static final int RUNS = 50;

	public static void main(String[] args) {
		JDEnv.init(ResourceBundle.getBundle("texts", Locale.GERMAN));
		StartLevelOLD startLevelFactory = new StartLevelOLD();

		for(int i = 0; i < RUNS; i++) {
			System.out.println("Run "+i);
			Dungeon dungeon = startLevelFactory.createDungeon();

		}

		System.out.println("LevelTester completed");

	}
}
