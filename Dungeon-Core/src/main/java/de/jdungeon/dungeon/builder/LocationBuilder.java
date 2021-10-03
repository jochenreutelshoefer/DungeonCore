package de.jdungeon.dungeon.builder;

import java.util.Objects;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

public class LocationBuilder extends AbstractLocationBuilder {

	/**
	 * We use a String here to allow for easier serialization
	 */
	public String clazz;
	private String identifier;

	public LocationBuilder(Class<? extends Locatable> clazz) {
		this(clazz, 0);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LocationBuilder that = (LocationBuilder) o;
		return clazz.equals(that.clazz) && identifier.equals(that.identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazz, identifier);
	}

	public LocationBuilder(Class<? extends Locatable> clazz, int number) {
		this.clazz = clazz.getName();
		this.identifier = createIdentifier();
	}

	public LocationBuilder(Class<? extends Locatable> clazz, int x, int y) {
		super(new JDPoint(x, y));
		this.clazz = clazz.getName();
		this.identifier = createIdentifier();
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
		return identifier;
	}

	private String createIdentifier() {
		if(clazz.equals(LevelExit.class)) {
			return "ExitLocation";
		}
		String string = Integer.toString(System.identityHashCode(this));
		String s = clazz + string.substring(string.lastIndexOf('@')+1);
		return s.replace(".", "_").replace("-", "_");
	}



	public Class<? extends Locatable> getClazz() {
		try {
			return (Class<? extends Locatable>) ClassReflection.forName(clazz);
		}
		catch (ReflectionException e) {
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

			Room room = dungeon.getRoom(x, y);
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
		}
		catch (ReflectionException e) {
			Log.severe("Could not find/execute constructor for location class: " + clazz);
			e.printStackTrace();
		}
	}

	public String getSimpleName() {
		return getClazz().getSimpleName();
	}



}
