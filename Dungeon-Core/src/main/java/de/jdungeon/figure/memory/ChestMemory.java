package de.jdungeon.figure.memory;

import de.jdungeon.item.Item;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.figure.FigureInfo;

public class ChestMemory extends MemoryObject {
	
	private List<ItemMemory> items = new LinkedList<ItemMemory>();
	
	public ChestMemory(Chest c,FigureInfo info) {
		List l = c.getItems();
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			items.add(element.getMemoryObject(info));
		}
		
	}

}
