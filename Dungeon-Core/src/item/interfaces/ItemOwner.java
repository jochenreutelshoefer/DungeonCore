package item.interfaces;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import figure.DungeonVisibilityMap;
import item.Item;
import item.ItemInfo;

import java.util.List;

import dungeon.JDPoint;
import dungeon.Room;

public interface ItemOwner {
	
	public boolean takeItem(Item i);
	
	public boolean addItems(List<Item> l, ItemOwner donator);
	
	public boolean removeItem(Item i);
	
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map);
		
	public Item getItem(ItemInfo it);
	
	public Item getItemNumber(int k);

	public JDPoint getLocation();

	public Room getRoom();
	
	

	
}
