package de.jdungeon;

import java.util.*;

import de.jdungeon.asset.LibgdxTextResourceBundle;
import de.jdungeon.io.ResourceBundleLoader;
import de.jdungeon.util.MyResourceBundle;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.20.
 */
public class DesktopResourceBundleLoader implements ResourceBundleLoader {
    @Override
    public MyResourceBundle getBundle(String path) {
        return new LibgdxTextResourceBundle();
    }


    @Override
    public MyResourceBundle getBundle(String baseName, Locale locale, Object game) {
        return new LibgdxTextResourceBundle();
    }
}
