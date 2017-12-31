package dungeon.generate;

import dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public interface RoomPositionConstraint {

	/**
	 * Tests whether some room is valid considering this constraint
	 *
	 * @param candidateRoom room to be tested
	 * @return test result
	 */
	boolean isValid(Room candidateRoom);

}
