package dungeon.generate.undo;

import dungeon.Room;
import shrine.Location;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class SetShrineAction implements DungeonChangeAction {

	private final Location shrine;
	private final Room room;
	private boolean possible;

	public SetShrineAction(Location shrine, Room room) {
		this.shrine = shrine;
		this.room = room;
	}

	@Override
	public boolean doAction() {
		if(room.getShrine() != null) {
			possible = false;
		} else {
			room.setShrine(shrine);
			possible = true;
		}
		return possible;
	}

	@Override
	public void undo() {
		if(possible) {
			room.setShrine(null);
		}
	}
}
