package de.jdungeon.game;

public interface Image {

    int getWidth();
    int getHeight();
    Graphics.ImageFormat getFormat();
    void dispose();
}
