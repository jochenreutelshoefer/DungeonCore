package de.jdungeon.test;

import android.content.Context;
import android.content.res.AssetManager;
import de.jdungeon.io.ResourceBundleLoader;

import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class AndroidResourceBundleLoader implements ResourceBundleLoader {

	private static final String TAG = AndroidResourceBundleLoader.class.getSimpleName();

	private final String prefix;
	private final AssetManager manager;

	public AndroidResourceBundleLoader(Context context, String prefix) {
		manager = context.getAssets();
		this.prefix = prefix;
	}

	@Override
	public ResourceBundle getBundle(String path) {
		try {
			String fullPath = prefix + path.replace(".", "/") + ".properties";
			return new PropertyResourceBundle(manager.open(fullPath));
		} catch (IOException e) {
			android.util.Log.e(TAG, "Failed to load " + path, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResourceBundle getBundle(String path, Locale locale, ClassLoader classLoader) {
		return getBundle(path + "_" + locale.getLanguage());
	}
}
