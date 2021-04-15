package de.jdungeon.game;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.log.Log;

public class DungeonWorldUpdaterRenderLoop implements DungeonWorldUpdater {

    private final Dungeon dungeon;
    private int round = 0;

    public DungeonWorldUpdaterRenderLoop(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void update() {
        boolean roundCompleted = dungeon.turnRenderLoop(round, this);
        if (roundCompleted) {
            round++;
        }
    }

    @Override
    public void stopRunning() {
        // we do not need to do anything in this case, as we do not have a distinct thread
    }

    @Override
    public int getCurrentGameRound() {
        return round;
    }

    @Override
    public GameLoopMode getGameLoopMode() {
        return GameLoopMode.RenderThreadWorldUpdate;
    }

    @Override
    public void waitSomeTimeOnGuiAction(int milliseconds) {
        // will not be called in this mode
        Log.severe("Should never happen in this mode: "+getGameLoopMode());
    }


}
