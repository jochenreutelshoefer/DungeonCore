/*
 * Created on 05.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import item.ItemInfo;

public class LayDownItemAction extends Action {
	
	private boolean equipment;
	private int index;
	private ItemInfo item = null;
	
	public ItemInfo getItem() {
		return item;
	}

	@Deprecated
	public LayDownItemAction(boolean equip, int index) {
		this.equipment=equip;
		this.index = index;
	}

	public LayDownItemAction(ItemInfo item) {
		this.item = item;
	}

	/**
	 * @return Returns the equipment.
	 */
	@Deprecated
	public boolean isEquipment() {
		return equipment;
	}

	/**
	 * @return Returns the index.
	 */
	@Deprecated
	public int getIndex() {
		return index;
	}
	
	

}
