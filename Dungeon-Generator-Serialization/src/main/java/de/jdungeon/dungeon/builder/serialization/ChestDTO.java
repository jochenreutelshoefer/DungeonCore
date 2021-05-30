package de.jdungeon.dungeon.builder.serialization;

import java.util.Objects;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Room;
import de.jdungeon.item.Item;
import de.jdungeon.log.Log;

public class ChestDTO extends AbstractDTO{

	ItemDTO item;

	public ChestDTO() {
	}

	public Chest insertInto(Room room) {
		try {
			Class<?> locationClazz = Chest.class;
			Object newChestInstance = null;

			Constructor[] constructors = ClassReflection.getConstructors(locationClazz);

			for (Constructor constructor : constructors) {
				// use the standard constructor if available
				int parameterCount = constructor.getParameterTypes().length;
				if (parameterCount == 0) {
					newChestInstance = constructor.newInstance();
					break;
				}
				if (parameterCount == 1) {
					// use the room constructor if applicable
					if (constructor.getParameterTypes()[0].equals(Room.class)) {
						if (room.getChest() != null) {
							Log.severe("Room already has a chest! :" + room.toString() + " (" + room.getLocation() + ")");
						}
						newChestInstance = constructor.newInstance(room);
						break;
					}
				}
			}
			Item itemInstance = this.item.create();
			Chest chestInstance = (Chest) newChestInstance;
			chestInstance.takeItem(itemInstance);
			room.setChest(chestInstance);
			return chestInstance;
		}
		catch ( ReflectionException e) {
			Log.severe("Could not find/execute constructor for location class: " + Chest.class);
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChestDTO chestDTO = (ChestDTO) o;
		return Objects.equals(item, chestDTO.item);
	}

	@Override
	public int hashCode() {
		return Objects.hash(item);
	}
}
