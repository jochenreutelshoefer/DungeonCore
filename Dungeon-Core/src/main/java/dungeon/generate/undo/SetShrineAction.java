package dungeon.generate.undo;

import dungeon.Dungeon;
import dungeon.Room;
import shrine.Shrine;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class SetShrineAction implements DungeonChangeAction {

	private final Shrine shrine;
	private final Room room;

	public SetShrineAction(Shrine shrine, Room room) {
		this.shrine = shrine;
		this.room = room;
	}

	@Override
	public void doAction() {
		assert(room.getShrine() == null);
		room.setShrine(shrine);
	}

	@Override
	public void undo() {
		room.setShrine(null);
	}
}
