package de.jdungeon.dungeon.builder;

/**
 * Allows to create a DungeonBuilder
 */
public interface DungeonBuilderFactory {

	/**
	 * Creates a new DungeonBuilder
	 *
	 * @return new DungeonBuilder
	 */
	DungeonBuilder create();

	/**
	 * Returns the dungeon generation Mode employed by this DungeonBuilders created
	 * by this factory.
	 *
	 * @return dungeon generation mode
	 */
	DungeonFactory.Mode getMode();

}
