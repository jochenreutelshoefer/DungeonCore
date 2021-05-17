package de.jdungeon.dungeon.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

public class LocationBuilder extends AbstractLocationBuilder {

	//private Class<? extends Locatable> clazz;
	public String clazz;

	public LocationBuilder(Class<? extends Locatable> clazz) {
		this.clazz = clazz.getName();
	}

	public LocationBuilder(Class<? extends Locatable> clazz, int x, int y) {
		super(new JDPoint(x, y));
		this.clazz = clazz.getName();
	}

	/**
	 * Required for JSON serialization
	 */
	public LocationBuilder() {
	}

	public LocationBuilder(Class<? extends Locatable> clazz, JDPoint pos) {
		super(pos);
		this.clazz = clazz.getName();
	}

	@Override
	public String getIdentifier() {
		String s = clazz + this.hashCode();
		return s.replace(".", "_").replace("-", "_");
	}

	public Class<? extends Locatable> getClazz() {
		try {
			return (Class<? extends Locatable>) Class.forName(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.severe("Could not find location class for name: " + clazz);
			e.printStackTrace();
		}

		// should not happen
		return null;
	}

	public void insert(Dungeon dungeon, int x, int y) {
		try {
			Class<?> locationClazz = getClazz();
			Object newLocationInstance = null;

			Constructor<?>[] constructors = locationClazz.getConstructors();
			Room room = dungeon.getRoom(x, y);

			for (Constructor<?> constructor : constructors) {
				// use the standard constructor if available
				if (constructor.getParameterCount() == 0) {
					newLocationInstance = constructor.newInstance();
					break;
				}
				if (constructor.getParameterCount() == 1) {
					// use the room constructor if applicable
					if (constructor.getParameterTypes()[0].equals(Room.class)) {
						if (room.getLocation() != null) {
							Log.severe("Room already has a location! :" + room.toString() + " (" + room.getLocation() + ")");
						}
						newLocationInstance = constructor.newInstance(room);
						break;
					}
				}
			}
			room.setLocation((Location) newLocationInstance);
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			Log.severe("Could not find/execute constructor for location class: " + clazz);
			e.printStackTrace();
		}
	}

	public String getSimpleName() {
		return getClazz().getSimpleName();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LocationBuilder that = (LocationBuilder) o;
		return clazz.equals(that.clazz);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazz);
	}
}
