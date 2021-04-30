package de.jdungeon.dungeon.builder;

import java.util.Collection;

/**
 * Specifies for a dungeon
 * - a set of fixed doors required
 * - a set of fixed non-doors (walls) required
 *
 * Note: A DoorSpecification is by no means complete for a dungeon.
 * It usually only specifies a subset of all possible doors/walls.
 *
 */
public class DoorSpecification {

	private final Collection<DoorMarker> doors;
	private final Collection<DoorMarker> walls;

	public DoorSpecification(Collection<DoorMarker> doors, Collection<DoorMarker> walls) {
		this.doors = doors;
		this.walls = walls;
	}

	public Collection<DoorMarker> getDoors() {
		return doors;
	}

	public Collection<DoorMarker> getWalls() {
		return walls;
	}
}
