package de.jdungeon.dungeon;

import java.util.Collection;

import de.jdungeon.figure.DungeonVisibilityMap;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.01.18.
 */
public abstract class RoomInfoEntity extends InfoEntity {

	public RoomInfoEntity(DungeonVisibilityMap m) {
		super(m);
	}

	public abstract Collection<PositionInRoomInfo> getInteractionPositions();
}
