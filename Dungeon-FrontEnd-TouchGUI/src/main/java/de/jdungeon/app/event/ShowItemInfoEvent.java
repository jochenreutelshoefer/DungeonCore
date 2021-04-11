package de.jdungeon.app.event;

import de.jdungeon.event.Event;
import de.jdungeon.item.ItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class ShowItemInfoEvent extends Event {

	public ItemInfo getItem() {
		return item;
	}

	private final ItemInfo item;

	public ShowItemInfoEvent(ItemInfo item) {
		this.item = item;
	}
}
