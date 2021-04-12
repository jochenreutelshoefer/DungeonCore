package de.jdungeon.util;

import java.util.Map;

public class MyResourceBundle {

    public static final String TEXTS_BUNDLE_BASENAME = "texts";


    private Map<String, String> texts;

    public MyResourceBundle(Map<String, String> texts) {
        this.texts = texts;
    }

    public String getString(String key) {
        return texts.get(key);
    }
}
