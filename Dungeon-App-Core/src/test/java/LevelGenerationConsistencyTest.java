import java.util.*;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.generate.ReachabilityChecker;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.game.JDEnv;
import de.jdungeon.util.MyResourceBundle;
import de.jdungeon.level.DefaultDungeonManager;
import de.jdungeon.user.DungeonFactory;
import de.jdungeon.level.DungeonManager;

import static junit.framework.Assert.*;

/**
 * This tests checks for all listed dungeon factories that the generated dungeon is consistent.
 * That is:
 * - all doors are set consistently (consistent properties from each side)
 * - all room that are part of the de.jdungeon.level can be reached from the starting point
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.12.19.
 */
public class LevelGenerationConsistencyTest {

	public void testLevels() throws DungeonGenerationException {
		//JDEnv.init(ResourceBundle.getBundle("texts", Locale.GERMAN));
		JDEnv.init(new MyResourceBundle() {
			@Override
			public String get(String key) {
				return null;
			}

			@Override
			public String format(String key, String... inserts) {
				return null;
			}
		});
		DungeonManager manager = new DefaultDungeonManager();

		for (int i = 0; i < manager.getNumberOfStages(); i++) {
			List<DungeonFactory> dungeonFactories = manager.getDungeonOptions(i);
			for (DungeonFactory dungeonFactory : dungeonFactories) {
				dungeonFactory.create();
				Dungeon dungeon = dungeonFactory.assembleDungeon();
				dungeon.prepare();
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
					//assertNull(door);
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
