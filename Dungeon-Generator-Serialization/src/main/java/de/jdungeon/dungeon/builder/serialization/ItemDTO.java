package de.jdungeon.dungeon.builder.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import de.jdungeon.item.Item;
import de.jdungeon.log.Log;

public class ItemDTO<T extends Item> extends AbstractDTO {

	String clazzName;
	String name;
	int amount;

	public ItemDTO(Class<T> clazz, String name, int amount) {
		this.clazzName = clazz.getName();
		this.name = name;
		this.amount = amount;
	}

	public ItemDTO(Class<T> clazz, int amount) {
		this.clazzName = clazz.getName();
		this.amount = amount;
	}

	public ItemDTO(Class<T> clazz) {
		this.clazzName = clazz.getName();
	}

	/**
	 * Required for JSON serialization
	 */
	public ItemDTO() {
	}

	public T create() {
		try {
			Class<?> itemClass = Class.forName(clazzName);
			Object newItemInstance = null;

			Constructor<?>[] constructors = itemClass.getConstructors();

			for (Constructor<?> constructor : constructors) {
				// use the standard constructor if available
				if (constructor.getParameterCount() == 0) {
					if (name == null && amount == 0) {
						newItemInstance = constructor.newInstance();
						break;
					}
				}
				if (constructor.getParameterCount() == 1) {
					// use the room constructor if applicable
					if (constructor.getParameterTypes()[0].equals(String.class) && name != null) {
						newItemInstance = constructor.newInstance(name);
						break;
					}
					if (constructor.getParameterTypes()[0].equals(Integer.class) && name == null) {
						newItemInstance = constructor.newInstance(amount);
						break;
					}
				}
				if (constructor.getParameterCount() == 2 && name != null) {
					if (constructor.getParameterTypes()[0].equals(String.class) &&
							constructor.getParameterTypes()[1].equals(Integer.class)) {
						newItemInstance = constructor.newInstance(name, amount);
						break;
					}
					if (constructor.getParameterTypes()[0].equals(Integer.class) &&
							constructor.getParameterTypes()[1].equals(String.class)) {
						newItemInstance = constructor.newInstance(amount, name);
						break;
					}
				}
			}
			return (T) newItemInstance;
		}
		catch (ClassNotFoundException e) {
			Log.severe("Could not find location class for name: " + clazzName);
			e.printStackTrace();
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			Log.severe("Could not find/execute constructor for location class: " + clazzName);
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ItemDTO<?> itemDTO = (ItemDTO<?>) o;
		return amount == itemDTO.amount && clazzName.equals(itemDTO.clazzName) && Objects.equals(name, itemDTO.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazzName, name, amount);
	}
}
