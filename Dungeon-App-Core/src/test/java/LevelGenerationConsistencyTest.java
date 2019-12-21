import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.ReachabilityChecker;
import dungeon.util.RouteInstruction;
import game.JDEnv;
import junit.framework.TestCase;
import level.DefaultDungeonManager;
import level.DungeonFactory;
import level.DungeonManager;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.12.19.
 */
public class LevelGenerationConsistencyTest extends TestCase {

	public void testStartLevel() {
		JDEnv.init(ResourceBundle.getBundle("texts", Locale.GERMAN));
		DungeonManager manager = new DefaultDungeonManager();

		for (int i = 0; i < manager.getNumberOfStages(); i++) {
			List<DungeonFactory> dungeonFactories = manager.getDungeonOptions(i);
			for (DungeonFactory dungeonFactory : dungeonFactories) {
				Dungeon dungeon = dungeonFactory.createDungeon();
				assertNotNull(dungeon);

				// check door consistency
				checkDoorConsistency(dungeon);

				// check that every place is reachable from starting point
				ReachabilityChecker reachabilityChecker = new ReachabilityChecker(dungeon, dungeonFactory.getHeroEntryPoint());
				Collection<JDPoint> unreachableRooms = reachabilityChecker.checkForUnreachableRooms();
				assertTrue("Reachability check failed for dungeon: '" + dungeonFactory.getName()+ "'; unreachable rooms: "+unreachableRooms, unreachableRooms.isEmpty());
			}
		}
	}

	private void checkDoorConsistency(Dungeon dungeon) {
		Collection<Room> allRooms = dungeon.getAllRooms();
		for (Room room : allRooms) {
			RouteInstruction.Direction[] values = RouteInstruction.Direction.values();

			for (RouteInstruction.Direction direction : values) {
				Door door = room.getDoor(direction);
				Room neighbourRoom = room.getNeighbourRoom(direction);
				if (room.isWall()) {
					// rooms that are marked as walls (are no rooms actually) must have no doors
					assertNull(door);
				}
				else if (door == null) {
					if (neighbourRoom != null) {
						// check that the door backwards is also null
						Door doorBackwards = neighbourRoom.getDoor(RouteInstruction.Direction.opposite(direction));
						assertNull("Door backwards was expected also to be null, but was not: Door: " + doorBackwards + " Room: " + room + " direction" + direction, doorBackwards);
					}
				}
				else {
					// check door consistency for connected rooms
					Room otherRoom = door.getOtherRoom(room);
					assertNotNull(otherRoom);
					assertEquals(otherRoom, neighbourRoom);
				}
			}
		}
	}
}
