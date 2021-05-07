package de.jdungeon.dungeon.builder;

import java.util.HashSet;
import java.util.Set;

import de.jdungeon.dungeon.util.RouteInstruction;

public class DungeonBuilderASPUtils {

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
