package dungeon.generate.undo;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.Room;
import dungeon.util.RouteInstruction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class AddDoor implements DungeonChangeAction {

	private final Room room;
	private final Door door;
	private final RouteInstruction.Direction dir;
	private Door previousDoor = null;

	public AddDoor(Room room, Door door, RouteInstruction.Direction dir) {
		this.room = room;
		this.door = door;
		this.dir = dir;
	}

	public AddDoor(Room room, RouteInstruction.Direction dir) {
		this.room = room;
		this.door = new Door(room, room.getDungeon().getRoomAt(room, dir));
		this.dir = dir;
	}

	@Override
	public boolean doAction() {
		previousDoor = room.getDoor(dir);
		room.setDoor(door, dir, true);
		return true;
	}

	@Override
	public void undo() {
		room.setDoor(previousDoor, dir, true);
	}
}
