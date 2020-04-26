package dungeon;

import java.util.Collection;

import item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 23.01.18.
 */
public interface RoomEntity extends Locatable {

	Collection<Position> getInteractionPositions();
}
