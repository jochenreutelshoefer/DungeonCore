package de.jdungeon.dungeon.generate;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.01.19.
 */
public class DistanceAtMostConstraint implements RoomPositionConstraint{

	private final JDPoint point;
	private final int maxDistance;

	public DistanceAtMostConstraint(JDPoint point, int maxDistance) {
		if(maxDistance == 0) {
			throw new IllegalArgumentException();
		}
		this.point = point;
		this.maxDistance = maxDistance;
	}

	@Override
	public boolean isValid(Room candidateRoom) {
		JDPoint candidatePosition = candidateRoom.getRoomNumber();
		int diffX = Math.abs(candidatePosition.getX() - point.getX());
		int diffY = Math.abs(candidatePosition.getY() - point.getY());
		return diffX + diffY <= maxDistance;
	}
}
