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


	private static List<Tupel> explore2(List<Tupel> exploredWay, RoomInfo to,
										boolean blocked,
										int cnt) {
		cnt++;
		if (cnt > 2000) {
			Log.severe("Endlosschleife in dungeon.explore2()");
			System.exit(0);
		}

		boolean more = walkBackToLastUnexploredPoint(exploredWay);
		if (more) {
			// find where to go next
			Tupel fringeNode = (exploredWay.get(exploredWay.size() - 1));
			Explorer ex = fringeNode.exp;
			int directionToBeExploredNext = ex.getOpenDir(to);
			ex.setExplored(directionToBeExploredNext);
			RoomInfo next = fringeNode.room.getDoor(directionToBeExploredNext + 1).getOtherRoom(fringeNode.room);

			Explorer newExp = new Explorer(next, blocked);
			configExp(newExp, exploredWay);
			exploredWay.add(new Tupel(next, newExp));
			if (next.equals(to)) {
				return exploredWay;
			}
			return explore2(exploredWay, to, blocked, cnt);
		}
		else {
			return null;
		}
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
				exp.setExplored(dir - 1);
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
			int k = list.size() - 2; // we walk back the list to find one that still has an open direction
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

		// copy to RoomInfo list
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

	public Explorer(RoomInfo r, boolean blocked) {

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
