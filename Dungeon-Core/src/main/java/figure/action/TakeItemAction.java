
package figure.action;

import item.ItemInfo;

/**
 * Gegenstand-Aufnehm-Aktion. Enthaelt den Gegenstand, der aufgenommen werden soll.
 *
 */

public class TakeItemAction extends Action {
	private final ItemInfo item;
	public TakeItemAction(ItemInfo it) {
		super();
		this.item = it;
		
	}

	/**
	 * @return Returns the item.
	 */
	public ItemInfo getItem() {
		return item;
	}
}
