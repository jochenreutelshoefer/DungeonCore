package de.jdungeon.game;

import android.app.Activity;

import de.jdungeon.implementation.AndroidGame;

public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void paint(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

	public abstract void init();
   
    public abstract void backButton();


}
 