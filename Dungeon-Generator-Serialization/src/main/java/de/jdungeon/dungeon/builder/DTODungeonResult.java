package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;

public class DTODungeonResult implements DungeonResult {

    private LevelDTO dungeon;

    private String description;

    DTODungeonResult(LevelDTO dungeon, String description) {
        this.dungeon = dungeon;
        this.description = description;
    }

    public LevelDTO getDungeonDTO() {
        return dungeon;
    }

    public Dungeon getDungeon() {
        return new DTODungeonFactory(dungeon).create();
    }

    public JDPoint getStartPosition() {
        return dungeon.getStartPosition();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
