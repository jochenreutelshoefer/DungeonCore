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
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.log.Log;

public class DungeonDTO extends AbstractDTO {

	//private RoomDTO[][] rooms;

	private HashSet<DoorMarker> doors = new HashSet<>();

	private HashSet<LockDTO> locks = new HashSet<>();

	private Map<String, LocatedEntityBuilder> locations = new HashMap<>();

	private int width;

	private int height;

	public DungeonDTO(JDPoint start, int width, int height) {
		this.width = width;
		this.height = height;
		//this.rooms = new RoomDTO[width][height];
	}



	public Set<LockDTO> getLocks() {
		return locks;
	}

	public DungeonDTO(int width, int height) {
		this.width = width;
		this.height = height;
		//this.rooms = new RoomDTO[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				//rooms[i][j] = new RoomDTO();
			}
		}
	}

	public Map<JDPoint, LocatedEntityBuilder> getLocations() {

		Map<JDPoint, LocatedEntityBuilder> result = new HashMap<>();
		Set<String> points = this.locations.keySet();
		for (String pointString : points) {
			LocatedEntityBuilder locatedEntityBuilder = locations.get(pointString);
			if(locatedEntityBuilder != null) {
				result.put(JDPoint.fromString(pointString), locatedEntityBuilder);
			} else {
				Log.severe("LocationEntityBuilder for point not found: "+pointString);
			}
		}
		return result;
	}

	/*
	public RoomDTO[][] getRooms() {
		return rooms;
	}

	 */

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
		if(locationBuilder == null) {
			Log.severe("LocationEntityBuilder added is null in DungeonDTO");
		} else {
			JDPoint point = new JDPoint(posX, posY);
			// we use Strings as key as otherwise JSON serialization does not work for HashMaps
			locations.put(point.toString(), locationBuilder);
		}
	}

	/**
	 * Returns the position of the first to be found location that matches the given clazz.
	 *
	 * @param locationClazz location clazz
	 * @return position point
	 */
	public JDPoint getLocationPosition(Class<?> locationClazz) {
		for (Map.Entry<String, LocatedEntityBuilder> locationEntry : locations.entrySet()) {
			if(locationEntry.getValue().getClass().equals(locationClazz)) {
				return JDPoint.fromString(locationEntry.getKey());
			}
			if(locationEntry.getValue() instanceof LocationBuilder) {
				Class<? extends Locatable> locationBuilderClass = ((LocationBuilder) locationEntry.getValue()).getClazz();
				if(locationBuilderClass.equals(locationClazz)) {
					return JDPoint.fromString(locationEntry.getKey());
				}
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DungeonDTO that = (DungeonDTO) o;
		boolean locksAreEqual = Objects
				.equals(locks, that.locks);
		boolean locationsAreEqual = Objects.equals(locations, that.locations);
		return /*arrays2Dequals(rooms, that.rooms) &&*/ Objects.equals(doors, that.doors) && locksAreEqual && locationsAreEqual ;
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
		result = 31 * result /*+ Arrays.hashCode(rooms)*/;
		return result;
	}
}
