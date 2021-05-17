package de.jdungeon.dungeon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.jdungeon.figure.Figure;
import de.jdungeon.item.Key;
import de.jdungeon.item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.18.
 */
public class Lock<T extends Lockable> implements Locatable, RoomEntity {

	private Key key;

	private T object;

	public Lock(Key key, T object) {
		this.key = key;
		this.object = object;
		object.setLock(this);
	}

	public Lock() {
	}

	public Key getKey() {
		return key;
	}

	public T getLockableObject() {
		return object;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Lock<?> lock = (Lock<?>) o;
		return key.equals(lock.key) && object.equals(lock.object);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, object);
	}

	@Override
	public String toString() {
		// todo : factor out String
		return "Schloss: " + key.getType();
	}

	@Override
	public JDPoint getRoomNumber() {
		return object.getRoomNumber();
	}

	@Override
	public Collection<Position> getInteractionPositions() {
		return object.getInteractionPositions();
	}

}
