package dungeon;

import java.util.Collection;

import item.Key;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.18.
 */
public class Lock<T extends RoomEntity> implements Locatable, RoomEntity {

	public T getLockableObject() {
		return object;
	}

	public Key getKey() {
		return key;
	}

	private final Key key;
	private final T object;

	public Lock(Key key, T object) {
		this.key = key;
		this.object = object;
	}

	@Override
	public JDPoint getLocation() {
		return object.getLocation();
	}


	@Override
	public Collection<Position> getInteractionPositions() {
		return object.getInteractionPositions();
	}
}
