package item;

import dungeon.Position;
import dungeon.RoomEntity;
import figure.attribute.Attribute;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Weapon;
import item.interfaces.ItemOwner;
import item.interfaces.LocatableItem;
import item.quest.Rune;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import shrine.Location;
import shrine.ShrineInfo;
import util.JDColor;
import dungeon.Chest;
import dungeon.ChestInfo;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.attribute.ItemModification;
import figure.attribute.ModifierI;
import figure.memory.ItemMemory;
import game.InfoEntity;
import game.InfoProvider;
import gui.Paragraph;
import gui.Paragraphable;

/**
 * Superklasse aller Gegenstaende. 
 * Jedem Gegenstand wird ein Wert zugeordnet,
 * je nach Nuetzlichkeit, Seltenheit, Eigenschaften etc.
 *
 */
public abstract class Item implements ModifierI, Paragraphable, InfoProvider, LocatableItem, Serializable, RoomEntity {

	public static final int ITEM_KEY_UNDEFINDED = -1;
	public static final int ITEM_KEY_ATTRPOTION = 1;
	public static final int ITEM_KEY_HEALPOTION = 2;
	
	protected int worth;

	protected String Type;

	protected ItemOwner owner;
	
	protected boolean magic = false;

	protected boolean unique = false;

	
	protected List modifications = new LinkedList();

	
	protected String name = "";



	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new ItemInfo(this,map); 	
	}

	@Deprecated
	public int getItemKey() {
		return Item.ITEM_KEY_UNDEFINDED;
	}
	
	public ItemMemory getMemoryObject(FigureInfo info) {
		return new ItemMemory(this);
	}
	
	public static InfoEntity wrappItemOwner(ItemOwner o,DungeonVisibilityMap map) {
		if(o instanceof Room) {
			return new RoomInfo((Room)o,map);
		}
		if(o instanceof Figure) {
			return FigureInfo.makeFigureInfo((Figure)o,map);
		}
		if(o instanceof Location) {
			return new ShrineInfo((Location)o,map);
		}
		if(o instanceof Chest) {
			return new ChestInfo((Chest)o,map);
		}
		
		System.out.println("wrappItemOwner failed! Unknown OwnerClass!: "+o.getClass().toString());
		return null;
	}
	
	@Override
	public JDPoint getLocation () {
		return owner.getLocation();
	}
	
	@Override
	public ItemOwner getOwner() {
		return owner;
	}
	
	@Override
	public void setOwner(ItemOwner o) {
		owner = o;
	}
	
	@Override
	public void getsRemoved() {
		owner = null;
	}
	
	public Item(int value, boolean magic) {
		worth = value;
		if (magic) {
			this.magic = true;
			
		}
	}

	public Item(int value) {
		this(value, false);
	}

	
	public void setUnique() {
		unique = true;
	}
	
	public static int calcValueSum(List<Item> l) {
		int val = 0;
		for(int i = 0 ; i < l.size(); i++ ) {
			Item it = (l.get(i));
			val+= it.getWorth();
		}
		
		return val;
		
	}

	@Override
	public Collection<Position> getInteractionPositions() {
		if(owner != null) {
			return owner.getInteractionPositions();
		}
		return null;
	}

	public static void notifyItem(Item i, ItemOwner newOwner ) {
		if (i instanceof Rune) {
			int k = 0;
			k++;
		}
		if(i instanceof LocatableItem) {
			((LocatableItem)i).setOwner(newOwner);
		}	
	}
	
	@Override
	public Paragraph[] getParagraphs() {
		Paragraph []p = new Paragraph[3];
		p[0] = new Paragraph(getName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new JDColor(200,40,30));
		p[0].setBold();
		
		p[1] = new Paragraph(toString());
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();
		
		p[2] = new Paragraph(getText());
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);
		
		return p;
	}

	
	public String getName() {
		return name;

	}
	
	public int getWorth() {
		return worth;
	}

	
	public abstract String getText();


	public static Item newItem(int value) {
		int i = (int) (Math.random() * 100);

		if (i < 30) {
			return Weapon.newRandomWeapon(value,false);
		} else if (i < 35) {
			return new Shield(value,false);
		} else if (i < 40) {
			return new Armor(value, false);
		} else if (i < 45) {
			return new Helmet(value, false);
		} else /**if(i < 100)**/
			{
			AttrPotion[] potion = ItemPool.getElexirs(1, value);
			AttrPotion a = potion[0];
			return a;
		}
	}


	@Override
	public List<ItemModification> getRemoveModifications() {

		//kreirt die umgekehrten Modifikationen
		int i = modifications.size();
		List<ItemModification> neg = new LinkedList<>();
		for (int j = 0; j < i; j++) {
			double k = ((ItemModification) (modifications.get(j))).getValue();
			Attribute.Type s = ((ItemModification) (modifications.get(j))).getAttribute();
			neg.add(new ItemModification(s, k * (-1)));
		}
		return neg;
	}

	
	public boolean isMagic() {
		return magic;
	}

	
	public void setMagic(boolean magic) {
		this.magic = magic;
	}

	
	@Override
	public List getModifications() {
		return modifications;
	}

	
	public void setModifications(LinkedList modifications) {
		this.modifications = modifications;
	}

	/**
	 * Sets the worth.
	 * @param worth The worth to set
	 * 
	 */
	public void setWorth(int worth) {
		this.worth = worth;
	}

}
