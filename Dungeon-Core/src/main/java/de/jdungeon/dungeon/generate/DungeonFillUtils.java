package de.jdungeon.dungeon.generate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.monster.Monster;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class DungeonFillUtils {


	public static Monster getRandomMonster(Dungeon dungeon, Collection<Room> excludeRoomList) {
		Collection<Figure> alleMonster = dungeon.collectAllFigures();
		List<Monster> potentialMonster = new ArrayList<>();
		for (Figure figure : alleMonster) {
			if(figure instanceof Monster) {
				if(! excludeRoomList.contains(figure.getRoom())) {
					potentialMonster.add((Monster)figure);
				}
			}
		}
		int index = (int) (Math.random() * potentialMonster.size());
		return potentialMonster.get(index);
	}

	public static boolean validateNet(Collection<Room> rooms, Room startPoint) {
		return validateNet(rooms, startPoint, new HashSet<>());
	}

	public static boolean validateNet(Collection<Room> rooms, Room startPoint, Collection<Room> exclude) {
		// tODO: distinguish allocated and reachable, as there are allocated rooms that need to be reachable
		List<Room> reachableRooms = new LinkedList<>();
		List<Room> roomsThatShouldBeReachable = new LinkedList<>();
		for (Room r : rooms) {
			if (!exclude.contains(r) && !r.isWall()) {
				// all room not in a RQ should be reachable
				roomsThatShouldBeReachable.add(r);
			}
		}
		reachableRooms.add(startPoint);
		int k = 0;
		while (k < reachableRooms.size()) {
			Room r = reachableRooms.get(k);
			Door[] doors = r.getDoors();
			for (int i = 0; i < 4; i++) {
				if (doors[i] != null) {
					// TODO: this does not consider locked doors and keys -> replace by ReachabilityChecker
					Room a = doors[i].getOtherRoom(r);
					if (!reachableRooms.contains(a)
							&& (!exclude.contains(a))) {
						reachableRooms.add(a);
					}
				}
			}
			k++;
		}
		// all non rq rooms can be reached
		// not every non rq room can be reached
		return reachableRooms.size() >= roomsThatShouldBeReachable.size();

	}

	public static void createAllDoors(Dungeon dungeon) {
		for(int x = 0; x < dungeon.getSize().getX(); x++) {
			for(int y = 0; y < dungeon.getSize().getY(); y++) {
				Room room = dungeon.getRoom(new JDPoint(x, y));
				RouteInstruction.Direction[] directions = RouteInstruction.Direction.values();
				for (RouteInstruction.Direction direction : directions) {
					Room neighbourRoom = dungeon.getRoomAt(room, direction);
					if(neighbourRoom != null && room.getDoor(direction) == null) {
						Door door = new Door(room, neighbourRoom);
						room.setDoor(door, direction, true);
					}
				}
			}
		}
	}
}
