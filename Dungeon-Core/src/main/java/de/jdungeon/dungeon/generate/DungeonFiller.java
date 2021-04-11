package de.jdungeon.dungeon.generate;

import java.util.Collection;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.item.Item;
import de.jdungeon.item.Key;

public interface DungeonFiller {

	Dungeon getDungeon();

	Monster getSmallMonster(int value);

	Monster getBigMonster(int value);

	void equipMonster(Monster m, int rate);

	boolean isAllocated(Room room);

	Room getUnallocatedRandomRoom(RoomPositionConstraint... contr);

	Room getUnallocatedRimRoom(boolean cornerAllowed);

	Room getUnallocatedRandomRoom(JDPoint near);

	void addAllocatedRoom(Room room);

	void addAllocatedRooms(Collection<Room> rooms);

	Key getNextKey();

	Key getOtherKey(Collection<Key> usedKeys);

	Collection<DeadEndPath> getDeadEndsUnallocated();

	/**
	 * Tries to find some empty space to fill with content.
	 * The point area will be used to start the search for
	 * this empty space.
	 *
	 * @param sizeX
	 * @param sizeY
	 * @param area
	 * @return
	 */
	RectArea getUnallocatedSpace(int sizeX, int sizeY, JDPoint area);

	/**
	 * * Tries to find some empty space randomly to fill with content.
	 * @return
	 */
	RectArea getUnallocatedSpaceRandom(int sizeX, int sizeY, RoomPositionConstraint... constraints);

	/**
	 * Returns an area that is unallocated and valid according to reachability.
	 *
	 * @param entryRoom
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	RectArea getValidArea(Room entryRoom, int sizeX, int sizeY);

	/**
	 * Sets an de.jdungeon.item that needs to be distributed in an accessible way
	 * during the de.jdungeon.level generation process
	 *
	 * @param item de.jdungeon.item
	 */
	void itemToDistribute(Item item);

	void itemsToDistribute(Collection<Item> items);

	/**
	 * Returns and removes one de.jdungeon.item for distribution.
	 * Returns null if no more de.jdungeon.item remaining.
	 *
	 * @return de.jdungeon.item
	 */
	Item getItemForDistribution();

	/**
	 * Removes the given number of doors randomly by retaining full reachability.
	 * If the number of doors cannot be removed without reducing reachability,
	 * only the number of doors possible is removed.
	 *
	 * @param number number of doors to be removed if possible
	 * @return number of doors that actually have been removed
	 */
	int removeDoors(int number, JDPoint entryPoint);

	RouteInstruction.Direction getUnallocatedRandomNeighbour(Room exitRoom);

	void setToWallUnreachableRoom(JDPoint heroEntryPoint);
}
