package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public class DungeonResultASP implements DungeonResult{

    private Dungeon dungeon;

    private JDPoint startPosition;
    private DungeonConfiguration dungeonConfiguration;

    DungeonResultASP(Dungeon dungeon, JDPoint startPosition, DungeonConfiguration dungeonConfiguration) {
        this.dungeon = dungeon;
        this.startPosition = startPosition;
        this.dungeonConfiguration = dungeonConfiguration;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public JDPoint getStartPosition() {
        return startPosition;
    }

    @Override
    public String getDescription() {
        return dungeonConfiguration.toString();
    }
}
