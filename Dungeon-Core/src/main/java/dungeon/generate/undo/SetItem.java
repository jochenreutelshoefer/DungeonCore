package dungeon.generate.undo;

import item.Item;
import item.interfaces.ItemOwner;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.08.16.
 */
public class SetItem implements DungeonChangeAction {

	private final ItemOwner itemOwner;
	private final Item item;

	public SetItem(ItemOwner owner, Item item) {
		itemOwner = owner;
		this.item = item;
	}

	@Override
	public void doAction() {
		boolean taken = itemOwner.takeItem(item);
		assert taken;
	}

	@Override
	public void undo() {
		boolean removed = itemOwner.removeItem(item);
		assert removed;
	}
}
