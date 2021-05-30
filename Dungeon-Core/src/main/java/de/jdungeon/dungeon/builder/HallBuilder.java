package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.jdungeon.dungeon.util.RouteInstruction;

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
		DefaultDoorSpecification hall = createHall(upperLeftCornerX, upperLeftCornerY, width, height, this.allInternalDoors);
		hall.removeDoors(removedDoors);
		hall.removeWalls(removedWalls);
		return hall;
	}

	public static DefaultDoorSpecification createHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height, boolean setAllInternalDoors) {
		Set<DoorMarker> doors = new HashSet<>();
		Set<DoorMarker> walls = new HashSet<>();

		for (int x = upperLeftCornerX; x < upperLeftCornerX + width; x++) {
			for (int y = upperLeftCornerY; y < upperLeftCornerY + height; y++) {
				if (setAllInternalDoors) {
					doors.addAll(DoorMarker.createAllDoorsOfRoom(x, y));
				}
				if (x == upperLeftCornerX) {
					// left border
					DoorMarker wallLeft = DoorMarker.create(x, y, RouteInstruction.Direction.West);
					walls.add(wallLeft);
					doors.remove(wallLeft);
				}
				if (x == upperLeftCornerX + width-1) {
					// right border
					DoorMarker wallRight = DoorMarker.create(x, y, RouteInstruction.Direction.East);
					walls.add(wallRight);
					doors.remove(wallRight);
				}
				if (y == upperLeftCornerY) {
					// bottom top
					DoorMarker wallTop = DoorMarker.create(x, y, RouteInstruction.Direction.North);
					walls.add(wallTop);
					doors.remove(wallTop);
				}
				if (y == upperLeftCornerY + height-1) {
					// bottom border
					DoorMarker wallBottom = DoorMarker.create(x, y, RouteInstruction.Direction.South);
					walls.add(wallBottom);
					doors.remove(wallBottom);
				}
			}
		}

		return new DefaultDoorSpecification(doors, walls);
	}

}
