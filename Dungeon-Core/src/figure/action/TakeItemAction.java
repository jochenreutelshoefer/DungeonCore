
package figure.action;

import item.ItemInfo;

/**
 * Gegenstand-Aufnehm-Aktion. Enthaelt den Gegenstand, der aufgenommen werden soll.
 *
 */

public class TakeItemAction extends Action {
	private ItemInfo item;
	public TakeItemAction(/*int fighterIndex,*/ ItemInfo it) {
		super(/*fighterIndex*/);
		this.item = it;
		
	}

	/**
	 * @return Returns the item.
	 */
	public ItemInfo getItem() {
		return item;
	}
}
