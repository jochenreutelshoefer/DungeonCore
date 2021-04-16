package de.jdungeon.example;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.DungeonWorldUpdaterInitializer;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDGUI;

public class DistinctWorldUpdateThreadInitializer implements DungeonWorldUpdaterInitializer{

    @Override
    public DungeonWorldUpdater initializeWorldUpdate(Dungeon dungeon, Hero hero, JDGUI gui) {
        return new DistinctUpdateThreadWorldUpdater(dungeon, hero, gui);
    }

    @Override
    public GameLoopMode getMode() {
        return GameLoopMode.DistinctWorldLoopThread;
    }
}
