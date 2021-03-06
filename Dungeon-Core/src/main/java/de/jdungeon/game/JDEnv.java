package de.jdungeon.game;

import java.io.File;

import de.jdungeon.gui.Texts;
import de.jdungeon.log.Log;
import de.jdungeon.util.MyResourceBundle;

/**
 * Klasse die eine Referenz auf das Game-Objekt und die newStatement()-Methode
 * zum ausgeben von Text im Textfenster zur Verfuegung stellt. Die meisten
 * anderen Klassen erben dies von dieser Klasse.
 */
public class JDEnv {



	protected static MyResourceBundle res;

	/**
	 * Initializes the texts and labels by loading the corresponding resource
	 * bundles
	 */
	public static void init(MyResourceBundle bundle) {
		res = bundle;
		if (res == null) {
			Log.error("Texts ResourceBundle ist null");
		}
		Texts.init();
	}

	public static MyResourceBundle getResourceBundle() {
		return res;
	}


	public static String getString(String key) {
		return res.get(key);
	}

	public static String getLevelFolderRelative(String levelName) {
		String subFolders = "levels/" +levelName;
		return subFolders;
	}
}