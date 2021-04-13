package de.jdungeon.game;

public enum GameLoopMode {


    DistinctWorldLoopThread,
    RenderThreadWorldUpdate;

    public static final String GAME_LOOP_MODE_KEY = "GameLoopMode";

}
