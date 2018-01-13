package dungeon;

import item.Key;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.18.
 */
public class Lock<T extends Locatable> implements Locatable {



	public T getLockableClass() {
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
	public ItemOwner getOwner() {
		return null;
	}

	@Override
	public void setOwner(ItemOwner o) {

	}

	@Override
	public void getsRemoved() {

	}
}
