/*
 * Created on 12.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.hero;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.RoomObservationStatus;

import item.Item;
import item.ItemInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import spell.Spell;
import spell.SpellInfo;
import item.equipment.*;
import item.equipment.weapon.*;

/**
 * Liefert die Informationen fuer eine Heldensteuerung
 */
public class HeroInfo extends FigureInfo {

	private Hero h;

	public HeroInfo(Hero h, DungeonVisibilityMap stats) {
		super(h, stats);
		this.h = h;
	}

	public Map getHighScoreString(String playerName, String comment,
			boolean reg, boolean liga) {
		return h.getHighScoreData(playerName, comment, reg, liga, this);
	}

	public List getAllItems() {
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

	public Boolean hasLuziaBall() {
		if (map.getFigure().equals(h)) {
			return new Boolean(h.hasLuziaBall());
		}
		return null;

	}

	public int getMight() {
		int lvl = h.getLevel();
		return (lvl+1) * 500;
	}

	public Boolean LuziaBallSeesEnemy() {
		if (map.getFigure().equals(h)) {
			if (!hasLuziaBall().booleanValue()) {
				return new Boolean(false);
			}
			return new Boolean(h.getLuziasBall().seesEnemy());
		}
		return null;
	}

	public EquipmentItemInfo getActualWeapon() {
		if (map.getFigure().equals(h)) {
			EquipmentItem i = h.getInventory().getWeapon1();
			if (i != null) {
				return new EquipmentItemInfo(i, map);
			}
		}
		return null;
	}

	public EquipmentItemInfo getActualArmor() {
		if (map.getFigure().equals(h)) {
			EquipmentItem i = h.getInventory().getArmor1();
			if (i != null) {
				return new EquipmentItemInfo(i, map);
			}
		}
		return null;
	}

	public int getHeroCode() {
		if (map.getVisibilityStatus(h.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return h.getHeroCode();
		}
		return -1;
	}

	public EquipmentItemInfo getActualHelmet() {
		if (map.getFigure().equals(h)) {
			EquipmentItem i = h.getInventory().getHelmet1();
			if (i != null) {
				return new EquipmentItemInfo(i, map);
			}
		}
		return null;
	}

	public EquipmentItemInfo getActualShield() {
		if (map.getFigure().equals(h)) {
			EquipmentItem i = h.getInventory().getShield1();
			if (i != null) {
				return new EquipmentItemInfo(i, map);
			}
		}
		return null;
	}

	public double getAttributeValue(int s) {
		if (map.getFigure().equals(h)) {
			return h.getAttribute(s).getValue();
		}
		return -1;

	}

	public double getAttributeBasic(int s) {
		if (map.getFigure().equals(h)) {
			return h.getAttribute(s).getBasic();
		}
		return -1;
	}

	public int getExpCode() {
		if (map.getFigure().equals(h)) {
			return h.getCharacter().getExpCode();
		}
		return -1;
	}

	public Boolean hasSkillPoints() {
		if (map.getFigure().equals(h)) {
			return new Boolean(h.getCharacter().hasSkillPoints());
		}
		return null;
	}

	public int getWeaponIndex() {

		if (map.getFigure().equals(h)) {
			return h.getInventory().getWeaponIndex();
		}
		return -1;
	}

	public int getHelmetIndex() {
		if (map.getFigure().equals(h)) {
			return h.getInventory().getHelmetIndex();
		}
		return -1;
	}

	public int getArmorIndex() {
		if (map.getFigure().equals(h)) {
			return h.getInventory().getArmorIndex();
		}
		return -1;
	}

	public int[] getExpInfo(int a, int b) {
		if (map.getFigure().equals(h)) {
			return h.getCharacter().getExpInfo(a, b);
		}
		return null;
	}

	public int getShieldIndex() {
		if (map.getFigure().equals(h)) {
			return h.getInventory().getShieldIndex();
		}
		return -1;
	}

	public LinkedList getSpellBuffer() {
		if (map.getFigure().equals(h)) {

			LinkedList res = new LinkedList();
			List l = h.getCharacter().getSpellBuffer();
			for (Iterator iter = l.iterator(); iter.hasNext();) {
				Spell element = (Spell) iter.next();
				res.add(new SpellInfo(element, map));
			}
			return res;
		}
		return null;
	}

	public int getSpellPoints() {
		if (map.getFigure().equals(h)) {
			return h.getCharacter().getSpellPoints();
		}
		return -1;
	}

	public String getWeaponString(int i) {
		String s = null;
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Weapon w = h.getInventory().getWeapon(i);
				if (w != null) {
					s = w.toString();
				}
			}
		}
		return s;
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

	public EquipmentItemInfo[] getShields() {
		if (map.getFigure().equals(h)) {
			EquipmentItemInfo[] its = new EquipmentItemInfo[3];
			for (int i = 0; i < 3; i++) {
				EquipmentItem it = h.getInventory().getShield(i);
				if (it != null) {
					its[i] = new EquipmentItemInfo(it, map);
				}
			}

			return its;
		}
		return null;
	}

	public EquipmentItemInfo[] getHelmets() {
		if (map.getFigure().equals(h)) {
			EquipmentItemInfo[] its = new EquipmentItemInfo[3];
			for (int i = 0; i < 3; i++) {
				EquipmentItem it = h.getInventory().getHelmet(i);
				if (it != null) {
					its[i] = new EquipmentItemInfo(it, map);
				}
			}

			return its;
		}
		return null;
	}

	public EquipmentItemInfo[] getWeapons() {
		if (map.getFigure().equals(h)) {
			EquipmentItemInfo[] its = new EquipmentItemInfo[3];
			for (int i = 0; i < 3; i++) {
				EquipmentItem it = h.getInventory().getWeapon(i);
				if (it != null) {
					its[i] = new EquipmentItemInfo(it, map);
				}
			}
			return its;
		}
		return null;
	}

	public EquipmentItemInfo[] getArmors() {
		if (map.getFigure().equals(h)) {
			EquipmentItemInfo[] its = new EquipmentItemInfo[3];
			for (int i = 0; i < 3; i++) {
				EquipmentItem it = h.getInventory().getArmor(i);
				if (it != null) {
					its[i] = new EquipmentItemInfo(it, map);
				}
			}
			return its;
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

	public int getSkillPoints() {
		if (map.getFigure().equals(h)) {
			return h.getCharacter().getSkillPoints();
		}
		return -1;
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

	public String getHelmetString(int i) {
		if (map.getFigure().equals(h)) {
			String s = "null";
			if (i < 3 && i >= 0) {
				Helmet w = h.getInventory().getHelmet(i);
				if (w != null) {
					s = w.toString();
				}
			}

			return s;
		}
		return null;
	}

	public String getShieldString(int i) {

		String s = null;
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Shield w = h.getInventory().getShield(i);
				if (w != null) {
					s = w.toString();
				}
			}
		}
		return s;

	}

	public String getArmorString(int i) {

		String s = null;
		if (map.getFigure().equals(h)) {
			if (i < 3 && i >= 0) {
				Armor w = h.getInventory().getArmor(i);
				if (w != null) {
					s = w.toString();
				}
			}
		}
		return s;
	}

	public String getSign() {
		if (map.getFigure().equals(h)) {
			return h.getSign();
		}
		return null;
	}

	public int getTotalExp() {
		if (map.getFigure().equals(h)) {
			return h.getCharacter().getTotalExp();
		}
		return -1;
	}

	public int getLevel() {
		if (map.getVisibilityStatus(h.getLocation()) >= RoomObservationStatus.VISIBILITY_FIGURES) {

			return h.getLevel();
		}
		return -1;
	}
}
