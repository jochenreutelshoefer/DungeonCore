package de.jdungeon.game;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.figure.hero.Hero;

public class RenderLoopWorldUpdateInitializer implements DungeonWorldUpdaterInitializer {
    @Override
    public DungeonWorldUpdater initializeWorldUpdate(Dungeon dungeon, Hero hero, ControlUnit gui) {
        return new DungeonWorldUpdaterRenderLoop(dungeon);
    }

    @Override
    public GameLoopMode getMode() {
        return GameLoopMode.RenderThreadWorldUpdate;
    }
}
