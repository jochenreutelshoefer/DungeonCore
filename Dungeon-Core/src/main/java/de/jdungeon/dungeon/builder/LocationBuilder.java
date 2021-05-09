package de.jdungeon.dungeon.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

public class LocationBuilder extends AbstractLocationBuilder {

	private Class<? extends Locatable> clazz;

	public LocationBuilder(Class<? extends Locatable> clazz) {
		this.clazz = clazz;
	}

	public LocationBuilder(Class<? extends Locatable> clazz, int x, int y) {
		super(new JDPoint(x, y));
		this.clazz = clazz;
	}

	public LocationBuilder(Class<? extends Locatable> clazz, JDPoint pos) {
		super(pos);
		this.clazz = clazz;
	}

	@Override
	public String getIdentifier() {
		// TODO: improve for case that we have multiple instances of one location clazz in a dungeon
		return clazz.getSimpleName();
	}

	public void insert(Dungeon dungeon, int x, int y) {
		try {
			Class<?> locationClazz = Class.forName(clazz.getName());
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
		catch (ClassNotFoundException e) {
			Log.severe("Could not find location class for name: " + clazz);
			e.printStackTrace();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			Log.severe("Could not find/execute constructor for location class: " + clazz);
			e.printStackTrace();
		}
	}

	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public Class<? extends Locatable> getClazz() {
		return clazz;
	}
}
