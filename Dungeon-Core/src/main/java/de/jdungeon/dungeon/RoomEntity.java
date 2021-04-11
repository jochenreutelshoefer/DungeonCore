package de.jdungeon.dungeon;

import java.util.Collection;

import de.jdungeon.item.interfaces.Locatable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 23.01.18.
 */
public interface RoomEntity extends Locatable {

	Collection<Position> getInteractionPositions();
}
