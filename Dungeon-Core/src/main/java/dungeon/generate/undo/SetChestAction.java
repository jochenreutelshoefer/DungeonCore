package dungeon.generate.undo;

import dungeon.Chest;
import dungeon.Dungeon;
import dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class SetChestAction implements DungeonChangeAction {

	private final Chest chest;

	private final Room room;
	private boolean possible;

	public SetChestAction(Chest chest, Room room) {
		this.chest = chest;
		this.room = room;
	}

	@Override
	public boolean doAction() {
		if(room.getShrine() != null) {
			possible = false;
		} else {
			room.setChest(chest);
			possible = true;
		}
		return possible;
	}

	@Override
	public void undo() {
		if(possible) {
			room.setChest(null);
		}
	}

	public Chest getChest() {
		return chest;
	}
}
