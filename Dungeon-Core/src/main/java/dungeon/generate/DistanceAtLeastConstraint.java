package dungeon.generate;

import dungeon.JDPoint;
import dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class DistanceAtLeastConstraint implements RoomPositionConstraint {

	private final JDPoint point;
	private final int minDistance;

	public DistanceAtLeastConstraint(JDPoint point, int minDistance) {
		this.point = point;
		this.minDistance = minDistance;
	}

	@Override
	public boolean isValid(Room candidateRoom) {
		JDPoint candidatePosition = candidateRoom.getRoomNumber();
		int diffX = Math.abs(candidatePosition.getX() - point.getX());
		int diffY = Math.abs(candidatePosition.getY() - point.getY());
		return diffX + diffY >= minDistance;
	}
}
