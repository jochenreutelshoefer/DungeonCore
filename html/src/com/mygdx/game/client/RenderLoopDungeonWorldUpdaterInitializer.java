package com.mygdx.game.client;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.DungeonWorldUpdaterInitializer;
import de.jdungeon.game.DungeonWorldUpdaterRenderLoop;

public class RenderLoopDungeonWorldUpdaterInitializer implements DungeonWorldUpdaterInitializer {
    @Override
    public DungeonWorldUpdater initializeWorldUpdate(Dungeon d) {
        return  new DungeonWorldUpdaterRenderLoop(d);
    }
}
