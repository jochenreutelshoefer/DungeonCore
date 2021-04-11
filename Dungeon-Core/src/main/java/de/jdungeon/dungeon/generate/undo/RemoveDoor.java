package de.jdungeon.dungeon.generate.undo;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class RemoveDoor implements DungeonChangeAction {

	private final Room room;
	private final RouteInstruction.Direction direction;
	private Door previousDoor;

	public RemoveDoor(Room room, RouteInstruction.Direction direction) {
		this.room = room;
		this.direction = direction;
	}

	@Override
	public boolean doAction() {
		previousDoor = room.getDoor(direction);
		room.removeDoor(previousDoor, true);
		return true;
	}

	@Override
	public void undo() {
		// assert that there is no new door since then to maintain consistency
		assert(room.getDoor(direction) == null);
		room.setDoor(previousDoor, direction, true);
	}
}
