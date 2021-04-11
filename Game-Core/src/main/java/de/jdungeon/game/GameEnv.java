package de.jdungeon.game;

public class GameEnv {

    private static GameEnv instance;

    public static GameEnv getInstance() {
        if(instance == null) {
            instance = new GameEnv();
        }
        return instance;
    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private Game game;

}
