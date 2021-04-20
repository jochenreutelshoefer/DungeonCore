package com.mygdx.game.client;

import de.jdungeon.util.UUIDGenerator;

public class UUIDGeneratorJavascript implements UUIDGenerator {


    public native static String uuid() /*-{
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
            function(c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r
                        : (r & 0x3 | 0x8);
                return v.toString(16);
            });
}-*/;

    @Override
    public String generateUUID() {
        return uuid();
    }
}
