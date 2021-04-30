package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public class DungeonResultASP implements DungeonResult{

    private Dungeon dungeon;

    private JDPoint startPosition;
    private String description;

    DungeonResultASP(Dungeon dungeon, JDPoint startPosition, String description) {
        this.dungeon = dungeon;
        this.startPosition = startPosition;
        this.description = description;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public JDPoint getStartPosition() {
        return startPosition;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
