package de.jdungeon.game;

public interface Logger {

    void info(String tag, String message);

    void info(String tag, String message, Throwable e);

    void info(String message);

    void info(String message, Throwable e);

    void warning(String tag, String message);

    void warning(String tag, String message, Throwable e);

    void warning(String message);

    void warning(String message, Throwable e);

    void error(String tag, String message);

    void error(String tag, String message, Throwable e);

    void error(String message);

    void error(String message, Throwable e);
}
