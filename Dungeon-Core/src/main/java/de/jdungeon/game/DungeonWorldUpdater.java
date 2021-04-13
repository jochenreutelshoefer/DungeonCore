package de.jdungeon.game;

import de.jdungeon.dungeon.Dungeon;

public class DungeonWorldUpdater {

    private final Dungeon dungeon;
    private int round = 0;

    public DungeonWorldUpdater(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public void update() {
        //dungeon.turn(round, GameLoopMode.RenderThreadWorldUpdate);
        boolean roundCompleted = dungeon.turnRenderLoop(round);
        if (roundCompleted) {
            round++;
        }
    }
}
