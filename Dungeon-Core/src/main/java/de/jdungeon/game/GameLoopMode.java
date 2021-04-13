package de.jdungeon.game;

/**
 * The game can be run using two different strategies considering the threads of the world object updates.
 * This enum represents the configuration flags for selecting the respective strategy.
 */
public enum GameLoopMode {

    /**
     * A distinct thread running to loop round by round all world objects.
     * That is totally separated/independent from UI and rendering.
     */
    DistinctWorldLoopThread,

    /**
     * The world update is integrated into the render loop.
     * Hence the world update call needs to be fast and in particular
     * cannot wait for a UI-User to specify a figures action.
     */
    RenderThreadWorldUpdate;

    public static final String GAME_LOOP_MODE_KEY = "GameLoopMode";

}
