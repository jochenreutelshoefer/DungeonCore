/*
 * Created on 27.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

/**
 * Gegenstand-benutzen-Aktion. Enthaelt den Gegenstand der benutzt werden soll.
 */

import game.RoomEntity;
import item.ItemInfo;

public class UseItemAction extends Action {

	private ItemInfo it;

	private RoomEntity target;
	private boolean meta;

	public UseItemAction(ItemInfo i, RoomEntity target,boolean meta) {

		it = i;
		this.target = target;
		this.meta = meta;

	}

	public UseItemAction(ItemInfo i) {

		it = i;

	}

	/**
	 * @return Returns the item.
	 */
	public ItemInfo getItem() {
		return it;
	}

	public RoomEntity getTarget() {
		return target;
	}

	public boolean isMeta() {
		return meta;
	}
}
