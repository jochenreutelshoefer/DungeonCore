package de.jdungeon.io;

import de.jdungeon.util.MyResourceBundle;

import java.util.Locale;

/**
 * Loader API
 */
public interface ResourceBundleLoader {

	MyResourceBundle getBundle(String path);
	MyResourceBundle getBundle(String baseName, Locale locale, Object game);
}
