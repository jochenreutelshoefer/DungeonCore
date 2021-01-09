package dungeon.generate.undo;

import item.Item;
import item.interfaces.ItemOwner;
import org.jetbrains.annotations.NotNull;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.08.16.
 */
public class SetItem implements DungeonChangeAction {

	private final ItemOwner itemOwner;
	private final Item item;
	private boolean possible;

	public SetItem(ItemOwner owner, @NotNull Item item) {
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
