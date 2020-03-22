/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package shrine;

import dungeon.RoomEntity;
import figure.Figure;
import game.JDEnv;
import item.Item;
import item.equipment.EquipmentItem;
import item.interfaces.ItemOwner;
import item.interfaces.Usable;

import java.util.List;

import util.JDColor;
import dungeon.Room;


public class RepairShrine extends Shrine /**implements itemReceiver, itemOwner*/{
	
	double rate;

	
	Item i;

	
	int rounds;

	double rest;
	//String story;
	//room location; //schon in shrine definiert
	
	
	public RepairShrine(Room p, double rate) {
		super(p);
		this.rate = rate;
		this.location = p;
		rounds = 0;
		rest = 0;
		story = JDEnv.getResourceBundle().getString("see_repair_shrine");
	} 
	public RepairShrine(double rate) {
		super();
		this.rate = rate;
		rounds = 0;
		rest = 0;
		story = JDEnv.getResourceBundle().getString("see_repair_shrine");
	} 
	@Override
	public String getStory() {
		return story;
	}
public void metaClick(Figure f) {
		
	}

@Override
public boolean needsTarget() {
	return false;
}


	@Override
	public boolean usableOnce() {
		return false;
	}

	
	public void tellItem(Item i) {
		this.i = i;
	}
	
	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_REPAIR;
	}

	
	@Override
	public JDColor getColor() {
		return JDColor.black;
	}
	
	@Override
	public String getText() {
		return toString()+"\n"+JDEnv.getResourceBundle().getString("shrine_repair_text");	
	}
	
	public boolean addItem(Item a, ItemOwner o) {
		if(i == null) {
			i = a;
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @see Shrine#turn(int)
	 * 
	 */
	@Override
	public void turn(int round) {
		//if(i != null) {
		rounds++;
		repair();
		//}

	}

	private void repair() {
			
			double d = ((rounds * rate) +rest );
			if( d > 1) {
				d--;
				rest = d;
				if(location == null) {
					//System.out.println("repair-shrine keine location gesetzt!");	
				}
				List<Item> itemList = location.getItems();
				for(int i = 0; i < itemList.size(); i++) {
					Item it = itemList.get(i);
					if(it instanceof EquipmentItem) {
						((EquipmentItem)it).repair(1);
					}
					
				}
				
				rounds = 0;
				repair();
			}
			
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_repair_name");
	}

	
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return false;
	   }
	
	@Override
	public String getStatus() {
		if(i == null) {
			return JDEnv.getResourceBundle().getString("empty");
		} else {
			return i.toString();
		}
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		return false;
	}

}
