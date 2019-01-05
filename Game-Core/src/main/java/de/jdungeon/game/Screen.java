package de.jdungeon.game;

public abstract class Screen {

    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
    public abstract void update(float deltaTime);

    public abstract void paint(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

	public abstract void init();
   
    public abstract void backButton();


}
 