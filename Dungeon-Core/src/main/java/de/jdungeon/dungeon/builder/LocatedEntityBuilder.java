package de.jdungeon.dungeon.builder;

import java.util.Set;

import com.badlogic.gdx.utils.Json;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public interface LocatedEntityBuilder extends Json.Serializable {

	/**
	 * Return a unique (for the current level) identifier for this entity.
	 *
	 * @return identifier
	 */
	String getIdentifier();

	/**
	 * Returns the order priority of building/instantiation
	 *
	 * @return priority value
	 */
	int getBuildPriority();

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
	 * Returns a set of possible positions. This is only relevant if the location does not have a fixed position.
	 * If the returned set is null or empty, then an arbitrary position is choosen by the generator.
	 *
	 * @return set of possible positions for this location
	 */
	Set<JDPoint> getPossiblePositions();

	/**
	 * Actually inserts the location into the newly created Dungeon
	 *
	 * @param dungeon dungeon to insert the location into
	 * @param x       x coordinate of the room where the location is to be inserted
	 * @param y       y coordinate of the room where the location is to be inserted
	 */
	void insert(Dungeon dungeon, int x, int y);
}
