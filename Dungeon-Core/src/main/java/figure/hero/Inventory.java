package figure.hero;

import item.Bunch;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Weapon;
import item.interfaces.ItemOwner;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.JDPoint;
import dungeon.Room;
import fight.Slap;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.attribute.Attribute;
import figure.attribute.ModifierI;
import figure.monster.Ghul;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import figure.percept.TextPercept;

/**
 * Inventar eines Helden. Enhaelt bis zu 3 Waffen, 3 Schilde, 3 Ruestungen und 3
 * Helme, wobei jeweils immer eines aktiv ist. Enthaelt des Weiteren eine Liste
 * (unbeschraenkt) von (nicht-Ausruestungs-) Gegenstaenden
 */
public class Inventory implements Serializable {

	private final Figure owner;

	private final List<Item> items;

	private final Weapon[] weapons;

	private final Armor[] armors;

	private final Helmet[] helmets;

	private final Shield[] shields;

	private int weaponIndex = 0;

	private int armorIndex = 0;

	private int helmetIndex = 0;

	private int shieldIndex = 0;

	public Inventory(int weaponsI, int armorsI, int helmetsI, int shieldsI,
			Figure f) {
		owner = f;
		weapons = new Weapon[weaponsI];
		armors = new Armor[armorsI];
		helmets = new Helmet[helmetsI];
		shields = new Shield[shieldsI];

		items = new LinkedList<Item>();

	}

	public Room getRoom() {
		return owner.getRoom();
	}

	public boolean canTakeItem(Item i) {
		if (i instanceof Weapon) {
			return weapons[0] == null || weapons[1] == null
					|| weapons[2] == null;
		}
		if (i instanceof Armor) {
			return armors[0] == null || armors[1] == null || armors[2] == null;
		}
		if (i instanceof Shield) {
			return shields[0] == null || shields[1] == null
					|| shields[2] == null;
		}
		if (i instanceof Helmet) {
			return helmets[0] == null || helmets[1] == null
					|| helmets[2] == null;
		}
		return true;

	}

	public boolean addItems(List<Item> l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (l.get(i));
			this.takeItem(it, o);
		}
		return true;
	}

	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		List<Item> syncList = Collections.synchronizedList(items);
		ItemInfo[] array = new ItemInfo[syncList.size()];
		for (int i = 0; i < syncList.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(syncList.get(i),map);
		}
		return array;
	}

	public float give_Weapon_on_Type_Modifier(Figure m) {
		if(getWeapon1() == null) {
			return 1;
		}
		if (m instanceof Wolf)
			return 1 + ((float) getWeapon1()
					.getWolfModifier() / 100);
		if (m instanceof Spider)
			return 1 + ((float) getWeapon1()
					.getBearModifier() / 100);
		if (m instanceof Skeleton)
			return 1 + ((float) getWeapon1()
					.getSkeletonModifier() / 100);
		if (m instanceof Ghul)
			return 1 + ((float) getWeapon1()
					.getGhulModifier() / 100);
		if (m instanceof Orc)
			return 1 + ((float) getWeapon1()
					.getOrcModifier() / 100);
		if (m instanceof Ogre)
			return 1 + ((float) getWeapon1()
					.getOgreModifier() / 100);
		else
			return 1;
	}

	public JDPoint getLocation() {
		return owner.getRoomNumber();
	}

	public Item getItem(ItemInfo it) {
		for (Iterator<Item> iter = items.iterator(); iter.hasNext();) {
			Item element = iter.next();
			if (element != null) {
				if (ItemInfo.makeItemInfo(element,null).equals(it)) {
					return element;
				}
			}

		}
		return null;
	}

	public int getAllArmor(Slap s) {
		Figure m = s.getActor();
		int a = 0;
		if (getArmor1() != null) {
			getArmor1().hit(s.getValueStandard());
			a += getArmor1().getArmorValue();
		}
		if (getHelmet1() != null) {
			getHelmet1().hit(s.getValueStandard());
			a += getHelmet1().getArmorValue();
			
		}
		return a + owner.getCharacter().giveType_skill(m);
	}

	public Weapon getWeapon1() {
		return weapons[weaponIndex];
	}

	public Item getItemNumber(int i) {
		return items.get(i);
	}

	public List<Item> getWeaponList() {

		List<Item> l = new LinkedList<Item>();
		for (int i = 0; i < 3; i++) {
			if (weapons[i] != null) {
				l.add(weapons[i]);
			}
		}

		return l;
	}

	/**
	 * Describe <code>getArmor1</code> method here.
	 * 
	 * @return an <code>an_armor</code> value
	 */
	public Armor getArmor1() {

		return armors[armorIndex];

	}

	/**
	 * Describe <code>getHelmet</code> method here.
	 * 
	 * @return an <code>helmet</code> value
	 */
	public Helmet getHelmet1() {
		return helmets[helmetIndex];

	}

	/**
	 * Describe <code>getshield</code> method here.
	 * 
	 * @return an <code>shield</code> value
	 */
	public Shield getShield1() {

		return shields[shieldIndex];

	}

	/**
	 * Man kann ihm hier die primaerwaffe geben, falls er noch keine hat.
	 * 
	 * @param w
	 *            a <code>weapon</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean take_weapon1(Weapon w) {

		boolean bool = true;
		int k = -1;
		if (weapons[0] == null) {
			weapons[0] = w;
			k = 0;
		} else if (weapons[1] == null) {
			weapons[1] = w;
			k = 1;

		} else if (weapons[2] == null) {
			weapons[2] = w;
			k = 2;
		} else {
			bool = false;
		}
		if (bool) {
			if (weaponIndex == k) {
				if (w.isMagic()) {
					owner.getCharacter()
							.makeModifications(w.getModifications());
				}
			}
		}
		return bool;

	}


	/**
	 * Man kann ihm hier eine Ruestung geben
	 * 
	 * @param a
	 *            an <code>an_armor</code> value
	 * @return a <code>boolean</code> value
	 */

	public boolean take_armor1(Armor a) {
		boolean bool = true;
		int k = -1;
		if (armors[0] == null) {
			armors[0] = a;
			k = 0;
		} else if (armors[1] == null) {
			armors[1] = a;
			k = 1;
		} else if (armors[2] == null) {
			armors[2] = a;
			k = 2;
		} else {
			bool = false;
		}
		if (bool) {
			if (armorIndex == k) {
				if (a.isMagic()) {
					owner.getCharacter()
							.makeModifications(a.getModifications());
				}
			}
		}
		return bool;
	}

	/**
	 * Man kann ihm hier den Helm aufsetzen
	 * 
	 * @param h
	 *            a <code>helmet</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean take_helmet1(Helmet h) {
		boolean bool = true;
		int k = -1;
		if (helmets[0] == null) {
			helmets[0] = h;
			k = 0;
		} else if (helmets[1] == null) {
			helmets[1] = h;
			k = 1;
		} else if (helmets[2] == null) {
			helmets[2] = h;
			k = 2;
		} else {
			bool = false;
		}
		if (bool) {
			if (helmetIndex == k) {
				if (h.isMagic()) {
					owner.getCharacter()
							.makeModifications(h.getModifications());
				}
			}
		}
		return bool;

	}

	/**
	 * Man kann ihm hier Schild aufsetzen
	 * 
	 *            a <code>shield</code> value
	 * @return a <code>boolean</code> value
	 */
	public boolean take_shield1(Shield s) {
		int k = -1;
		boolean bool = true;
		if (shields[0] == null) {
			shields[0] = s;
			k = 0;
		} else if (shields[1] == null) {
			shields[1] = s;
			k = 1;
		} else if (shields[2] == null) {
			shields[2] = s;
			k = 2;
		} else {
			bool = false;
		}
		if (bool) {
			if (shieldIndex == k) {
				if (s.isMagic()) {
					owner.getCharacter()
							.makeModifications(s.getModifications());
				}
			}
		}
		return bool;
	}

	public boolean takeItem(Item ding, ItemOwner o) {
		if (ding instanceof Weapon) {
			boolean taken = take_weapon1((Weapon) ding);
			if (taken) {
				Item.notifyItem(ding, this.owner);
				if (o != null) {
					o.removeItem(ding);
				}
				return true;
			} else {
				return false;
			}
		} else if (ding instanceof Armor) {
			boolean taken = take_armor1((Armor) ding);
			if (taken) {
				Item.notifyItem(ding, this.owner);
				if (o != null)
					o.removeItem(ding);
				return true;
			} else {
				return false;
			}
		} else if (ding instanceof Shield) {
			boolean taken = take_shield1((Shield) ding);
			if (taken) {
				Item.notifyItem(ding, this.owner);
				if (o != null)
					o.removeItem(ding);
				return true;
			} else {
				return false;
			}
		} else if (ding instanceof Helmet) {
			boolean taken = take_helmet1((Helmet) ding);
			if (taken) {
				Item.notifyItem(ding, this.owner);
				if (o != null)
					o.removeItem(ding);
				return true;
			} else {
				return false;
			}
		} else if (ding instanceof DustItem) {
			Attribute dust = owner.getCharacter().getDust();
			double basic = dust.getBasic();
			double value = dust.getValue();
			double diff = basic - value;
			if (diff <= 0) {
				//System.out.println("dust voll --> return false");
				return false;
			}
			if (diff > ((DustItem) ding).getCount()) {
				// System.out.println("viel platz");
				dust.modValue(((DustItem) ding).getCount());
				if (o != null)
					o.removeItem(ding);

			} else {
				// System.out.println("rest zur�ck");
				double rest = ((DustItem) ding).getCount() - (basic - value);
				DustItem j = new DustItem(rest);
				dust.setValue(dust.getBasic());
				if (o != null) {
					o.removeItem(ding);
					if (rest > 0.5) {
						o.takeItem(j);
					}
				}

			}
			return true;
		} else {

			Item.notifyItem(ding, this.owner);
			/*
			if (ding instanceof Key) {

				Bunch b = getBunch();
				if (b != null) {
					b.addKey(((Key) ding));
				} else {
					items.add(ding);
				}
				if (o != null)
					o.removeItem(ding);
			} else {

			 */
				items.add(ding);

				if (o != null)
					o.removeItem(ding);
			//}
			return true;
		}
	}

	/*
	private Bunch getBunch() {
		for (int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			if (it instanceof Bunch) {
				return (Bunch) it;
			}
		}
		return null;

	}
	*/


	public boolean removeItem(Item i) {
		return items.remove(i);
	}

	public boolean giveAwayItem(Item it, ItemOwner o) {
		boolean no = (it == null);

		if (!no) {
			if (it instanceof Weapon) {
				for (int i = 0; i < 3; i++) {
					if (getWeapon(i) == it) {
						if (o != null) {
							o.takeItem(it);
						}
						if ((it.isMagic()) && (weaponIndex == i)) {

							owner.getCharacter().makeModifications(
									((ModifierI) (getWeapon1()))
											.getRemoveModifications());
						}
						this.weapons[i] = null;
						return true;
					}
				}
			} else if (it instanceof Armor) {
				for (int i = 0; i < 3; i++) {
					if (getArmor(i) == it) {
						if (o != null) {
							o.takeItem(it);
						}
						if ((it.isMagic()) && (armorIndex == i)) {
							owner.getCharacter().makeModifications(
									((ModifierI) (getArmor1()))
											.getRemoveModifications());
						}
						this.armors[i] = null;
						return true;
					}
				}
			} else if (it instanceof Shield) {
				for (int i = 0; i < 3; i++) {
					if (getShield(i) == it) {
						if (o != null) {
							o.takeItem(it);
						}
						if ((it.isMagic()) && (shieldIndex == i)) {
							owner.getCharacter().makeModifications(
									((ModifierI) (getShield1()))
											.getRemoveModifications());
						}
						this.shields[i] = null;
						return true;
					}
				}
			} else if (it instanceof Helmet) {
				for (int i = 0; i < 3; i++) {
					if (getHelmet(i) == it) {
						if (o != null) {
							o.takeItem(it);
						}
						if ((it.isMagic()) && (helmetIndex == i)) {
							owner.getCharacter().makeModifications(
									((ModifierI) (getHelmet1()))
											.getRemoveModifications());
						}
						this.helmets[i] = null;
						return true;
					}
				}
			} else {
				List<Item> l = getItems();
				int k = l.size();

				for (int i = 0; i < k; i++) {
					if (it == (l.get(i))) {
						if (o != null) {
							o.takeItem(it);
						}
						l.remove(it);

						return true;
					}
				}
			}
		}

		return false;

	}

	public void layDown(Item it) {
		giveAwayItem(it, owner.getRoom());
	}

	public void giveArmor(int i, Armor w) {
		armors[i] = w;
	}

	public void giveHelmet(int i, Helmet w) {
		helmets[i] = w;
	}

	public void giveShield(int i, Shield w) {
		shields[i] = w;
	}

	public void addItem(Item i) {
		items.add(i);
	}

	public Shield getShield(int i) {
		return shields[i];
	}

	public Armor getArmor(int i) {
		return armors[i];
	}

	public Weapon getWeapon(int i) {
		return weapons[i];
	}

	public Helmet getHelmet(int i) {
		return helmets[i];
	}

	/**
	 * 
	 * @uml.property name="weaponIndex"
	 */
	public void setWeaponIndex(int i) {
		if (i != getWeaponIndex()) {
			if ((i < 3) && (i >= 0)) {
				if (getWeapon1() != null) {

					if (getWeapon1().isMagic()) {
						owner.getCharacter().makeModifications(
								((ModifierI) (getWeapon1()))
										.getRemoveModifications());
					}
				}
				if (getWeapon(i) != null) {

					if (getWeapon(i).isMagic()) {
						owner.getCharacter().makeModifications(
								((ModifierI) getWeapon(i)).getModifications());
					}
				}
				weaponIndex = i;
			} else {
				// System.out.println("Illegal Weapon Index!");
			}
		}
	}

	/**
	 * 
	 * @uml.property name="armorIndex"
	 */
	public void setArmorIndex(int i) {
		if ((i < 3) && (i >= 0)) {
			if (getArmor1() != null) {

				if (getArmor1().isMagic()) {
					owner.getCharacter().makeModifications(
							((ModifierI) (getArmor1()))
									.getRemoveModifications());
				}
			}
			if (getArmor(i) != null) {

				if (getArmor(i).isMagic()) {
					owner.getCharacter().makeModifications(
							((ModifierI) getArmor(i)).getModifications());
				}
			}
			armorIndex = i;
		} else {
			// System.out.println("Illegal Armor Index!");
		}
	}

	/**
	 * 
	 * @uml.property name="shieldIndex"
	 */
	public void setShieldIndex(int i) {
		if ((i < 3) && (i >= 0)) {
			if (getShield1() != null) {

				if (getShield1().isMagic()) {
					owner.getCharacter().makeModifications(
							((ModifierI) (getShield1()))
									.getRemoveModifications());
				}
			}
			if (getShield(i) != null) {

				if (getShield(i).isMagic()) {
					owner.getCharacter().makeModifications(
							((ModifierI) getShield(i)).getModifications());
				}
			}
			shieldIndex = i;
		} else {
			// System.out.println("Illegal Shield Index!");
		}
	}

	/**
	 * 
	 * @uml.property name="helmetIndex"
	 */
	public void setHelmetIndex(int i) {
		if ((i < 3) && (i >= 0)) {
			if (getHelmet1() != null) {

				if (getHelmet1().isMagic()) {
					owner.getCharacter().makeModifications(
							((ModifierI) (getHelmet1()))
									.getRemoveModifications());
					// System.out.println("helmet remove");
				}
			}
			if (getHelmet(i) != null) {

				if (getHelmet(i).isMagic()) {
					owner.getCharacter().makeModifications(
							((ModifierI) getHelmet(i)).getModifications());
					// System.out.println("helmet plug");
				}
			}
			helmetIndex = i;
		} else {
			// System.out.println("Illegal Helmet Index!");
		}
	}

	/**
	 * Returns the armorIndex.
	 * 
	 * @return int
	 * 
	 * @uml.property name="armorIndex"
	 */
	public int getArmorIndex() {
		return armorIndex;
	}

	/**
	 * Returns the armors.
	 * 
	 * @return an_armor[]
	 * 
	 * @uml.property name="armors"
	 */
	public Armor[] getArmors() {
		return armors;
	}

	/**
	 * Returns the helmetIndex.
	 * 
	 * @return int
	 * 
	 * @uml.property name="helmetIndex"
	 */
	public int getHelmetIndex() {
		return helmetIndex;
	}

	/**
	 * Returns the helmets.
	 * 
	 * @return helmet[]
	 * 
	 * @uml.property name="helmets"
	 */
	public Helmet[] getHelmets() {
		return helmets;
	}

	/**
	 * Returns the items.
	 * 
	 * @return LinkedList
	 * 
	 * @uml.property name="items"
	 */
	public List<Item> getItems() {
		return items;
	}

	/**
	 * Returns the shieldIndex.
	 * 
	 * @return int
	 * 
	 * @uml.property name="shieldIndex"
	 */
	public int getShieldIndex() {
		return shieldIndex;
	}

	/**
	 * Returns the shields.
	 * 
	 * @return shield[]
	 * 
	 * @uml.property name="shields"
	 */
	public Shield[] getShields() {
		return shields;
	}

	/**
	 * Returns the weaponIndex.
	 * 
	 * @return int
	 * 
	 * @uml.property name="weaponIndex"
	 */
	public int getWeaponIndex() {
		return weaponIndex;
	}

	/**
	 * Returns the weapons.
	 * 
	 * @return weapon[]
	 * 
	 * @uml.property name="weapons"
	 */
	public Weapon[] getWeapons() {
		return weapons;
	}

	public List<Item> getUnusedItems() {
		List<Item> things = new LinkedList<Item>(items);

		for (int i = 0; i < 3; i++) {

			Weapon w = weapons[i];
			if (weaponIndex != i && !(w == null)) {
				things.add(w);
			}
			Item a = armors[i];

			if (armorIndex != i && !(a == null)) {
				things.add(a);
			}
			Item h = helmets[i];
			if (!(h == null)) {

				things.add(h);
			}
			Item s = shields[i];
			if (!(s == null)) {
				things.add(s);
			}
		}
		return things;
	}

	public void payRel(double d) {
		List<Item> things = getUnusedItems();
		if (d > 0) {
			((Hero) owner).tellPercept(new TextPercept("Panisch fliehend verlierst Du:", -1));
		}

		for (int i = 0; i < things.size(); i++) {
			if (Math.random() < d) {
				Item it = ((things.get(i)));
				((Hero) owner).tellPercept(new TextPercept(it.toString(), -1));
				this.layDown(it);
			}
		}
	}

	/**
	 * Method getAllItems.
	 * 
	 * @return LinkedList
	 */
	public List<Item> getAllItems() {
		List<Item> l = new LinkedList<Item>(items);

		for (int i = 0; i < 3; i++) {

			if (weapons[i] != null) {
				l.add(weapons[i]);
			}

			if (armors[i] != null) {

				l.add(armors[i]);
			}
			Item h = helmets[i];

			if (h != null) {
				l.add(h);
			}
			Item s = shields[i];

			if (s != null) {
				l.add(s);
			}
		}
		return l;
	}

	public boolean hasItem(Item i) {
		for (Item item : items) {
			if(item.equals(i)) {
				return true;
			}
		}

		boolean found = checkArray(weapons, i);
		if(found) return true;

		found = checkArray(armors, i);
		if(found) return true;

		found = checkArray(helmets, i);
		if(found) return true;

		found = checkArray(shields, i);
		if(found) return true;

		return false;
	}

	private boolean checkArray(Object[] weapons, Item i) {
		for (Object weapon : weapons) {
			if(i.equals(weapon)) {
				return true;
			}
		}
		return false;
	}

	public void clearKeys() {
		Iterator<Item> itemIterator = items.iterator();
		while(itemIterator.hasNext()) {
			Item next = itemIterator.next();
			if(next instanceof Key) {
				itemIterator.remove();
			}
		}
	}
}
