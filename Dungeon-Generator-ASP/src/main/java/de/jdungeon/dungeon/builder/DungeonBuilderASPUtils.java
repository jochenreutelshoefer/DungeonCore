package de.jdungeon.dungeon.builder;

import java.util.HashSet;
import java.util.Set;

import de.jdungeon.dungeon.util.RouteInstruction;

public class DungeonBuilderASPUtils {

	public static DefaultDoorSpecification createHall(int upperLeftCornerX, int lowerLeftCornerY, int width, int height, boolean setAllInternalDoors) {
		Set<DoorMarker> doors = new HashSet<>();
		Set<DoorMarker> walls = new HashSet<>();

		for (int x = upperLeftCornerX; x < upperLeftCornerX + width; x++) {
			for (int y = lowerLeftCornerY; y < lowerLeftCornerY + height; y++) {
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
				if (y == lowerLeftCornerY) {
					// bottom border
					DoorMarker wallBottom = DoorMarker.create(x, y, RouteInstruction.Direction.South);
					walls.add(wallBottom);
					doors.remove(wallBottom);
				}
				if (y == lowerLeftCornerY + height-1) {
					// top border
					DoorMarker wallTop = DoorMarker.create(x, y, RouteInstruction.Direction.North);
					walls.add(wallTop);
					doors.remove(wallTop);
				}
			}
		}

		return new DefaultDoorSpecification(doors, walls);
	}
}
