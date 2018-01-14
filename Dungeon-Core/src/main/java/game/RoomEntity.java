package game;

import java.util.Collection;

import dungeon.PositionInRoomInfo;
import figure.DungeonVisibilityMap;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.01.18.
 */
public abstract class RoomEntity extends InfoEntity {

	public RoomEntity(DungeonVisibilityMap m) {
		super(m);
	}

	public abstract Collection<PositionInRoomInfo> getInteractionPositions();
}
