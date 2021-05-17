package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;

public class DungeonResultASP implements DungeonResult {

    private LevelDTO dungeon;

    private JDPoint startPosition;
    private String description;

    DungeonResultASP(LevelDTO dungeon, JDPoint startPosition, String description) {
        this.dungeon = dungeon;
        this.startPosition = startPosition;
        this.description = description;
    }

    public LevelDTO getDungeonDTO() {
        return dungeon;
    }

    public Dungeon getDungeon() {
        return new DTODungeonFactory(dungeon).create();
    }

    public JDPoint getStartPosition() {
        return startPosition;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
