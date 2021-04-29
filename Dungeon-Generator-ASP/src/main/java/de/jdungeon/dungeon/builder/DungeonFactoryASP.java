package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.level.DungeonFactory;

public class DungeonFactoryASP implements DungeonFactory {

    private DungeonResultASP dungeonResult;

    public DungeonFactoryASP(DungeonResultASP dungeonResult) {
        this.dungeonResult = dungeonResult;
    }

    @Override
    public Dungeon createDungeon() {
        return dungeonResult.getDungeon();
    }

    @Override
    public JDPoint getHeroEntryPoint() {
        return dungeonResult.getStartPosition();
    }

    @Override
    public String icon() {
        return null;
    }

    @Override
    public String getName() {
        return dungeonResult.getDescription();
    }

    @Override
    public String getDescription() {
        return dungeonResult.getDescription();
    }

    @Override
    public int getRoundScoringBaseValue() {
        // TODO !
        return 0;
    }
}
