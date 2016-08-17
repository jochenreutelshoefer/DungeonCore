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


	public SetChestAction(Chest chest, Room room) {
		this.chest = chest;
		this.room = room;
	}

	@Override
	public void doAction() {
		assert(room.getChest() == null);
		room.setChest(chest);
	}

	@Override
	public void undo() {
		room.setChest(null);
	}

	public Chest getChest() {
		return chest;
	}
}
