package de.jdungeon.dungeon.generate;

import java.util.Collection;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public interface Area {

	Collection<Room> getRooms();

	JDPoint getPosition();
}
