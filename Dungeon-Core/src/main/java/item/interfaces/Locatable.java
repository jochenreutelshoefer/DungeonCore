package item.interfaces;

import dungeon.JDPoint;

public interface Locatable {

	JDPoint getLocation();

	ItemOwner getOwner();

	
	void setOwner(ItemOwner o);

	
	void getsRemoved();

}
