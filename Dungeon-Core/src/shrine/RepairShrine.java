/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package shrine;

import item.*;
import item.equipment.EquipmentItem;
import item.interfaces.ItemOwner;
import item.interfaces.Usable;

import java.awt.Color;
import java.util.*;

import dungeon.*;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;
import gui.Texts;


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
	public String getStory() {
		return story;
	}
public void metaClick(Figure f) {
		
	}

public boolean needsTarget() {
	return false;
}


	public boolean usableOnce() {
		return false;
	}

	
	public void tellItem(Item i) {
		this.i = i;
	}
	
	public int getShrineIndex() {
		return Shrine.SHRINE_REPAIR;
	}

	
	public Color getColor() {
		return java.awt.Color.black;	
	}
	
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
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_repair_name");
	}

	
	public boolean canBeUsedBy(Figure f) {
		   return false;
	   }
	
	/**
	 * @see Shrine#getStatus()
	 */
	public String getStatus() {
		if(i == null) {
			return JDEnv.getResourceBundle().getString("empty");
		} else {
			return i.toString();
		}
	}

	/**
	 * @see Usable#use(fighter)
	 */
	public boolean use(Figure f,Object target,boolean meta) {

		return false;
	}

}
