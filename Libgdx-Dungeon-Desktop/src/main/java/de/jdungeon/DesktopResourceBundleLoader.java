package de.jdungeon;

import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import de.jdungeon.io.ResourceBundleLoader;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.20.
 */
public class DesktopResourceBundleLoader implements ResourceBundleLoader {
	@Override
	public ResourceBundle getBundle(String path) {
		return ResourceBundle.getBundle(path);
	}

	@Override
	public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
		return ResourceBundle.getBundle(baseName, locale, loader);
	}
}
