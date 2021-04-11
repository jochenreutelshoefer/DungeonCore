package de.jdungeon.dungeon.generate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;
import de.jdungeon.item.Item;
import de.jdungeon.item.Key;
import de.jdungeon.item.interfaces.ItemOwner;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.Location;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 02.08.16.
 */
public class ReachabilityChecker {

	private final Dungeon dungeon;
	private final JDPoint entryPoint;
	private boolean allowThroughExit = false;

	public ReachabilityChecker(Dungeon dungeon, JDPoint entryPoint) {
		this.dungeon = dungeon;
		this.entryPoint = entryPoint;
	}

	public ReachabilityChecker(Dungeon dungeon, JDPoint entryPoint, boolean allowThroughExit) {
		this.dungeon = dungeon;
		this.entryPoint = entryPoint;
		this.allowThroughExit = allowThroughExit;
	}

	public boolean check() {
		return check(Collections.<JDPoint>emptyList());
	}

	public boolean checkIgnoreRooms(Collection<Room> ignoredRooms) {
		Set<JDPoint> points = new HashSet<>();
		for (Room room : ignoredRooms) {
			points.add(room.getRoomNumber());
		}
		return check(points);
	}

	public boolean check(Collection<JDPoint> ignoredRooms) {
		return checkForUnreachableRooms(ignoredRooms).isEmpty();
	}

	public Collection<JDPoint> checkForUnreachableRooms() {
		return checkForUnreachableRooms(Collections.<JDPoint>emptyList());
	}

	public Collection<JDPoint> checkForUnreachableRooms(Collection<JDPoint> ignoredRooms) {
		// TODO: improve: do not allow to 'cross' exit room
		// everything should be accessible without going 'through' exit

		Set<Key> keys = new HashSet<Key>();

		Node startNode = new Node(entryPoint);
		Set<Node> closed = new HashSet<>();
		Set<Node> fringe = new HashSet<>();
		Set<Node> postPoned = new HashSet<>();

		fringe.add(startNode);
		while(fringe.iterator().hasNext()) {
			Iterator<Node> iterator = fringe.iterator();
			Node next = iterator.next();
			iterator.remove();

			Node.ExpansionResult expansionResult = next.expand(dungeon, keys);
			if (expansionResult.isPostPoned()) {
				// remember the postponed ones to detect deadlocks
				postPoned.add(next);
			}
			else {
				// this one is fully expanded and solved
				closed.add(next);
			}
			Set<Node> expanded = expansionResult.getExpandedNodes();
			for (Node expandedNode : expanded) {
				if(!closed.contains(expandedNode) && !postPoned.contains(expandedNode)) {
					fringe.add(expandedNode);
				}
			}
			if(expansionResult.isKeyFound()) {
				// reset stop counter, so we do at least one more full iteration of all postponed ones
				fringe.addAll(postPoned);
				postPoned.clear();
			}

		}
		Collection<JDPoint> allRooms = enumRoomsOfDungeon();
		int numberOfRooms = allRooms.size();

		if(closed.size() == numberOfRooms) {
			// all rooms have been expanded
			return Collections.emptySet();
		}

		// some rooms have not been reached, create some debug information - which ones?
		Collection<JDPoint> expandedPoints = new HashSet<>();
		for (Node node : closed) {
			expandedPoints.add(node.point);
		}
		Collection<JDPoint> postPonedPoints = new HashSet<>();
		for (Node node : postPoned) {
			postPonedPoints.add(node.point);
		}
		allRooms.removeAll(expandedPoints);
		allRooms.removeAll(postPonedPoints);
		// allRooms now contains the rooms that have not been reached

		// rule out ignored rooms
		allRooms.removeAll(ignoredRooms);


		return allRooms;

	}

	private Collection<JDPoint> enumRoomsOfDungeon() {
		Room[][] rooms = dungeon.getRooms();
		Collection<JDPoint> allRooms = new HashSet<JDPoint>();
		for (Room[] row : rooms) {
			for (Room room : row) {
				if(!room.isWall()) {
					allRooms.add(room.getRoomNumber());
				}
			}
		}
		return allRooms;
	}

	static class Node {
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Node node = (Node) o;

			return point.equals(node.point);

		}

		@Override
		public int hashCode() {
			return point.hashCode();
		}

		private final JDPoint point;

		public Node(JDPoint room) {
			this.point = room;
		}

		public ExpansionResult expand(Dungeon dungeon, Set<Key> keys) {
			Room current = dungeon.getRoom(this.point);
			Door[] doors = current.getDoors();
			Set<Node> resultSet = new HashSet<Node>();
			ExpansionResult result = new ExpansionResult(resultSet);

			//if(!allowThroughExit) {
				// everything should be reachable without going 'over'/'through' exit
				Location shrine = dungeon.getRoom(point).getLocation();
				if(shrine instanceof LevelExit) {
					// we do not expand the exit room
					return result;
				}
			//}

			for (Door door : doors) {
				if(door == null) continue;
				boolean canPass = true;
				if(door.hasLock()) {
					if(!keys.contains(door.getKey())) {
						canPass = false;
						result.setPostPoned(true);
					}
				}
				if(canPass) {
					Node expandedNode = new Node(door.getOtherRoom(current).getRoomNumber());
					boolean keyFound = expandedNode.collectKeys(dungeon, keys);
					if(keyFound) {
						result.setKeyFound(true);
					}
					resultSet.add(expandedNode);

				}
			}
			return result;
		}

		private boolean collectKeys(Dungeon dungeon, Set<Key> keys) {
			Room room = dungeon.getRoom(point);
			boolean keyFound = false;

			// collect keys lying on ground
			if(collectKeys(keys, room.getItems())) {
				keyFound = true;
			}

			// collect keys from figures in room
			List<Figure> roomFigures = room.getRoomFigures();
			for (ItemOwner figure : roomFigures) {
				if(collectKeys(keys, figure.getItems())) {
					keyFound = true;
				}
			}

			// collect keys from chest
			Chest chest = room.getChest();
			if(chest != null) {
				if (chest.hasLock()) {
					// TODO: currently chests are not locked
				}
				if(collectKeys(keys, chest.getItems())) {
					keyFound = true;
				}
			}
			return keyFound;
		}

		private boolean collectKeys(Set<Key> keys, List<Item> items) {
			boolean keyFound = false;
				for (Item figureItem : items) {
					if(figureItem instanceof Key) {
						if(!keys.contains(figureItem)) {
								keyFound = true;
							keys.add((Key) figureItem);
						}
					}
				}
			return keyFound;
		}

		static class ExpansionResult {
			private final Set<Node> expandedNodes;
			// postponed because some door could not be expanded due to missing key
			private boolean postPoned = false;
			private boolean keyFound = false;

			public boolean isKeyFound() {
				return keyFound;
			}

			public void setKeyFound(boolean keyFound) {
				this.keyFound = keyFound;
			}
			public ExpansionResult(Set<Node> expandedNodes) {
				this.expandedNodes = expandedNodes;
			}

			public boolean isPostPoned() {
				return postPoned;
			}

			public void setPostPoned(boolean postPoned) {
				this.postPoned = postPoned;
			}

			public Set<Node> getExpandedNodes() {
				return expandedNodes;
			}
		}
	}
}
