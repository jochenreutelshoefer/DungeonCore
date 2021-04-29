package de.jdungeon.game;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.figure.hero.Hero;

public interface DungeonWorldUpdaterInitializer {

    DungeonWorldUpdater initializeWorldUpdate(Dungeon dungeon, Hero hero, ControlUnit gui);

    GameLoopMode getMode();
}
