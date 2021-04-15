package de.jdungeon.game;

public interface DungeonWorldUpdater {

    void update();

    void stopRunning();

    int getCurrentGameRound();

    GameLoopMode getGameLoopMode();

    void waitSomeTimeOnGuiAction(int milliseconds);

}
