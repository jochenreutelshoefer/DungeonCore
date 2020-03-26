/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.hero;

import figure.APAgility;
import item.Item;
import item.ItemInfo;
import item.equipment.Armor;
import item.equipment.EquipmentItem;
import item.equipment.EquipmentItemInfo;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Weapon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import spell.AbstractSpell;
import spell.SpellInfo;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.action.EquipmentChangeAction;
import figure.attribute.Attribute;

/**
 * Liefert die Informationen fuer eine Heldensteuerung
 */
public class HeroInfo extends FigureInfo {

	private final Hero h;

	public HeroInfo(Hero h, DungeonVisibilityMap stats) {
		super(h, stats);
		this.h = h;
	}


	@Override
	public List<ItemInfo> getAllItems() {
		if (map.getFigure().equals(h)) {
			List l = h.getAllItems();
			List wrapped = new LinkedList();
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				Item element = (Item) iter.next();
				wrapped.add(ItemInfo.makeItemInfo(element, map));

			}
			return wrapped;
		}
		return null;
	}


	public APAgility getAgility() {
		return h.getAgility();
	}

	public int getHeroCode() {
		return h.getHeroCode();
	}

	public Hero.HeroCategory getHeroCategory() {
		return Hero.HeroCategory.fromValue(getHeroCode());
	}



	public double getAttributeValue(Attribute.Type s) {
		if (map.getFigure().equals(h)) {
			Attribute attribute = h.getAttribute(s);
			if (attribute != null) {
				return attribute.getValue();
			}
		}
		return -1;

	}

	public double getAttributeBasic(Attribute.Type s) {
		if (map.getFigure().equals(h)) {
			Attribute attribute = h.getAttribute(s);
			if (attribute != null) {
				return attribute.getBasic();
			}
		}
		return -1;
	}

	public List<SpellInfo> getSpellBuffer() {
		if (map.getFigure().equals(h)) {
			List<SpellInfo> res = new ArrayList<SpellInfo>();
			List<AbstractSpell> l = h.getCharacter().getSpellBuffer();
			for (Iterator<AbstractSpell> iter = l.iterator(); iter.hasNext();) {
				AbstractSpell element = iter.next();
				res.add(new SpellInfo(element, map));
			}
			return res;
		}
		return null;
	}


	public EquipmentItemInfo getWeapon(int i) {
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Weapon w = h.getInventory().getWeapon(i);
				if (w != null) {
					return new EquipmentItemInfo(w, map);
				}
			}
		}
		return null;
	}



	public EquipmentItemInfo getShield(int i) {
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Shield w = h.getInventory().getShield(i);
				if (w != null) {
					return new EquipmentItemInfo(w, map);
				}
			}
		}
		return null;
	}

	public EquipmentItemInfo getHelmet(int i) {
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Helmet w = h.getInventory().getHelmet(i);
				if (w != null) {
					return new EquipmentItemInfo(w, map);
				}
			}
		}
		return null;
	}

	public EquipmentItemInfo getEquipmentItemInfo(int index, int type) {
		if (type == EquipmentChangeAction.EQUIPMENT_TYPE_ARMOR) {
			return getArmor(index);
		}

		if (type == EquipmentChangeAction.EQUIPMENT_TYPE_HELMET) {
			return getHelmet(index);
		}

		if (type == EquipmentChangeAction.EQUIPMENT_TYPE_SHIELD) {
			return getShield(index);
		}

		if (type == EquipmentChangeAction.EQUIPMENT_TYPE_WEAPON) {
			return getWeapon(index);
		}
		return null;
	}

	public EquipmentItemInfo getArmor(int i) {
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Armor w = h.getInventory().getArmor(i);
				if (w != null) {
					return new EquipmentItemInfo(w, map);
				}
			}
		}
		return null;
	}



	public Zodiac getSign() {
		if (map.getFigure().equals(h)) {
			return h.getSign();
		}
		return null;
	}

}
