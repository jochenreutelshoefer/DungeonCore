package de.jdungeon.io;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Loader API
 */
public interface ResourceBundleLoader {
	ResourceBundle getBundle(String path);
	ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader);
}
