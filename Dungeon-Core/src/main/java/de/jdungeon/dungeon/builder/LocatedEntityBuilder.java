package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public interface LocatedEntityBuilder {

	/**
	 * Return a unique (for the current level) identifier for this entity.
	 *
	 * @return identifier
	 */
	String getIdentifier();

	/**
	 * Says whether this LocatedEntity has a fixed position predefined before the dungeon generation process.
	 * That is, the position will be an input to the dungeon generation process.
	 *
	 * @return
	 */
	boolean hasFixedRoomPosition();

	/**
	 * Returns the final position of this LocatedEntity.
	 *
	 * @return final position
	 */
	JDPoint getRoomPosition();

	/**
	 * Actually inserts the location into the newly created Dungeon
	 *
	 * @param dungeon dungeon to insert the location into
	 * @param x       x coordinate of the room where the location is to be inserted
	 * @param y       y coordinate of the room where the location is to be inserted
	 */
	void insert(Dungeon dungeon, int x, int y);
}
