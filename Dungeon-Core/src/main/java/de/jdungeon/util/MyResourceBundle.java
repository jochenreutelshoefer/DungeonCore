package de.jdungeon.util;

import java.util.Map;

public interface MyResourceBundle {

    public static final String TEXTS_BUNDLE_BASENAME = "texts";

    String get(String key);

    @Deprecated
    default String getString(String key) {
        return get(key);
    }

    String format(String key, String... inserts);
}
