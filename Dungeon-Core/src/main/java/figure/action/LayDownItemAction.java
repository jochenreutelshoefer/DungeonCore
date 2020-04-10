/*
 * Created on 05.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.hero.Hero;
import figure.hero.Inventory;
import item.Item;
import item.ItemInfo;

public class LayDownItemAction extends AbstractExecutableAction {

	private final Figure figure;
	private boolean equipment;
	private int index;
	private ItemInfo item = null;

	public ItemInfo getItem() {
		return item;
	}

	@Deprecated
	public LayDownItemAction(FigureInfo info, boolean equip, int index) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		this.equipment = equip;
		this.index = index;
	}

	public LayDownItemAction(FigureInfo info, ItemInfo item) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		this.item = item;
	}

	private boolean isEquipment() {
		return equipment;
	}

	private int getIndex() {
		return index;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		if (getItem() != null) {
			ItemInfo itemInfo = getItem();
			Item item = figure.getItemForInfo(itemInfo);
			if (doIt) {
				figure.layDown(item);
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
		boolean equip = isEquipment();
		int index = getIndex();

		if (equip) {
			if (figure instanceof Hero) {
				if (doIt) {
					Inventory inv = ((Hero) figure).getInventory();
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR) {
						inv.layDown(inv.getArmor(inv.getArmorIndex()));
					}
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_HELMET) {
						inv.layDown(inv.getHelmet(inv.getHelmetIndex()));
					}
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD) {
						inv.layDown(inv.getShield(inv.getShieldIndex()));
					}
					if (index == EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON) {
						inv.layDown(inv.getWeapon(inv.getWeaponIndex()));
					}
					return ActionResult.DONE;
				}
				return ActionResult.POSSIBLE;
			}
			return ActionResult.OTHER;
		}
		else {
			if (doIt) {
				Item ding = figure.getItems().get(index);
				figure.layDown(ding);
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
	}
}
