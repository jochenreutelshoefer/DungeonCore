package de.jdungeon.dungeon.builder;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public abstract class AbstractLocationBuilder implements LocatedEntityBuilder {

	public JDPoint room;

	public AbstractLocationBuilder() {
	}

	@Override
	public int getBuildPriority() {
		return 0;
	}

	public AbstractLocationBuilder(JDPoint room) {
		this.room = room;
	}

	public abstract String getIdentifier();

	public AbstractLocationBuilder setRoom(int x, int y) {
		this.room = new JDPoint(x, y);
		return this;
	}

	public boolean hasFixedRoomPosition() {
		return room != null;
	}

	public JDPoint getRoomPosition() {
		return room;
	}

	@Override
	public void write(Json json) {
		json.writeFields(this);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {

		Field[] declaredFields = ClassReflection.getDeclaredFields(this.getClass());
		for (Field declaredField : declaredFields) {
			String fieldName = declaredField.getName();
			Object fieldValue = null;
			if (jsonData.hasChild(fieldName)) {
				fieldValue = json.readValue(declaredField.getType(), jsonData.get(fieldName));
			}
			if (declaredField.getType().equals(String.class)) {
				fieldValue = jsonData.getString(fieldName);
			}
			try {
				declaredField.set(this, fieldValue);
			}
			catch (ReflectionException e) {
				e.printStackTrace();
			}
		}
	}
}
