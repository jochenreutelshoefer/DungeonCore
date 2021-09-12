package de.jdungeon.dungeon.builder;

import java.util.Collection;

/**
 * Specifies for a dungeon
 * - a set of fixed doors required
 * - a set of fixed non-doors (walls) required
 * <p>
 * Note: A DoorSpecification is by no means complete for a dungeon.
 * It usually only specifies a subset of all possible doors/walls.
 */
public class DefaultDoorSpecification implements DoorSpecification {

	private final Collection<DoorMarker> doors;
	private final Collection<DoorMarker> walls;

	public DefaultDoorSpecification(Collection<DoorMarker> doors, Collection<DoorMarker> walls) {
		this.doors = doors;
		this.walls = walls;
	}

	public void removeDoor(DoorMarker door) {
		this.doors.remove(door);
	}

	public void removeWall(DoorMarker door) {
		this.walls.remove(door);
	}

	public void addDoor(DoorMarker door) {
		if(walls.contains(door)) {
			throw new IllegalStateException("Cannot add door as there is a wall already specified.");
		}
		// hotfix: see hashcode/equal because of door with/without keyString
		this.removeDoor(door);
		this.doors.add(door);
	}

	public void addWall(DoorMarker door) {
		if(doors.contains(door)) {
			throw new IllegalStateException("Cannot add wall as there is a door already specified.");
		}
		this.walls.add(door);
	}

	public void removeDoors(Collection<DoorMarker> doors) {
		this.doors.removeAll(doors);
	}

	public void removeWalls(Collection<DoorMarker> doors) {
		this.walls.removeAll(doors);
	}

	public Collection<DoorMarker> getDoors() {
		return doors;
	}

	public Collection<DoorMarker> getWalls() {
		return walls;
	}
}
