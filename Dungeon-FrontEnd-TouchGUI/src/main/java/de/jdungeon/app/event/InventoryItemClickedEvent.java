package de.jdungeon.app.event;

import de.jdungeon.event.Event;
import de.jdungeon.item.equipment.EquipmentItemInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class InventoryItemClickedEvent extends Event {

	public EquipmentItemInfo getItem() {
		return item;
	}

	public int getType() {
		return type;
	}

	public ClickType getClick() {
		return click;
	}

	private final EquipmentItemInfo item;
	private final int type;
	private final ClickType click;

	public InventoryItemClickedEvent(EquipmentItemInfo item, int type, ClickType click) {
		this.item = item;
		this.type = type;
		this.click = click;
	}
}
