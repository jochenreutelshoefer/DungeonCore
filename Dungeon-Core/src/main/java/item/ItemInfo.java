/*
 * Created on 26.05.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item;
import item.interfaces.*;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.ItemMemory;
import figure.memory.MemoryObject;
import game.InfoEntity;
import gui.Paragraph;
import gui.Paragraphable;
import spell.TargetScope;

/**
 * 
 * Diese Klasse bietet alle Informationen ueber einen Gegenstand
 * 
 */
public class ItemInfo extends InfoEntity {
	
	protected Item it;
	
	public ItemInfo(Item i, DungeonVisibilityMap map) {
		super(map);
		it = i;
	}
	
	@Override
	public MemoryObject getMemoryObject(FigureInfo info) {
		return it.getMemoryObject(info);
	}
	
	public int getItemKey(){
		return it.getItemKey();
	}
	
	public TargetScope getTargetClass() {
		if(it instanceof UsableWithTarget) {
			return ((UsableWithTarget) it).getTargetScope();
		}
		return null;
	}
	
	public InfoEntity getOwner() {
		return Item.wrappItemOwner(it.getOwner(),map);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemInfo itemInfo = (ItemInfo) o;

		return !(it != null ? !it.equals(itemInfo.it) : itemInfo.it != null);

	}

	@Override
	public int hashCode() {
		return it != null ? it.hashCode() : 0;
	}

	public static ItemInfo makeItemInfo(Item it, DungeonVisibilityMap map)  {
		if(it != null) {
			return new ItemInfo(it,map);
		}
		return null;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if(it == null) {
			return "null";
		}
		return it.toString();
	}
	
	/**
	 * Liefert ob der Gegenstand das Usable-Interface implementiert,
	 * also ob er benutzbar ist.
	 * 
	 * @return Usable
	 */
	public boolean isUsable() {
		return it instanceof Usable;
	}

	public boolean isUsableWithTarget() {
		return it instanceof UsableWithTarget;
	}

	public TargetScope getTargetScope() {
		if(it instanceof UsableWithTarget) {
			return ((UsableWithTarget)it).getTargetScope();
		}
		return null;
	}
	
	public boolean needsTarget() {
		if(this.isUsable()) {
			if(((Usable)it).needsTarget()) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Liefert den zugeordneten Wert des Gegenstandes
	 * 
	 * @return Wert des Gegenstandes
	 */
	public int getWorth() {
		return it.getWorth();
	}
	
	/**
	 * Liefert die Klasse des Gegenstandes
	 * 
	 * @return Klasse des Gegenstandes
	 */
	public Class getItemClass() {
		return it.getClass();
	}
	
	@Override
	public Paragraph[] getParagraphs() {
		return it.getParagraphs();
	}

}
