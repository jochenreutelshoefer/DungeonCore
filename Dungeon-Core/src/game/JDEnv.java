package game;

import java.util.ResourceBundle;

/**
 * Klasse die eine Referenz auf das Game-Objekt und die newStatement()-Methode
 * zum ausgeben von Text im Textfenster zur Verfuegung stellt. Die meisten anderen
 * Klassen erben dies von dieser Klasse.
 * 
 */
public class JDEnv {

	protected static Game game;
	
	protected static ResourceBundle res;
	
	protected static boolean beginnerGame = false;
	
	public static double BEGINNER_RATE = 0.65;
	
	public static ResourceBundle getResourceBundle() {
		return res;
	}

	public static void setGame(Game g) {
		game = g;
		System.gc();
	}
	
	public static boolean visCheat = false;
	
	public static void unsetGame() {
		game = null;
	}

	public static void setRes(ResourceBundle res) {
		JDEnv.res = res;
	}
	
	public static String getString(String key) {
		return res.getString(key);
	}

	public static boolean isBeginnerGame() {
		return beginnerGame;
	}

	public static void setBeginnerGame(boolean beginnerGame) {
		JDEnv.beginnerGame = beginnerGame;
	}


}