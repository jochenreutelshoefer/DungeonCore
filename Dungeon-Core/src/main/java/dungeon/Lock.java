package dungeon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import figure.Figure;
import item.Key;
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

	private Set<Figure> figuresThatCanLocateKey = new HashSet<>();

	public boolean isKeyLocatable(Figure f) {
		return figuresThatCanLocateKey.contains(f);
	}

	public void setKeyLocatable(Figure f) {
		figuresThatCanLocateKey.add(f);
	}

	public Lock(Key key, T object) {
		this.key = key;
		this.object = object;
	}

	@Override
	public String toString() {
		// todo : factor out String
		return "Schloss: "+key.getType();
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
