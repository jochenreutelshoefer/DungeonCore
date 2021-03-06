package de.jdungeon.item.quest;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.ItemOwner;
import de.jdungeon.item.interfaces.LocatableItem;
import de.jdungeon.item.interfaces.Usable;
import de.jdungeon.location.Location;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.attribute.Attribute;

/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of type
 * comments go to Window>Preferences>Java>Code Generation.
 */
public class Thing extends Item<Thing> implements Usable, LocatableItem {

	private final Object sup;

	private ItemOwner owner;

	public Thing(String name, Object sup) {
		super(150, false);
		this.name = name;
		this.sup = sup;
	}

	@Override
	public void setOwner(ItemOwner o) {
		owner = o;
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
	}

	@Override
	public Thing copy() {
		return new Thing(getName(), getSup());
	}

	@Override
	protected String getTypeVerbalization() {
		return "Gegenstand";
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
		return getTypeVerbalization() + ": " + getName();
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
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		if (f.getRoom().getLocation() == this.sup) {
			((Location) sup).use(f, this, meta, round, doIt);
			return ActionResult.DONE;
		}
		return ActionResult.OTHER;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}
}
