package de.jdungeon.game;

import java.util.Map;

public class MyResourceBundle {

    private Map<String, String> texts;

    public MyResourceBundle(Map<String, String> texts) {
        this.texts = texts;
    }

    public String getString(String key) {
        return texts.get(key);
    }
}
