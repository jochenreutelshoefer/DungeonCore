package de.jdungeon.dungeon.builder;

import java.util.Collection;

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
