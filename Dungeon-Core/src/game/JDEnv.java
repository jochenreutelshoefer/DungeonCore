package game;

import gui.Texts;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Klasse die eine Referenz auf das Game-Objekt und die newStatement()-Methode
 * zum ausgeben von Text im Textfenster zur Verfuegung stellt. Die meisten anderen
 * Klassen erben dies von dieser Klasse.
 * 
 */
public class JDEnv {

	public static DungeonGame game;
	
	protected static ResourceBundle res;
	
	protected static boolean beginnerGame = false;
	
	public static double BEGINNER_RATE = 0.65;
	
	private static boolean english = false;
	
	public static boolean isEnglish() {
		return english;
	}

	public static void setEnglish(boolean english) {
		JDEnv.english = english;
	}

	public static void init() {
		Locale loc_de = Locale.GERMAN;
		Locale loc_en = Locale.ENGLISH;

		if (english) {
			res = ResourceBundle.getBundle("texts", loc_en);
		} else {
			res = ResourceBundle.getBundle("texts", loc_de);

		}

		if (res == null) {
			System.out.println("Bundle ist null");
			System.exit(0);
		}
		Texts.init();

	}
	
	public static ResourceBundle getResourceBundle() {
		return res;
	}

	public static void setGame(DungeonGame g) {
		game = g;
		System.gc();
	}
	
	public static boolean visCheat = true;
	
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