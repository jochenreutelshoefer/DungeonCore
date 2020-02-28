package dungeon.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.Way;
import figure.DungeonVisibilityMap;
import game.DungeonGame;
import log.Log;
import test.TestTracker;

public class DungeonUtils {

	@Deprecated
	public static List<Room> findShortestWayFromTo(Dungeon d, JDPoint p1,
												   JDPoint p2, DungeonVisibilityMap map) {
		return findShortestWayFromTo(d.getRoom(p1), d.getRoom(p2), map);
	}

	@Deprecated
	public static List<Room> findShortestWayFromTo(Room room1, Room room2,
												   DungeonVisibilityMap map) {

		RoomInfo r1 = RoomInfo.makeRoomInfo(room1, map);
		RoomInfo r2 = RoomInfo.makeRoomInfo(room2, map);
		// doPrint();
		// doPrint();
		// doPrint("Beginne Wegsuche: " + r1.toString() + " - " +
		// r2.toString());
		return findShortestWayFromTo(r1, r2);
	}

	@Deprecated
	public static List<Room> findShortestWayFromTo(RoomInfo r1, RoomInfo r2) {
		DungeonGame.getInstance().setTestTracker(new TestTracker());
		// LinkedList way = searchWayGreedy(r1, r2, new LinkedList());
		List<Room> way = searchWayBackTrack(r1, r2);
		removeCycles(way);
		builtShortCuts(way);

		return way;
	}

	public static int stepRight(int pos) {
		if (pos == 7) {
			return 0;
		}
		else {
			return pos + 1;
		}
	}

	public static int stepLeft(int pos) {
		if (pos == 0) {
			return 7;
		}
		else {
			return pos - 1;
		}
	}

	private static void builtShortCuts(List<Room> way) {
		if (way == null) {
			// System.out.println("Weg ist null!");
			return;
		}
		int i = 0;
		while (i < way.size()) {
			Room r1 = (way.get(i));
			for (int j = way.size() - 1; j > 0; j--) {
				Room r2 = (way.get(j));
				if (r2.hasConnectionTo(r1)) {
					for (int k = i + 1; k < j - i; k++) {
						way.remove(i + 1);
					}
					break;
				}
			}
			i++;

		}
	}

	private static void builtShortCuts2(Way way) {
		if (way == null) {
			return;
		}
		int i = 0;
		while (i < way.getRooms().size()) {
			Room r1 = (way.getRooms().get(i));
			for (int j = way.getRooms().size() - 1; j > 0; j--) {
				Room r2 = (way.getRooms().get(j));
				if (r2.hasConnectionTo(r1)) {
					for (int k = i + 1; k < j - i; k++) {
						way.getRooms().remove(i + 1);
					}
					break;
				}
			}
			i++;

		}
	}

	private static void explore(List<Tupel> list, RoomInfo to) {

		boolean more = walkBackToLastUnexploredPoint(list);
		if (more) {

			Tupel t = (list.get(list.size() - 1));
			Explorer ex = t.exp;
			int dir = ex.getOpenDir(to);
			ex.setExplored(dir);
			RoomInfo next = t.room.getDoor(dir + 1).getOtherRoom(t.room);

			Explorer newExp = new Explorer(next, false);
			configExp(newExp, list);
			Tupel tup = new Tupel(next, newExp);
			if (!list.contains(tup)) {
				list.add(tup);
			}
			if (next.equals(to)) {
				return;
			}
			explore(list, to);
		}
	}

	private static List<Tupel> explore2(List<Tupel> list, RoomInfo to,
										boolean blocked,
										int cnt) {
		cnt++;
		if (cnt > 2000) {
			Log.severe("Endlosschleife in dungeon.explore2()");
			System.exit(0);
		}

		boolean more = walkBackToLastUnexploredPoint(list);
		if (more) {

			Tupel fringeNode = (list.get(list.size() - 1));
			Explorer ex = fringeNode.exp;
			int directionToBeExploredNext = ex.getOpenDir(to);
			ex.setExplored(directionToBeExploredNext);
			RoomInfo next = fringeNode.room.getDoor(directionToBeExploredNext + 1).getOtherRoom(fringeNode.room);

			Explorer newExp = new Explorer(next, blocked);
			configExp(newExp, list);
			list.add(new Tupel(next, newExp));
			if (next.equals(to)) {
				return list;
			}
			return explore2(list, to, blocked, cnt);
		}
		else {
			return null;
		}
	}



	private int getLastDir(List<Room> way) {
		if (way.size() < 2) {
			//doPrint("letzte Richtung noch nicht verfuegbar");
			return 0;
		}
		Room r1 = way.get(way.size() - 2);
		Room r2 = way.get(way.size() - 1);
		int dir = r1.getConnectionDirectionTo(r2);
		if (dir == 0) {
			//doPrint("Error bei lastDir() --> Weg gar nicht moeglich");
		}
		return dir;

	}

	/**
	 * We set up the explorer marking which of the accessible neighbour room had already
	 * been accessed by fringe.
	 *
	 * @param exp the new explorer to be set up
	 * @param list fringe list of explored rooms until new
	 */
	private static void configExp(Explorer exp, List<Tupel> list) {
		for (int iterationIndex = 0; iterationIndex < list.size(); iterationIndex++) {  // iterate entire fringe list
			RoomInfo iterationRoom = list.get(iterationIndex).room; 		// room of iteration position in fringe list
			if (exp.r.hasConnectionTo(iterationRoom)) {					// check whether explorer has connection to iter room
				int dir = exp.r.getConnectionDirectionTo(iterationRoom);
				// if so, we mark this direction as explored as we had been there already because its in the fringe list
				exp.setExplored2(dir - 1);
			}
		}
	}

	public static RouteInstruction.Direction getNeighbourDirectionFromTo(
			Room from, Room to) {
		JDPoint fromP = from.getNumber();
		JDPoint toP = to.getNumber();
		return getNeighbourDirectionFromTo(fromP, toP);

	}

	public static RouteInstruction.Direction getNeighbourDirectionFromTo(
			JDPoint fromP, JDPoint toP) {
		if (fromP.getX() == toP.getX()) {
			if (fromP.getY() == toP.getY() - 1) {
				return RouteInstruction.Direction.South;
			}
			else if (fromP.getY() == toP.getY() + 1) {
				return RouteInstruction.Direction.North;

			}
			else {
				// doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
				// return some default value
				return RouteInstruction.Direction.North;
			}
		}
		else if (fromP.getY() == toP.getY()) {
			if (fromP.getX() == toP.getX() - 1) {
				// doPrint("Gefundene Richtung: EAST!");
				return RouteInstruction.Direction.East;
			}
			else if (fromP.getX() == toP.getX() + 1) {
				// doPrint("Gefundene Richtung: WEST!");
				return RouteInstruction.Direction.West;

			}
			else {
				// doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
				// return some default value
				return RouteInstruction.Direction.North;
			}
		}
		else {
			// doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");

			// return some default value
			return RouteInstruction.Direction.North;
		}
	}

	private static void removeCycles(List<Room> l) {
		if (l == null) {
		}
		else {
			int index = 0;
			while (index < l.size() - 1) {
				Room r = l.get(index);
				int lastAppearence = -1;
				for (int i = index + 1; i < l.size(); i++) {

					Room next = l.get(i);
					if (next == r) {
						lastAppearence = i;
					}
				}
				if (lastAppearence > -1) {
					for (int i = index; i < lastAppearence; i++) {
						l.remove(index);
					}
				}
				index++;
			}
		}
	}

	private static void removeCycles2(Way l) {
		if (l == null) {
		}
		else {
			int index = 0;
			while (index < l.size() - 1) {
				Room r = l.getRooms().get(index);
				int lastAppearence = -1;
				for (int i = index + 1; i < l.getRooms().size(); i++) {

					Room next = l.getRooms().get(i);
					if (next == r) {
						lastAppearence = i;
					}
				}
				if (lastAppearence > -1) {
					for (int i = index; i < lastAppearence; i++) {
						l.getRooms().remove(index);
					}
				}
				index++;
			}
		}
	}

	private static boolean walkBackToLastUnexploredPoint(List<Tupel> list) {
		Explorer backtrackExplorer = list.get(list.size() - 1).exp;  // start at the end of the list
		configExp(backtrackExplorer, list);
		if (backtrackExplorer.stillOpen()) {
			return true;
		}
		else {
			int k = list.size() - 2;
			while (!backtrackExplorer.stillOpen()) {
				if (k < 0) {
					return false;
				}
				Tupel t = (list.get(k));
				backtrackExplorer = t.exp;
				list.add(t);
				k--;
			}
		}
		return true;
	}

	private static Way searchWayBackTrack2(Dungeon d, Room fromR, Room toR,
										   DungeonVisibilityMap visMap, boolean blocked) {

		RoomInfo from = RoomInfo.makeRoomInfo(fromR, visMap);
		RoomInfo to = RoomInfo.makeRoomInfo(toR, visMap);

		if (from == to) {
			List<RoomInfo> l = new LinkedList<RoomInfo>();
			l.add(from);
			return new Way(l, false);
		}
		List<Tupel> list = new LinkedList<Tupel>();
		list.add(new Tupel(from, new Explorer(from, blocked)));
		list = explore2(list, to, blocked, 0);
		if (list == null) {
			return null;
		}
		List<RoomInfo> erg = new LinkedList<RoomInfo>();

		for (int i = 0; i < list.size(); i++) {
			erg.add(list.get(i).room);
		}
		return new Way(erg, false);
	}

	public static Way findShortestWayFromTo2(Dungeon d, JDPoint r1, JDPoint r2,DungeonVisibilityMap visMap, boolean crossBlockedDoors) {
		return findShortestWayFromTo2(d, d.getRoom(r1), d.getRoom(r2), visMap, crossBlockedDoors);
	}

	public static Way findShortestWayFromTo2(Dungeon d, Room r1, Room r2, DungeonVisibilityMap visMap, boolean crossBlockedDoors) {
		//TODO: fix expansion: room list contains duplicates
		if (r1.getLocation().equals(r2.getLocation())) {
			// we are already there..
			return new Way(Collections.singletonList(RoomInfo.makeRoomInfo(r1, visMap)), false);
		}
		DungeonGame.getInstance().setTestTracker(new TestTracker());
		Way way = searchWayBackTrack2(d, r1, r2, visMap, crossBlockedDoors);
		removeCycles2(way);
		builtShortCuts2(way);
		return way;
	}

	public static int getFirstStepFromTo2(Dungeon d, Room r1, Room r2, DungeonVisibilityMap visMap) {
		if (r1 == r2) {
			return 0;
		}
		Way way = findShortestWayFromTo2(d, r1, r2, visMap, false);
		if (way == null) {
			// if there is no way without blocked doors then we also take a blocked way // TODO: caller should determine whether blocked ways are interesting or not
			way = findShortestWayFromTo2(d, r1, r2, visMap, true);
		}
		Room next = null;
		if (way.size() == 1) {
		}
		else {
			next = way.get(1).getRoom();
		}
		int dir = 0;
		if (next != null && next.hasConnectionTo(r1)) {
			dir = r1.getConnectionDirectionTo(next);
		}
		return dir;
	}

	private static List<Room> searchWayBackTrack(RoomInfo from, RoomInfo to) {
		if (from.equals(to)) {
			return new LinkedList<Room>();
		}
		LinkedList<Tupel> list = new LinkedList<Tupel>();
		list.add(new Tupel(from, new Explorer(from, false)));
		explore(list, to);
		List<Room> erg = new LinkedList<Room>();
		for (int i = 0; i < list.size(); i++) {
			erg.add(list.get(i).room.getRoom());
		}

		return erg;
	}

}

class Tupel {

	public RoomInfo room;

	public Explorer exp;

	public Tupel(RoomInfo r, Explorer e) {
		this.room = r;
		exp = e;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tupel) {
			if (room.equals(((Tupel) o).room)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = room.hashCode();
		result = 31 * result + exp.hashCode();
		return result;
	}
}

class Explorer {

	/*
	 * direction index
	 * 0 = north
	 * 1 = east
	 * 2 = south
	 * 3 = west
	 *
	 * int code in Explorer:
	 * 0 = no door
	 * 1 = door has been explored
	 * 2 = door existing that is passable
	 *
	 *
	 */

	int[] directions;

	RoomInfo r;

	boolean blocked = false;

	public Explorer(RoomInfo r, boolean blocked) {

		this.blocked = blocked;
		directions = new int[4];
		this.r = r;

		if (blocked) {
			for (int i = 0; i < 4; i++) {

				DoorInfo[] doors = r.getDoors();
				if (doors[i] != null) {
					directions[i] = 2;
				}
				else {
					directions[i] = 0;
				}
			}
		}
		else {

			for (int i = 0; i < 4; i++) {
				DoorInfo[] doors = r.getDoors();
				if (doors == null) {

				}
				else {
					if (doors[i] != null && doors[i].isPassable()) {
						directions[i] = 2;
					}
					else {
						directions[i] = 0;
					}
				}
			}
		}
	}

	public void setExplored(int k) {
		if (directions[k] == 2) {
			directions[k] = 1;
		}
	}

	public void setExplored2(int directionIndex) {
		if (directions[directionIndex] == 2) {
			directions[directionIndex] = 1;
		}
	}

	public int getTotalDirections() {
		int cnt = 0;
		for (int i = 0; i < 4; i++) {
			if (directions[i] != 0) {
				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * Checks for open directions that should be explored.
	 *
	 * @return number of open directions
	 */
	public int getFreeDirections() {
		int cnt = 0;
		for (int i = 0; i < 4; i++) {
			if (directions[i] == 2) {
				// we found a door that should be explored
				cnt++;
			}
		}
		return cnt;
	}

	/**
	 * Returns some open direction to be explored next.
	 * If there are multiple open directions,
	 * it tries to go into the direction of the target destination
	 * as a heuristic
	 *
	 * @param target where we want to go in the end
	 * @return open direction to be explored next
	 */
	public int getOpenDir(RoomInfo target) {
		// Room target= this.

		boolean[] openDirs = new boolean[4];
		for (int i = 0; i < 4; i++) {
			if (directions[i] == 2) {
				openDirs[i] = true;
				;
			}
			else {
				openDirs[i] = false;
			}
		}
		int dx = target.getNumber().getX() - r.getNumber().getX();
		int dy = target.getNumber().getY() - r.getNumber().getY();

		int dirx = 0;
		int diry = 0;
		if (dx > 0) {
			dirx = 1;
		}
		else {
			dirx = 3;
		}

		if (dy > 0) {
			diry = 2;
		}
		else {
			diry = 0;
		}

		int dirOrder[] = new int[4];
		if (Math.abs(dx) > Math.abs(dy)) {
			dirOrder[0] = dirx;
			dirOrder[1] = diry;
			dirOrder[2] = (diry + 2) % 4;
			dirOrder[3] = (dirx + 2) % 4;
		}
		else {
			dirOrder[0] = diry;
			dirOrder[1] = dirx;
			dirOrder[2] = (dirx + 2) % 4;
			dirOrder[3] = (diry + 2) % 4;
		}
		for (int i = 0; i < 4; i++) {
			if (openDirs[dirOrder[i]]) {
				return dirOrder[i];
			}
		}
		return -1;
	}

	public boolean stillOpen() {
		return getFreeDirections() > 0;
	}
}
