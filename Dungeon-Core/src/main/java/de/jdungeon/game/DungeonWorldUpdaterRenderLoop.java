package de.jdungeon.game;

import de.jdungeon.dungeon.Dungeon;

public class DungeonWorldUpdaterRenderLoop implements DungeonWorldUpdater {

    private final Dungeon dungeon;
    private int round = 0;

    public DungeonWorldUpdaterRenderLoop(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void update() {
        boolean roundCompleted = dungeon.turnRenderLoop(round);
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


}
