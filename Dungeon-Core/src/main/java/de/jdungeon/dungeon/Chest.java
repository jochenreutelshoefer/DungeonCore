package de.jdungeon.dungeon;

import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.memory.ChestMemory;
import de.jdungeon.figure.percept.ItemDroppedPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.gui.Paragraphable;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.Key;
import de.jdungeon.item.interfaces.ItemOwner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.jdungeon.util.JDColor;

/**
 * Eine Truhe steht immer fest in einem Raum. Truhen koennen Gegenstaende
 * enthalten die herausgenommen ein hineingelegt werden koennen. Truhen koennen
 * verschlossen sein.
 */

public class Chest implements Lockable, ItemOwner, Paragraphable, InfoProvider, RoomEntity {

	private final List<Item> items;

	private Lock lock;
	private boolean locked = false;

	private JDPoint location;
	private Room r;

	public Chest() {
		items = new LinkedList<>();
	}

	public void setRoom(Room r) {
		this.r = r;
	}

	public ChestMemory getMemoryObject(FigureInfo info) {
		return new ChestMemory(this, info);
	}

	public Chest(Item i) {
		items = new LinkedList<>();
		this.takeItem(i);
	}

	public void setLock(Key k) {
		this.lock = new Lock(k, this);
	}

	public void setLock(Lock l) {
		this.lock = l;
	}

	public Chest(List<Item> list) {
		items = new LinkedList<>();
		for (int i = 0; i < list.size(); i++) {
			Item it = list.get(i);
			if (it != null) {
				this.takeItem(it);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chest chest = (Chest) o;
		return locked == chest.locked && items.equals(chest.items) && Objects.equals(lock, chest.lock) && location
				.equals(chest.location) && Objects.equals(r, chest.r);
	}

	@Override
	public int hashCode() {
		return Objects.hash(items, lock, locked, location, r);
	}

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new ChestInfo(this, map);
	}

	@Override
	public Room getRoom() {
		return r;
	}

	@Override
	public Item unwrapItem(ItemInfo it) {
		for (Iterator<Item> iter = items.iterator(); iter.hasNext(); ) {
			Item element = iter.next();
			if (ItemInfo.makeItemInfo(element, null).equals(it)) {
				return element;
			}
		}
		return null;
	}

	@Override
	public boolean hasItem(Item i) {
		return items.contains(i);
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(items.get(i), map);
		}
		return array;
	}

	@Override
	public boolean addItems(List<Item> l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (l.get(i));
			if (it != null) {
				this.takeItem(it);
			}
		}
		return true;
	}

	public Chest(List<Item> list, Key k) {
		items = new ArrayList<Item>();
		for (int i = 0; i < list.size(); i++) {
			Item it = list.get(i);
			if (it != null) {
				this.takeItem(it);
			}
		}
		this.lock = new Lock(k, this);
	}

	public void setLocation(JDPoint p) {
		location = p;
	}

	public boolean hasLock() {
		return lock != null;
	}

	@Override
	public boolean lockMatches(Key k) {
		return false;
	}

	@Override
	public void toggleLock(Key k) {

	}

	public boolean lock(Key k) {
		if (lock != null && k.equals(lock.getKey())) {
			locked = !locked;
			return true;
		}
		return false;
	}

	public void clicked(Figure f, boolean right) {
		if (items != null) {
			List<Item> droppedItems = new ArrayList<>(this.items);
			this.items.clear();
			f.getDungeon().getRoom(location).addItems(droppedItems, null);
			f.getRoom().distributePercept(new ItemDroppedPercept(droppedItems, f, -1));
		}
	}

	public Lock getLock() {
		return lock;
	}

	@Override
	public void setKey(Key k) {
		this.lock = new Lock(k, this);
		locked = true;
	}

	@Override
	public boolean getLocked() {
		return locked;
	}

	@Override
	public void setLocked(boolean b) {
		this.locked = b;
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(JDEnv.getString("chest"));
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();

		String s2 = new String();
		if (lock != null) {
			s2 += JDEnv.getString("door_lock") + ": " + lock + "  "
					+ JDEnv.getString("state") + ": ";
			if (locked) {
				s2 += JDEnv.getString("door_locked");
			}
			else {
				s2 += JDEnv.getString("door_open");
			}
		}
		else {
			s2 = new String("Unverschlossen");
		}
		p[1] = new Paragraph(s2);
		p[1].setSize(14);
		p[1].setCentered();
		p[1].setBold();

		String itemS = new String("");
		// System.out.println("in Kiste; "+items.size());
		for (int i = 0; i < items.size(); i++) {
			Item it = (items.get(i));
			// System.out.println(it.toString());
			itemS += it.toString() + ",  ";
		}
		p[2] = new Paragraph(itemS);
		p[2].setCentered();
		p[2].setSize(12);

		String s = "";
		if (!items.isEmpty()) {
			int itemPointer = 0;
			s = JDEnv.getString("take") + ": "
					+ items.get(itemPointer).toString();
		}
		p[3] = new Paragraph(s);
		p[3].setCentered();
		p[3].setSize(14);

		return p;
	}

	@Override
	public boolean removeItem(Item i) {
		return items.remove(i);
	}

	@Override
	public boolean takeItem(Item i) {
		items.add(i);
		Item.notifyItem(i, (this));
		return true;
	}

	@Override
	public List<Item> getItems() {
		return items;
	}

	@Override
	public JDPoint getRoomNumber() {
		return location;
	}

	@Override
	public Collection<Position> getInteractionPositions() {
		return Collections.singletonList(this.getRoom().getPositions()[0]);
	}
}
