package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.log.Log;

public class HallBuilder {

	private final Collection<DoorMarker> removedDoors = new HashSet<>();
	private final Collection<DoorMarker> removedWalls = new HashSet<>();
	private final int upperLeftCornerX;
	private final int upperLeftCornerY;
	private final int width;
	private final int height;
	private final int dungeonWidth;
	private final int dungeonHeight;

	private Set<AbstractLocationBuilder> locations = new HashSet<>();

	// default we do not create all internal doors

	private boolean allInternalDoors = false;

	public HallBuilder(int upperLeftCornerX, int upperLeftCornerY, int width, int height, int dungeonWidth, int dungeonHeight) {
		this.upperLeftCornerX = upperLeftCornerX;
		this.upperLeftCornerY = upperLeftCornerY;
		this.width = width;
		this.height = height;
		this.dungeonWidth = dungeonWidth;
		this.dungeonHeight = dungeonHeight;
	}

	public HallBuilder addLocation(LocationBuilder builder, int x, int y) {
		return this.addLocation(builder, new JDPoint(x, y));
	}

	public HallBuilder addLocation(AbstractLocationBuilder builder, JDPoint position) {
		builder.setPosition(position);
		this.locations.add(builder);
		return this;
	}

	public HallBuilder createAllInternalDoors() {
		this.allInternalDoors = true;
		return this;
	}

	public void addNonPositionedLocation(LocationBuilder locationBuilder) {
		int maxX = upperLeftCornerX + width;
		int maxY = upperLeftCornerY + height;
		for(int x = upperLeftCornerX; x < maxX; x++) {
			for(int y = upperLeftCornerY; y < maxY; y++) {
				locationBuilder.addPossiblePosition(new JDPoint(x, y));
			}
		}
		this.locations.add(locationBuilder);
	}


	public HallBuilder removeDoor(DoorMarker door) {
		this.removedDoors.add(door);
		return this;
	}

	public HallBuilder removeWall(DoorMarker door) {
		this.removedWalls.add(door);
		return this;
	}

	public Hall build() {
		DefaultDoorSpecification doorSpec = createHall(upperLeftCornerX, upperLeftCornerY, width, height, this.allInternalDoors, dungeonWidth, dungeonHeight);
		doorSpec.removeDoors(removedDoors);
		doorSpec.removeWalls(removedWalls);
		return new Hall(doorSpec, locations);
	}

	public static DefaultDoorSpecification createHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height, boolean setAllInternalDoors, int dungeonWidth, int dungeonHeight) {
		Set<DoorMarker> doors = new HashSet<>();
		Set<DoorMarker> walls = new HashSet<>();

		for (int x = upperLeftCornerX; x < upperLeftCornerX + width; x++) {
			for (int y = upperLeftCornerY; y < upperLeftCornerY + height; y++) {
				if (setAllInternalDoors) {
					doors.addAll(DoorMarker.createAllDoorsOfRoomIfInRange(x, y, dungeonWidth, dungeonHeight));
				}
				if (x == upperLeftCornerX) {
					// left border
					DoorMarker wallLeft = DoorMarker.create(x, y, RouteInstruction.Direction.West);
					walls.add(wallLeft);
					doors.remove(wallLeft);
				}
				if (x == upperLeftCornerX + width - 1) {
					// right border
					DoorMarker wallRight = DoorMarker.create(x, y, RouteInstruction.Direction.East);
					walls.add(wallRight);
					doors.remove(wallRight);
				}
				if (y == upperLeftCornerY) {
					// bottom top
					DoorMarker wallTop = DoorMarker.createIfInRange(x, y, RouteInstruction.Direction.North, dungeonWidth, dungeonHeight);
					walls.add(wallTop);
					doors.remove(wallTop);
				}
				if (y == upperLeftCornerY + height - 1) {
					// bottom border
					DoorMarker wallBottom = DoorMarker.create(x, y, RouteInstruction.Direction.South);
					walls.add(wallBottom);
					doors.remove(wallBottom);
				}
			}
		}

		return new DefaultDoorSpecification(doors, walls);
	}

	public HallBuilder removeWallIfPossible(int x, int y, RouteInstruction.Direction dir) {
		try {
			DoorMarker doorMarker = DoorMarker.create(x, y, dir);
			removeWall(doorMarker);
			return this;
		}
		catch (IllegalArgumentException e) {
			// we are out of dungeon range
			return this;
		}
	}
}
