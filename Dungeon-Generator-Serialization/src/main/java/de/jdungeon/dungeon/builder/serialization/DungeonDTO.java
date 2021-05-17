package de.jdungeon.dungeon.builder.serialization;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import jdk.nashorn.internal.scripts.JD;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.DoorMarker;
import de.jdungeon.dungeon.builder.LocatedEntityBuilder;

public class DungeonDTO extends AbstractDTO {

	private RoomDTO[][] rooms;

	private Set<DoorMarker> doors = new HashSet<>();

	private Set<LockDTO> locks = new HashSet<>();

	private Map<String, LocatedEntityBuilder> locations = new HashMap<>();

	private int width;

	private int height;

	public DungeonDTO(JDPoint start, int width, int height) {
		this.width = width;
		this.height = height;
		this.rooms = new RoomDTO[width][height];
	}

	public Set<LockDTO> getLocks() {
		return locks;
	}

	public DungeonDTO(int width, int height) {
		this.width = width;
		this.height = height;
		this.rooms = new RoomDTO[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				rooms[i][j] = new RoomDTO();
			}
		}
	}

	public Map<JDPoint, LocatedEntityBuilder> getLocations() {

		Map<JDPoint, LocatedEntityBuilder> result = new HashMap<>();
		Set<String> points = this.locations.keySet();
		for (String pointString : points) {
			result.put(JDPoint.fromString(pointString), locations.get(pointString));
		}
		return result;
	}

	public RoomDTO[][] getRooms() {
		return rooms;
	}

	public DungeonDTO() {
	}

	public void addDoor(DoorMarker door) {
		this.doors.add(door);
	}

	public void addLock(LockDTO lock) {
		this.locks.add(lock);
	}

	public int getWidth() {
		return width;
	}

	public Set<DoorMarker> getDoors() {
		return Collections.unmodifiableSet(doors);
	}

	public int getHeight() {
		return height;
	}

	public void addLocation(LocatedEntityBuilder locationBuilder, int posX, int posY) {
		JDPoint point = new JDPoint(posX, posY);
		// we use Strings as key as otherwise JSON serialization does not work for HashMaps
		locations.put(point.toString(), locationBuilder);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DungeonDTO that = (DungeonDTO) o;
		boolean locksAreEqual = Objects
				.equals(locks, that.locks);
		boolean locationsAreEqual = Objects.equals(locations, that.locations);
		return arrays2Dequals(rooms, that.rooms) && Objects.equals(doors, that.doors) && locksAreEqual && locationsAreEqual ;
	}

	static boolean arrays2Dequals(RoomDTO[][] grid1, RoomDTO[][] grid2) {
		if (grid1.length != grid2.length) return false;

		for (int i = 0; i < grid1.length; i++) {
			RoomDTO[] array1 = grid1[i];
			RoomDTO[] array2 = grid2[i];
			boolean eq = Arrays.equals(array1, array2);
			if (!eq) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(doors, locks, locations);
		result = 31 * result + Arrays.hashCode(rooms);
		return result;
	}
}
