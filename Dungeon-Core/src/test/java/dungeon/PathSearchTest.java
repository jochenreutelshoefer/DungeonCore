package dungeon;

import java.util.Iterator;
import java.util.List;

import dungeon.generate.DungeonFillUtils;
import dungeon.util.DungeonUtils;
import dungeon.util.RouteInstruction;
import figure.DungeonVisibilityMap;
import junit.framework.TestCase;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.03.20.
 */
public class PathSearchTest extends TestCase {

	public static final String WAY_NOT_FOUND = "way not found";
	public static final String WAY_FOUND = "way found where none should be";
	public static final String WAY_HAS_WRONG_LENGTH = "way has wrong length";

	public void testPathStraightDown() {
		Dungeon dungeon = prepareDungeon();



		Way way = DungeonUtils.findShortestWayFromTo2(dungeon, new JDPoint(1, 1), new JDPoint(1, 8), DungeonVisibilityMap.getAllVisMap(dungeon), false);
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

		Way way = DungeonUtils.findShortestWayFromTo2(dungeon, new JDPoint(1, 1), new JDPoint(0, 8), DungeonVisibilityMap.getAllVisMap(dungeon), false);
		assertTrue(WAY_NOT_FOUND, way != null);
		assertTrue(WAY_HAS_WRONG_LENGTH, way.size() == 9);
		assertIsConnected(way, false);
		assertRoomPathIndex(way, 0, 1, 1);
		assertRoomPathIndex(way, 8, 0, 8);

	}

	/*
	public void testNonExistingPathToOtherArea() {
		Dungeon dungeon = prepareDungeon();

		assertTrue("dungeon does not exist", dungeon != null);

		int toX = 12;
		int toY = 1;
		Way way = DungeonUtils.findShortestWayFromTo2(dungeon, new JDPoint(1, 1), new JDPoint(toX, toY), DungeonVisibilityMap.getAllVisMap(dungeon), false);
		assertTrue(WAY_FOUND, way == null);

	}
	*/

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

		assertTrue("dungeon does not exist", dungeon != null);

		return dungeon;
	}

	private void removeAllDoorsAtX11(Dungeon dungeon) {
		for(int y = 0; y < dungeon.getSize().getY(); y++) {
			Room room = dungeon.getRoom(new JDPoint(11, y));
			room.removeDoor(RouteInstruction.Direction.East.getValue(), true);
		}
	}

	private void assertRoomPathIndex(Way path, int pathIndex, int roomX, int roomY) {
		RoomInfo roomInfo = path.get(pathIndex);
		assertTrue("unexpected room in path", roomInfo.getNumber().equals(new JDPoint(roomX, roomY)));
	}
}
