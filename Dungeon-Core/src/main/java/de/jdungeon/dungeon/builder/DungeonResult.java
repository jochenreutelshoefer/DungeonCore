package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public interface DungeonResult {

    Dungeon getDungeon();

    JDPoint getStartPosition();

    String getDescription();
}
