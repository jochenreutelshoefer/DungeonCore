package item.interfaces;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import figure.DungeonVisibilityMap;
import game.InfoEntity;
import item.Item;
import item.ItemInfo;

import java.util.Collection;
import java.util.List;

import dungeon.JDPoint;
import dungeon.Room;

public interface ItemOwner {
	
	boolean takeItem(Item i);
	
	boolean addItems(List<Item> l, ItemOwner donator);
	
	boolean removeItem(Item i);
	
	ItemInfo[] getItemInfos(DungeonVisibilityMap map);
		
	Item getItem(ItemInfo it);
	
	boolean hasItem(Item i);

	JDPoint getLocation();

	Collection<Position> getInteractionPositions();

	Room getRoom();

	List<Item> getItems();
}
