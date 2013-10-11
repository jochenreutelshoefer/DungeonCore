/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.ChestMemory;
import game.InfoEntity;
import gui.Paragraph;
import item.Item;
import item.ItemInfo;

import java.util.Iterator;
import java.util.List;

public class ChestInfo extends InfoEntity {
	
	private Chest chest;
	
	public ChestInfo(Chest c,DungeonVisibilityMap stats) {
		super(stats);
		chest = c;
	}
	public boolean hasLock() {
		return chest.hasLock();
	}
	
	public Paragraph[] getParagraphs() {
			return chest.getParagraphs();
	}
	
	public JDPoint getLocation() {
		return chest.getLocation();
	}
	
	public ChestMemory getMemoryObject(FigureInfo info) {
		return (ChestMemory)chest.getMemoryObject(info);
	}
	
	public ItemInfo[] getItems() {
		List l = chest.getItems();
		ItemInfo infos [] = new ItemInfo[l.size()];
		int i = 0;
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			infos [i] = new ItemInfo(element,map);
			i++;
		}
		return infos;
	}

}
