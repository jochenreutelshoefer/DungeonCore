package game;

import gui.Texts;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * Klasse die eine Referenz auf das Game-Objekt und die newStatement()-Methode
 * zum ausgeben von Text im Textfenster zur Verfuegung stellt. Die meisten
 * anderen Klassen erben dies von dieser Klasse.
 * 
 */
public class JDEnv {

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

	/**
	 * Initializes the texts and labels by loading the corresponding resource
	 * bundles
	 */
	public static void init() {
		Locale loc_de = Locale.GERMAN;
		Locale loc_en = Locale.ENGLISH;
		UTF8Control utf8Control = new UTF8Control();
		String baseName = "texts";
		try {
			if (english) {
				res = utf8Control.newBundle(baseName, loc_en, null,
						JDEnv.class.getClassLoader(), false);
			} else {
				res = utf8Control.newBundle(baseName, loc_de, null,
						JDEnv.class.getClassLoader(), false);
			}

		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public static boolean visCheat = false;

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

class UTF8Control extends Control {
	@Override
	public ResourceBundle newBundle(String baseName, Locale locale,
			String format, ClassLoader loader, boolean reload)
			throws IllegalAccessException, InstantiationException, IOException {
		// The below is a copy of the default implementation.
		String bundleName = toBundleName(baseName, locale);
		String resourceName = toResourceName(bundleName, "properties");
		ResourceBundle bundle = null;
		InputStream stream = null;
		if (reload) {
			URL url = loader.getResource(resourceName);
			if (url != null) {
				URLConnection connection = url.openConnection();
				if (connection != null) {
					connection.setUseCaches(false);
					stream = connection.getInputStream();
				}
			}
		} else {
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null) {
			try {
				// Only this line is changed to make it to read properties files
				// as UTF-8.
				bundle = new PropertyResourceBundle(new InputStreamReader(
						stream, "UTF-8"));
			} finally {
				stream.close();
			}
		}
		return bundle;
	}
}