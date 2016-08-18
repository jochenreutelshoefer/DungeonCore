package item;

//Wann und wieviel gesetzt werden soll - Bei Tränken und Elixieren

public class PotionMod {

	private int value;

	private int round;

	public PotionMod(int v, int r) {
		value = v;
		round = r;
	}

	public int getValue() {
		return value;
	}

	public int getRound() {
		return round;
	}

}
