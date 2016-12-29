package figure.action;

import dungeon.DoorInfo;
import dungeon.util.RouteInstruction;

public class LockAction extends Action {

	private final DoorInfo door;

	public LockAction(DoorInfo door) {
		this.door = door;
	}

	public DoorInfo getDoor() {
		return door;
	}

}
