package de.jdungeon.dungeon.builder.serialization;

import java.util.Objects;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.jdungeon.dungeon.Room;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

public class LocationDTO<T extends Location>  extends AbstractDTO{

	Class<T> clazz;

	public T insertInto(Room room) {
		try {
			Class<?> locationClazz = ClassReflection.forName(clazz.getName());
			Object newLocationInstance = null;

			com.badlogic.gdx.utils.reflect.Constructor[] constructors = ClassReflection.getConstructors(locationClazz);

			for (Constructor constructor : constructors) {
				// use the standard constructor if available
				int parameterCount = constructor.getParameterTypes().length;
				if (parameterCount == 0) {
					newLocationInstance = constructor.newInstance();
					break;
				}
				if (parameterCount == 1) {
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
			return (T) newLocationInstance;
		}
		catch (ReflectionException e) {
			Log.severe("Could not find location class for name: " + clazz);
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LocationDTO<?> that = (LocationDTO<?>) o;
		return clazz.equals(that.clazz);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazz);
	}
}
