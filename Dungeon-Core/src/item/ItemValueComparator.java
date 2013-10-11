/*
 * Created on 03.01.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item;
import java.util.*;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ItemValueComparator implements Comparator {
	
	public int compare(Object o1, Object o2) {
		Item item1 = ((Item)o1);
		Item item2 = ((Item)o2);
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
