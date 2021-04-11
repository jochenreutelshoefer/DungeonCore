package de.jdungeon.item.interfaces;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import de.jdungeon.dungeon.Position;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;

import java.util.Collection;
import java.util.List;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;

public interface ItemOwner {
	
	boolean takeItem(Item i);
	
	boolean addItems(List<Item> l, ItemOwner donator);
	
	boolean removeItem(Item i);
	
	ItemInfo[] getItemInfos(DungeonVisibilityMap map);
		
	Item unwrapItem(ItemInfo it);
	
	boolean hasItem(Item i);

	JDPoint getRoomNumber();

	Collection<Position> getInteractionPositions();

	Room getRoom();

	List<Item> getItems();
}
