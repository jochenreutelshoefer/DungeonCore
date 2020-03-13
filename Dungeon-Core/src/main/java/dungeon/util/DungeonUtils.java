package dungeon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.Path;
import figure.DungeonVisibilityMap;

public class DungeonUtils {





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

	public static Path findShortestPath(Dungeon dungeon, Room start, Room goal, DungeonVisibilityMap visibilityMap, boolean crossBlockedDoors) {
		return findShortestPath(dungeon, start.getPoint(), goal.getPoint(), visibilityMap, crossBlockedDoors);

	}

	public static Path findShortestPath(Dungeon dungeon, JDPoint start, JDPoint goal, DungeonVisibilityMap visibilityMap, boolean crossBlockedDoors) {
		RoomInfo startRoom = RoomInfo.makeRoomInfo(dungeon.getRoom(start), visibilityMap);
		List<SearchNode> fringe = new ArrayList<>();
		Set<JDPoint> closed = new HashSet<>();
		fringe.add(new SearchNode(startRoom, null, 0));

		while(!fringe.isEmpty()) {
			SearchNode node = fringe.remove(0);// get first from fringe
			if(node.room.getPoint().equals(goal)) {
				// we found our goal
				return createPathTo(node);
			}
			fringe.addAll(expandNode(node, closed, crossBlockedDoors));
		}
		// we have not found a path, so we return null;
		return null;
	}

	private static Path createPathTo(SearchNode node) {
		List<RoomInfo> roomSequence = new ArrayList<>();
		SearchNode current = node;
		while(current != null) {
			roomSequence.add(current.room);
			current = current.predecessor;
		}
		Collections.reverse(roomSequence);
		return new Path(roomSequence);
	}

	private static Collection<SearchNode> expandNode(SearchNode node, Set<JDPoint> closed, boolean crossBlockedDoors) {
		Collection<SearchNode> expansionNodes = new ArrayList<>();
		RoomInfo room = node.room;
		DoorInfo[] doors = room.getDoors();
		for (DoorInfo door : doors) {
			if(door != null) {
				RoomInfo otherRoom = door.getOtherRoom(room);
				if(closed.contains(otherRoom.getPoint())) continue; // already visited

				if(crossBlockedDoors) {
					expansionNodes.add(new SearchNode(otherRoom, node, node.distanceWalked+1));
				} else {
					if(door.isPassable()) {
						expansionNodes.add(new SearchNode(otherRoom, node,node.distanceWalked+1));
					}
				}
			}
		}
		closed.add(node.room.getPoint());
		return expansionNodes;
	}

	public static RouteInstruction.Direction getFirstStepFromTo(Dungeon dungeon, Room start, Room destination, DungeonVisibilityMap visMap) {
		Path shortestPath = findShortestPath(dungeon, start, destination, visMap, false);
		if(shortestPath == null) {
			return null;
		} else {
			return RouteInstruction.Direction.fromInteger(start.getConnectionDirectionTo(shortestPath.get(1)));
		}
	}

	private static class SearchNode {
		private final RoomInfo room;
		private final SearchNode predecessor;
		private final int distanceWalked;

		public SearchNode(RoomInfo room, SearchNode predecessor, int distanceWalked) {

			this.room = room;
			this.predecessor = predecessor;
			this.distanceWalked = distanceWalked;
		}
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
