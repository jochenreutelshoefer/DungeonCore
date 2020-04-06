package item.quest;

import dungeon.RoomEntity;
import item.Item;
import item.interfaces.ItemOwner;
import item.interfaces.LocatableItem;
import item.interfaces.Usable;
import shrine.Location;
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
public class Thing extends Item implements Usable, LocatableItem {

	private final Object sup;

	private ItemOwner owner;

	public Thing(String name, Object sup) {
		super(150, false);
		Type = "Gegenstand";
		this.name = name;
		this.sup = sup;
	}

	@Override
	public void setOwner(ItemOwner o) {
		owner = o;
		if (name.equalsIgnoreCase("Luzia�s Amulett")) {
			if (owner instanceof Monster) {

				((Monster) owner).setLuzia(true);
			}
		}
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	public Attribute getHitPoints() {
		return null;
	}

	@Override
	public void getsRemoved() {
		if (owner instanceof Monster) {

			((Monster) owner).setLuzia(true);
		}
	}

	/**
	 * 
	 */
	@Override
	public ItemOwner getOwner() {
		return owner;
	}

	/**
	 * 
	 */
	public Object getSup() {
		return sup;
	}

	@Override
	public String getText() {
		return "";
	}

	public String toString() {
		return Type + ": " + getName();
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		if (f instanceof Hero) {
			if (f.getRoom().getShrine() == this.sup) {
				((Location) sup).use(f, this, meta, round);
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean usableOnce() {
		return false;
	}

}
