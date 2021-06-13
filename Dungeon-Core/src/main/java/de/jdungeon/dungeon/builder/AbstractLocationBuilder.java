package de.jdungeon.dungeon.builder;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public abstract class AbstractLocationBuilder implements LocatedEntityBuilder {

	/**
	 * A location can EITHER have a fixed predefined position
	 */
	public JDPoint room;

	/**
	 * OR can have a set of possible positions to be chosen from by the level generator
	 */
	public Set<JDPoint> possiblePositions = new HashSet<>();

	/**
	 * OR can have no information for positioning, then the generator will pick some arbitrary location on the level
	 */

	public AbstractLocationBuilder() {
	}

	@Override
	public int getBuildPriority() {
		return 0;
	}

	public void setPosition(JDPoint room) {
		this.room = room;
	}

	public AbstractLocationBuilder(JDPoint room) {
		this.room = room;
	}

	public abstract String getIdentifier();

	public boolean hasFixedRoomPosition() {
		return room != null;
	}

	public Set<JDPoint> getPossiblePositions() {
		return possiblePositions;
	}

	public void setPossiblePositions(Set<JDPoint> possiblePositions) {
		this.possiblePositions = possiblePositions;
	}

	public void addPossiblePosition(JDPoint point) {
		this.possiblePositions.add(point);
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
