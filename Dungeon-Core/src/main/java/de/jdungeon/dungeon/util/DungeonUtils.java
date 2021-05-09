package de.jdungeon.dungeon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Path;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.FigureInfo;

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

	public static Path findShortestPath(FigureInfo figure, JDPoint start, JDPoint goal, boolean crossBlockedDoors) {
		return findShortestPath(start, goal, figure.getVisMap(), crossBlockedDoors);
	}

	public static Path findShortestPath(Room start, Room goal, DungeonVisibilityMap visibilityMap, boolean crossBlockedDoors) {
		return findShortestPath(start.getPoint(), goal.getPoint(), visibilityMap, crossBlockedDoors);
	}

	public static Path findShortestPath(JDPoint start, JDPoint goal, DungeonVisibilityMap visibilityMap, boolean crossBlockedDoors) {
		RoomInfo startRoom = RoomInfo.makeRoomInfo(visibilityMap.getDungeon().getRoom(start), visibilityMap);
		List<SearchNode> fringe = new ArrayList<>();
		Set<JDPoint> closed = new HashSet<>();
		fringe.add(new SearchNode(startRoom, null, 0));

		while (!fringe.isEmpty()) {
			SearchNode node = fringe.remove(0);// get first from fringe
			if (node.room.getPoint().equals(goal)) {
				// we found our goal
				return createPathTo(node);
			}
			Collection<SearchNode> expandedNode = expandNode(node, closed, crossBlockedDoors);
			for (SearchNode searchNode : expandedNode) {
				if(!fringe.contains(searchNode)) {
					fringe.add(searchNode);

				}
			}
		}
		// we have not found a path, so we return null;
		return null;
	}

	private static Path createPathTo(SearchNode node) {
		List<RoomInfo> roomSequence = new ArrayList<>();
		SearchNode current = node;
		while (current != null) {
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
		if (doors != null) {
			for (DoorInfo door : doors) {
				if (door != null) {
					RoomInfo otherRoom = door.getOtherRoom(room);
					if (closed.contains(otherRoom.getPoint())) continue; // already visited

					if (crossBlockedDoors) {
						expansionNodes.add(new SearchNode(otherRoom, node, node.distanceWalked + 1));
					}
					else {
						if (door.isPassable() != null && door.isPassable()) {  // TODO: find solution for not knowing the passable state due to vis
							expansionNodes.add(new SearchNode(otherRoom, node, node.distanceWalked + 1));
						}
					}
				}
			}
		}
		closed.add(node.room.getPoint());
		return expansionNodes;
	}

	public static RouteInstruction.Direction getFirstStepFromTo(Dungeon dungeon, Room start, Room destination, DungeonVisibilityMap visMap) {
		Path shortestPath = findShortestPath(start, destination, visMap, false);
		if (shortestPath == null) {
			return null;
		}
		else {
			return RouteInstruction.Direction.fromInteger(start.getConnectionDirectionTo(shortestPath.get(1)));
		}
	}

	public static RouteInstruction.Direction getFirstStepFromTo(Dungeon dungeon, JDPoint start, JDPoint destination, DungeonVisibilityMap visMap) {
		Path shortestPath = findShortestPath(start, destination, visMap, false);
		if (shortestPath == null) {
			return null;
		}
		else {
			return RouteInstruction.Direction.fromInteger(dungeon.getRoom(start)
					.getConnectionDirectionTo(shortestPath.get(1)));
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

		@Override
		public String toString() {
			return "SearchNode{" +
					"room=" + room +
					'}';
		}
	}
}

