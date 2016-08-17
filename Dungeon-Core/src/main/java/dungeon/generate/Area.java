package dungeon.generate;

import java.util.Collection;

import dungeon.JDPoint;
import dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public interface Area {

	Collection<Room> getRooms();

	JDPoint getPosition();
}
