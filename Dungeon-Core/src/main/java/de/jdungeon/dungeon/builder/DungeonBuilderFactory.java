package de.jdungeon.dungeon.builder;

/**
 * A factory that allows to instantiate DungeonBuilders
 */
@FunctionalInterface
public interface DungeonBuilderFactory {

    /**
     * Creates a new DungeonBuilder.
     *
     * @return new DungeonBuilder
     */
    DungeonBuilder create();
}
