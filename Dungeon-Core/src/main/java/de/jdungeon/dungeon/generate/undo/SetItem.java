package de.jdungeon.dungeon.generate.undo;

import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.ItemOwner;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.08.16.
 */
public class SetItem implements DungeonChangeAction {

	private final ItemOwner itemOwner;
	private final Item item;
	private boolean possible;

	public SetItem(ItemOwner owner, Item item) {
		itemOwner = owner;
		this.item = item;
	}

	@Override
	public boolean doAction() {
		possible = itemOwner.takeItem(item);
		return possible;
	}

	@Override
	public void undo() {
		if(possible) {
			itemOwner.removeItem(item);
		}
	}
}
