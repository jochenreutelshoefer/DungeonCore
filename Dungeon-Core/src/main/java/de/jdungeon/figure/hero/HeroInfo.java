/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.hero;

import de.jdungeon.figure.APAgility;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.equipment.Armor;
import de.jdungeon.item.equipment.EquipmentItemInfo;
import de.jdungeon.item.equipment.Helmet;
import de.jdungeon.item.equipment.Shield;
import de.jdungeon.item.equipment.weapon.Weapon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.spell.SpellInfo;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.EquipmentChangeAction;
import de.jdungeon.figure.attribute.Attribute;

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


	public int getHeroCode() {
		return h.getHeroCode();
	}

	public Hero.HeroCategory getHeroCategory() {
		return Hero.HeroCategory.fromValue(getHeroCode());
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


}
