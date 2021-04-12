package de.jdungeon.app;

import de.jdungeon.game.MyResourceBundle;
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
        Map<String, String> entries = new HashMap<>();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            entries.put(key, bundle.getString(key));
        }
        return new MyResourceBundle(entries);
    }

    public ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
        return ResourceBundle.getBundle(baseName, locale, loader, new UTF8Control());
        //return ResourceBundle.getBundle(baseName, locale, loader);
    }

    @Override
    public MyResourceBundle getBundle(String baseName, Locale locale, Object game) {
        return convertBundle(getBundle(baseName, locale, game.getClass().getClassLoader()));
    }
}
