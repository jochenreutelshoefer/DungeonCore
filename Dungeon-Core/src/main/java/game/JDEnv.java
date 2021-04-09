package game;

import java.util.ResourceBundle;

import gui.Texts;
import log.Log;

/**
 * Klasse die eine Referenz auf das Game-Objekt und die newStatement()-Methode
 * zum ausgeben von Text im Textfenster zur Verfuegung stellt. Die meisten
 * anderen Klassen erben dies von dieser Klasse.
 */
public class JDEnv {

	protected static ResourceBundle res;


	protected static boolean beginnerGame = false;

	public static final String TEXTS_BUNDLE_BASENAME = "texts";


	/**
	 * Initializes the texts and labels by loading the corresponding resource
	 * bundles
	 */
	public static void init(ResourceBundle bundle) {
		res = bundle;
		if (res == null) {
			Log.error("Texts ResourceBundle ist null");
		}
		Texts.init();
	}

	public static ResourceBundle getResourceBundle() {
		return res;
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

/*
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
		}
		else {
			stream = loader.getResourceAsStream(resourceName);
		}
		if (stream != null) {
			try {
				// Only this line is changed to make it to read properties files
				// as UTF-8.
				bundle = new PropertyResourceBundle(new InputStreamReader(
						stream, "UTF-8"));
			}
			finally {
				stream.close();
			}
		}
		return bundle;
	}

}
*/