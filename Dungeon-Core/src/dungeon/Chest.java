package dungeon;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.memory.ChestMemory;
import figure.percept.Percept;
import figure.percept.TakePercept;
import game.InfoEntity;
import game.InfoProvider;
import game.JDEnv;
import gui.Paragraph;
import gui.Paragraphable;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.interfaces.ItemOwner;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import util.JDColor;

/**
 * Eine Truhe steht immer fest in einem Raum. Truhen koennen Gegenstaende
 * enthalten die herausgenommen ein hineingelegt werden koennen. Truhen koennen
 * verschlossen sein.
 * 
 * 
 */
public class Chest implements ItemOwner, Paragraphable, InfoProvider {

	private final LinkedList items;

	private String lock = new String("unlockable");

	private boolean locked = true;

	private int itemPointer = 0;

	private JDPoint location;
	private Room r;

	public Chest() {

		items = new LinkedList();
		// this.game = game;

	}

	public void setRoom(Room r) {
		this.r = r;
	}

	public ChestMemory getMemoryObject(FigureInfo info) {
		return new ChestMemory(this, info);
	}

	public Chest(Item i) {

		items = new LinkedList();
		this.takeItem(i, null);
		// this.game = game;

	}

	@Override
	public Item getItemNumber(int i) {
		return (Item) items.get(i);
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
	public Item getItem(ItemInfo it) {
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			if (ItemInfo.makeItemInfo(element, null).equals(it)) {
				return element;
			}

		}
		return null;
	}

	public Chest(String lock) {
		items = new LinkedList();
		this.lock = lock;
	}

	public Chest(LinkedList list) {
		// items = list;
		items = new LinkedList();
		for (int i = 0; i < list.size(); i++) {
			this.takeItem((Item) list.get(i), null);
		}
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo((Item) items.get(i), map);
		}
		return array;
	}

	@Override
	public boolean addItems(List l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (Item) (l.get(i));
			this.takeItem(it, o);
		}
		return true;
	}

	public Chest(LinkedList list, String lock) {
		// items = list;
		items = new LinkedList();
		for (int i = 0; i < list.size(); i++) {
			this.takeItem((Item) list.get(i), null);
		}
		this.lock = lock;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean b) {
		locked = b;
	}

	public void setLocation(JDPoint p) {
		location = p;
	}

	public boolean hasLock() {
		if (lock.equals("unlockable")) {
			return false;
		}
		return true;
	}

	public boolean lock(Key k) {
		if (k.getType().equals(lock)) {
			locked = !locked;

			return true;
		}
		return false;
	}

	public void clicked(Figure f, boolean right) {

		if (lock.equals("unlockable")) {
			if (right) {
				if (items.size() > 0) {

					Item it = ((Item) items.get(itemPointer));
					// items.remove(it);
					itemPointer = 0;
					boolean taken = f.takeItem(it, this);
					if (taken) {
						Percept p = new TakePercept(f, it);
						f.getRoom().distributePercept(p);
					}
				}
			} else {
				if (items.size() > 0) {
					itemPointer = (itemPointer + 1) % items.size();
				}
			}
		}
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public String getLock() {
		return lock;
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
		if (!lock.equals("unlockable")) {
			s2 += JDEnv.getString("door_lock") + ": " + lock + "  "
					+ JDEnv.getString("state") + ": ";
			if (locked) {
				s2 += JDEnv.getString("door_locked");
			} else {
				s2 += JDEnv.getString("door_open");
			}
		} else {
			s2 = new String();
		}
		p[1] = new Paragraph(s2);
		p[1].setSize(14);
		p[1].setCentered();
		p[1].setBold();

		String itemS = new String("");
		// System.out.println("in Kiste; "+items.size());
		for (int i = 0; i < items.size(); i++) {
			Item it = ((Item) items.get(i));
			// System.out.println(it.toString());
			itemS += it.toString() + ",  ";
		}
		p[2] = new Paragraph(itemS);
		p[2].setCentered();
		p[2].setSize(12);

		String s = "";
		if (items.size() > 0) {
			s = JDEnv.getString("take") + ": "
					+ ((Item) items.get(itemPointer)).toString();
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
	public boolean takeItem(Item i, ItemOwner o) {

		items.add(i);
		Item.notifyItem(i, (this));
		return true;
	}

	public LinkedList getItems() {
		return items;
	}

	@Override
	public JDPoint getLocation() {
		return location;
	}

}
