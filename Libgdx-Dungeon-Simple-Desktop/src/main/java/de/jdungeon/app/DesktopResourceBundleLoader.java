package de.jdungeon.app;

import de.jdungeon.asset.LibgdxTextResourceBundle;
import de.jdungeon.util.MyResourceBundle;
import de.jdungeon.io.ResourceBundleLoader;

import java.util.*;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.20.
 */
public class DesktopResourceBundleLoader implements ResourceBundleLoader {
    @Override
    public MyResourceBundle getBundle(String path) {
        return convertBundle(ResourceBundle.getBundle(path));
    }

    private MyResourceBundle convertBundle(ResourceBundle bundle) {
        return new LibgdxTextResourceBundle();
    }

    public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
        return ResourceBundle.getBundle(baseName, locale, loader, new UTF8Control());
    }

    @Override
    public MyResourceBundle getBundle(String baseName, Locale locale, Object game) {
        return convertBundle(getBundle(baseName, locale, game.getClass().getClassLoader()));
    }
}
