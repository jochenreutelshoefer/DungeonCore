package com.mygdx.game.client;

import de.jdungeon.asset.LibgdxTextResourceBundle;
import de.jdungeon.io.ResourceBundleLoader;
import de.jdungeon.util.MyResourceBundle;

import java.util.Locale;

public class GwtResourceBundleLoader implements ResourceBundleLoader {
    @Override
    public MyResourceBundle getBundle(String path) {
        return new LibgdxTextResourceBundle();
    }

    @Override
    public MyResourceBundle getBundle(String baseName, Locale locale, Object game) {
        return getBundle(baseName);
    }
}
