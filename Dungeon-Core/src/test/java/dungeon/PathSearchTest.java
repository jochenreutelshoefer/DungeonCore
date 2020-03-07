package dungeon;

import java.util.Iterator;
import java.util.List;

import dungeon.generate.DungeonFillUtils;
import dungeon.util.DungeonUtils;
import figure.DungeonVisibilityMap;
import item.Key;
import junit.framework.TestCase;

import static dungeon.util.RouteInstruction.*;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.03.20.
 */
public class PathSearchTest extends TestCase {

	public static final String WAY_NOT_FOUND = "path not found";
	public static final String WAY_FOUND = "path found where none should be";
	public static final String WAY_HAS_WRONG_LENGTH = "path has wrong length";

	public void testPathStraightDown() {
		Dungeon dungeon = prepareDungeon();



		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(1, 1), new JDPoint(1, 8), DungeonVisibilityMap.getAllVisMap(dungeon), false);
		assertTrue(WAY_NOT_FOUND, way != null);
		assertTrue(WAY_HAS_WRONG_LENGTH, way.size() == 8);
		assertIsConnected(way, false);
		assertRoomPathIndex(way, 0, 1, 1);
		assertRoomPathIndex(way, 1, 1, 2);
		assertRoomPathIndex(way, 2, 1, 3);
		assertRoomPathIndex(way, 3, 1, 4);
		assertRoomPathIndex(way, 4, 1, 5);
		assertRoomPathIndex(way, 5, 1, 6);
		assertRoomPathIndex(way, 6, 1, 7);
		assertRoomPathIndex(way, 7, 1, 8);

	}

	public void testPathDownOneOffsetLeft() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(1, 1), new JDPoint(0, 8), DungeonVisibilityMap.getAllVisMap(dungeon), false);
		assertTrue(WAY_NOT_FOUND, way != null);
		assertTrue(WAY_HAS_WRONG_LENGTH, way.size() == 9);
		assertIsConnected(way, false);
		assertRoomPathIndex(way, 0, 1, 1);
		assertRoomPathIndex(way, 8, 0, 8);

	}

	/*
	Check passage through locked door
	 */
	public void test3() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		boolean crossBlockedDoors = true;
		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(1, 1), new JDPoint(5, 3), DungeonVisibilityMap.getAllVisMap(dungeon), crossBlockedDoors);
		assertTrue(WAY_NOT_FOUND, way != null);
		assertTrue(WAY_HAS_WRONG_LENGTH, way.size() == 7);
		assertIsConnected(way, crossBlockedDoors);
		assertRoomPathIndex(way, 0, 1, 1);
		assertRoomPathIndex(way, 1, 2, 1);
		assertRoomPathIndex(way, 2, 2, 2);
		assertRoomPathIndex(way, 3, 3, 2);
		assertRoomPathIndex(way, 4, 4, 2);
		assertRoomPathIndex(way, 5, 4, 3);
		assertRoomPathIndex(way, 6, 5, 3);

	}

	/*
	Check passage with locked door without using blocked paths, going another way
	 */
	public void test4() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		boolean crossBlockedDoors = false;
		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(1, 1), new JDPoint(5, 3), DungeonVisibilityMap.getAllVisMap(dungeon), crossBlockedDoors);
		assertTrue(WAY_NOT_FOUND, way != null);
		assertTrue(WAY_HAS_WRONG_LENGTH, way.size() == 9);
		assertIsConnected(way, crossBlockedDoors);
		assertRoomPathIndex(way, 0, 1, 1);
		assertRoomPathIndex(way, 2, 2, 0);
		assertRoomPathIndex(way, 3, 3, 0);
		assertRoomPathIndex(way, 8, 5, 3);

	}

	/*
	Check path where one needs to go in the opposite direction first to find the shortest path
	 */
	public void test5() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		boolean crossBlockedDoors = false;
		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(6, 7), new JDPoint(9, 6), DungeonVisibilityMap.getAllVisMap(dungeon), crossBlockedDoors);
		assertTrue(WAY_NOT_FOUND, way != null);
		assertTrue(WAY_HAS_WRONG_LENGTH, way.size() == 11);
		assertIsConnected(way, crossBlockedDoors);
		assertRoomPathIndex(way, 0, 6, 7);
		assertRoomPathIndex(way, 1, 5, 7);
		assertRoomPathIndex(way, 2, 5, 8);
		assertRoomPathIndex(way, 3, 5, 9);
		assertRoomPathIndex(way, 4, 6, 9);
		assertRoomPathIndex(way, 5, 7, 9);
		assertRoomPathIndex(way, 6, 8, 9);
		assertRoomPathIndex(way, 7, 9, 9);
		assertRoomPathIndex(way, 8, 9, 8);
		assertRoomPathIndex(way, 9, 9, 7);
		assertRoomPathIndex(way, 10, 9, 6);

	}



	public void testNonExistingPathToOtherArea() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		int toX = 12;
		int toY = 1;
		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(1, 1), new JDPoint(toX, toY), DungeonVisibilityMap.getAllVisMap(dungeon), false);
		assertTrue(WAY_FOUND, way == null);

	}

	public void testNonExistingPathToOtherAreaBlocked() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		int toX = 12;
		int toY = 1;
		Way way = DungeonUtils.findShortestPath(dungeon, new JDPoint(1, 1), new JDPoint(toX, toY), DungeonVisibilityMap.getAllVisMap(dungeon), true);
		assertTrue(WAY_FOUND, way == null);

	}


	private void assertIsConnected(Way way, boolean allowBlockedDoors) {
		List<Room> rooms = way.getRooms();
		Iterator<Room> iterator = rooms.iterator();
		Room last = iterator.next();
		while (iterator.hasNext()) {
			Room next = iterator.next();
			String message = "subsequent Rooms in path not connected! ";
			if(allowBlockedDoors) {
				assertTrue(message, last.hasConnectionTo(next));
			} else {
				assertTrue(message, last.hasOpenConnectionTo(next));
			}
			last = next;
		}
	}

	private Dungeon prepareDungeon() {
		Dungeon dungeon = new Dungeon(20, 10);
		DungeonFillUtils.createAllDoors(dungeon);

		removeAllDoorsAtX11(dungeon);

		removeDoorsTest3(dungeon);

		removeDoorsTest5(dungeon);

		assertTrue("dungeon does not exist", dungeon != null);

		return dungeon;
	}

	private void removeDoorsTest5(Dungeon dungeon) {
		// remove all straight line down just letting space one top and one bottom
		for(int y = 1; y < dungeon.getSize().getY()-1; y++) {
			removeDoor(dungeon, 8, y, Direction.East);
		}
		removeDoor(dungeon, 8, 8, Direction.South);
		removeDoor(dungeon, 7, 8, Direction.South);
		removeDoor(dungeon, 6, 8, Direction.South);
		removeDoor(dungeon, 6, 8, Direction.West);
		removeDoor(dungeon, 6, 8, Direction.North);

	}

	private void removeDoorsTest3(Dungeon dungeon) {
		removeDoor(dungeon, 2, 1, Direction.East);
		removeDoor(dungeon, 3, 1, Direction.South);
		removeDoor(dungeon, 3, 2, Direction.South);
		removeDoor(dungeon, 4, 2, Direction.East);
		removeDoor(dungeon, 4, 1, Direction.South);
		removeDoor(dungeon, 4, 3, Direction.South);
		removeDoor(dungeon, 4, 3, Direction.West);

		// add locked door within path
		setDoorLocked(dungeon, 3, 2, Direction.East);
	}

	private void removeDoor(Dungeon dungeon, int x, int y, Direction dir) {
		dungeon.getRoom(new JDPoint(x, y)).removeDoor(dir.getValue(), true);
	}

	private void setDoorLocked(Dungeon dungeon, int x, int y, Direction dir) {
		Room room = dungeon.getRoom(new JDPoint(x, y));
		Room neighbourRoom = room.getNeighbourRoom(dir);
		room.setDoor(new Door(room, neighbourRoom, new Key("foo")), dir, true);
	}

	private void removeAllDoorsAtX11(Dungeon dungeon) {
		for(int y = 0; y < dungeon.getSize().getY(); y++) {
			removeDoor(dungeon, 11, y, Direction.East);
		}
	}

	private void assertRoomPathIndex(Way path, int pathIndex, int roomX, int roomY) {
		RoomInfo roomInfo = path.get(pathIndex);
		assertTrue("unexpected room in path", roomInfo.getNumber().equals(new JDPoint(roomX, roomY)));
	}
}
