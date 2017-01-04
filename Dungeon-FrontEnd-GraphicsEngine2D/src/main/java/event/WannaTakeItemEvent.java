package event;

import item.ItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class WannaTakeItemEvent extends Event {

	private final ItemInfo item;

	public WannaTakeItemEvent(ItemInfo item) {
		this.item = item;
	}

	public ItemInfo getItem() {
		return item;
	}

}
