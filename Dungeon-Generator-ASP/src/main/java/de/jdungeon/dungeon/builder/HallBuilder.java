package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;

public class HallBuilder {

	private final Collection<DoorMarker> removedDoors = new HashSet<>();
	private final Collection<DoorMarker> removedWalls = new HashSet<>();
	private final int upperLeftCornerX;
	private final int upperLeftCornerY;
	private final int width;
	private final int height;

	// default we do not create all internal doors
	private boolean allInternalDoors = false;

	public HallBuilder(int upperLeftCornerX, int upperLeftCornerY, int width, int height) {
		this.upperLeftCornerX = upperLeftCornerX;
		this.upperLeftCornerY = upperLeftCornerY;
		this.width = width;
		this.height = height;
	}

	public HallBuilder createAllInternalDoors() {
		this.allInternalDoors = true;
		return this;
	}

	public HallBuilder removeDoor(DoorMarker door) {
		this.removedDoors.add(door);
		return this;
	}

	public HallBuilder removeWall(DoorMarker door) {
		this.removedWalls.add(door);
		return this;
	}

	public DefaultDoorSpecification build() {
		DefaultDoorSpecification hall = DungeonBuilderASPUtils.createHall(upperLeftCornerX, upperLeftCornerY, width, height, this.allInternalDoors);
		hall.removeDoors(removedDoors);
		hall.removeWalls(removedWalls);
		return hall;
	}

}
