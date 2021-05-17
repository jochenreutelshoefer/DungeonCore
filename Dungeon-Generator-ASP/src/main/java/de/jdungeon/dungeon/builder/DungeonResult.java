package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;

public interface DungeonResult {

    Dungeon getDungeon();

    LevelDTO getDungeonDTO();

    JDPoint getStartPosition();

    String getDescription();
}
