package de.jdungeon.asset;

import de.jdungeon.util.MyResourceBundle;

public class LibgdxTextResourceBundle implements MyResourceBundle {

    @Override
    public String get(String key) {
        return Assets.instance.getTextBundle().get(key);
    }

    @Override
    public String format(String key, String... inserts) {
        return Assets.instance.getTextBundle().format(key, inserts);
    }
}
