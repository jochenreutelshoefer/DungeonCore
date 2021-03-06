/*
 * Created on 03.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.item;
import java.util.Comparator;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ItemValueComparator implements Comparator<Item> {
	
	@Override
	public int compare(Item o1, Item o2) {
		Item item1 = (o1);
		Item item2 = (o2);
		if(item1.getWorth() > item2.getWorth()) {
			return -1; 
		}
		else if(item1.getWorth() < item2.getWorth()) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
