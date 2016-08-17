package item.quest;

import java.util.Iterator;
import java.util.List;

import dungeon.Room;
import item.Item;
import item.interfaces.ItemOwner;
import item.interfaces.Locatable;
import item.interfaces.Usable;
import shrine.Shrine;
import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.monster.Monster;

/**
 * @author Duke1
 * 
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class Thing extends Item implements Usable, Locatable {

	/**
	 * 
	 */
	Object sup;

	/**
	 * 
	 */
	ItemOwner owner;

	public Thing(String name, Object sup) {
		super(150, false);
		Type = "Gegenstand";
		this.name = name;
		this.sup = sup;
	}

	/**
	 * 
	 */
	public void setOwner(ItemOwner o) {
		owner = o;
		if (name.equalsIgnoreCase("Luzia�s Amulett")) {
			if (owner instanceof Monster) {

				((Monster) owner).setLuzia(true);
			}
		}
	}

	public boolean needsTarget() {
		return false;
	}

	/**
	 * @see Item#getHitPoints()
	 */
	public Attribute getHitPoints() {
		return null;
	}

	public void getsRemoved() {
		if (owner instanceof Monster) {

			((Monster) owner).setLuzia(true);
		}
	}

	/**
	 * 
	 */
	public ItemOwner getOwner() {
		return owner;
	}

	/**
	 * 
	 */
	public Object getSup() {
		return sup;
	}

	/**
	 * @see Item#getText()
	 */
	public String getText() {
		return "";
	}

	public String toString() {
		return Type + ": " + getName();
	}

	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	public boolean use(Figure f, Object target, boolean meta) {
		if (f instanceof Hero) {
			if (f.getRoom().getShrine() == this.sup) {
				((Shrine) sup).use(f, this, meta);
				return true;
			}
		}
		return false;

	}

	public boolean usableOnce() {
		return false;
	}

}
