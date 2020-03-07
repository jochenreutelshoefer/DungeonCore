package item.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.Way;
import dungeon.util.DungeonUtils;
import dungeon.util.RouteInstruction;
import figure.DungeonVisibilityMap;

/**
 * Generates a randomized AncientMapFragment of given size
 * that is likely to be quite far away from the entry point.
 * Further, the generated AncientMapFragment is likely to cover at least one shrine.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 15.08.16.
 */
public class AncientMapFragmentUtils {

	public static AncientMapFragment createMap(Dungeon dungeon, JDPoint heroEntryPoint, int rooms) {
		List<Room> shortestPathToEntryDistances = getShortestPathToEntryDistanceOrder(dungeon, heroEntryPoint);
		if(shortestPathToEntryDistances.isEmpty()) {
			return null;
		}

		AncientMapFragment result = null;
		int count = 0;
		int limit = 10;
		while(count < limit) {
			if (shortestPathToEntryDistances.size() < 10) {
				result = createRandomMap(shortestPathToEntryDistances.get(0), rooms, dungeon);
			}
			else {
				int randomRoomIndex = (int) (Math.random() * 10);
				result = createRandomMap(shortestPathToEntryDistances.get(randomRoomIndex), rooms, dungeon);
			}
			boolean hasShrine = checkShrine(result, dungeon);
			if(hasShrine) {
				// we try 10 times to generate one covering a shrine
				return result;
			}
		}
		// otherwise we just take the last trial
		return result;
	}

	private static boolean checkShrine(AncientMapFragment result, Dungeon dungeon) {
		Collection<JDPoint> rooms = result.getRooms();
		for (JDPoint point : rooms) {
			if(dungeon.getRoom(point).getShrine() != null) {
				return true;
			}
		}
		return false;
	}

	private static AncientMapFragment createRandomMap(Room room, int numberOfRooms, Dungeon dungeon) {
		List<JDPoint> rooms = new ArrayList<>();
		rooms.add(room.getLocation());
		int limit = 1000;
		int counter = 0;
		while(rooms.size() < numberOfRooms) {
			Room randomRoomAlreadyIncluded = dungeon.getRoom(rooms.get((int) (Math.random() * rooms.size())));
			Room newRoom = hasNeighbourToAdd(randomRoomAlreadyIncluded, rooms, dungeon);
			if(newRoom != null) {
				rooms.add(newRoom.getLocation());
			}

			// a breaking condition, just to be on the safe side (should not be necessary)
			counter++;
			if(counter > limit) {
				break;
			}
		}

		return new AncientMapFragment(rooms);
	}

	private static Room hasNeighbourToAdd(Room room, Collection<JDPoint> rooms, Dungeon dungeon) {
		List<RouteInstruction.Direction> directions = new ArrayList<>();
		directions.addAll(Arrays.asList(RouteInstruction.Direction.values()));
		Collections.shuffle(directions);
		for (RouteInstruction.Direction direction : directions) {
			Room neighbourRoom = room.getNeighbourRoom(direction);
			if(neighbourRoom != null && !rooms.contains(neighbourRoom.getLocation())) {
				return neighbourRoom;
			}
		}
		// none found
		return null;
	}

	private static List<Room> getShortestPathToEntryDistanceOrder(Dungeon dungeon, JDPoint heroEntryPoint) {
		Collection<Room> allRooms = dungeon.getAllRooms();
		List<Room> roomList  = new ArrayList<>(allRooms);
		Collections.shuffle(roomList);
		List<Way> distances = new ArrayList<>();
		//int counter = 0;
		//int limit = 20;
		for (Room room : roomList) {
			Way shortestWay = DungeonUtils.findShortestPath(dungeon, heroEntryPoint, room.getNumber(), DungeonVisibilityMap.getAllVisMap(dungeon), true);
			distances.add(shortestWay);
			//if(counter > limit) {
			//	break;
			//}
		}

		// sort descending by lengths of the paths
		Collections.sort(distances, new Comparator<Way>() {
			@Override
			public int compare(Way o1, Way o2) {
				return Integer.compare(o1.size(), o2.size());
			}
		});

		// collect destination rooms
		List<Room> result = new ArrayList<>();
		for (Way way : distances) {
			// this is the destination room
			result.add(dungeon.getRoom(way.getDestinationRoomInfo().getLocation()));
		}
		return result;
	}

}
