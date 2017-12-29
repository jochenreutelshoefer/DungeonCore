package figure.action;

import item.ItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class TakeItemFromChestAction extends Action {
	private final ItemInfo item;
	public TakeItemFromChestAction(ItemInfo it) {
		this.item = it;

	}

	/**
	 * @return Returns the item.
	 */
	public ItemInfo getItem() {
		return item;
	}

}
