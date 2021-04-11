package de.jdungeon.libgdx;

import com.badlogic.gdx.Gdx;
import de.jdungeon.game.Logger;

public class LibgdxLogger implements Logger {

    @Override
    public void info(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable e) {
        Gdx.app.log(tag, message,e);
    }

    @Override
    public void info(String message) {
        Gdx.app.log("", message);
    }

    @Override
    public void info(String message, Throwable e) {
        Gdx.app.log("", message, e);
    }

    @Override
    public void warning(String tag, String message) {
        Gdx.app.log(tag, message);
    }

    @Override
    public void warning(String tag, String message, Throwable e) {
        Gdx.app.log(tag, message, e);
    }


    @Override
    public void warning(String message) {
        Gdx.app.log("WARNING", message);
    }

    @Override
    public void warning(String message, Throwable e) {
        Gdx.app.log("WARNING", message, e);
    }

    @Override
    public void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable e) {
        Gdx.app.error(tag, message, e);
    }


    @Override
    public void error(String message) {
        Gdx.app.error("ERROR", message);
    }

    @Override
    public void error(String message, Throwable e) {
        Gdx.app.error("ERROR", message, e);
    }

}
