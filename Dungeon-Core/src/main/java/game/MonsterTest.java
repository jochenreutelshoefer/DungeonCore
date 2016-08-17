package game;

import figure.monster.Wolf;

public class MonsterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Wolf w =Wolf.buildCustomWolf(70,3,130,1, 3,"Günter");

		System.out.println(w.toString());
	}

}
