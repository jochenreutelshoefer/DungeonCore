/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package de.jdungeon.dungeon.quest;

import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemPool;
import de.jdungeon.item.Key;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.location.Location;
import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.monster.Monster;
public class RoomQuest_1x1_1 extends RoomQuest {
	
	private final List<Item> sector_items;
	private boolean locked = false;
	/**
	 * 
	 * @uml.property name="s"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Location s;
	
	
	public RoomQuest_1x1_1(JDPoint p, DungeonFiller df, boolean locked,
						   boolean chest, Location s, List<Item> restItems) {
		super(p,df,1,1);
		this.s = s;
		sector_items = restItems;
		this.locked = locked;
		
	}
	


	/**
	 * @see RoomQuest#setUp()
	 */
	@Override
	public boolean setUp() {
		//System.out.println("setting up roomQuest");
		int dir = (int)(Math.random()*4) +1;
		
		if (!accessible(rooms[0][0], dir)) {
			//System.out.println("rq nicht zug�nglich - abbruch!");
			return false;
		}

		for(int i = 1; i < 5; i++) {
			if(i != dir) {
				rooms[0][0].removeDoor(i,true);	
			}
			else {
				Key k  = df.getNextKey();
				if(k == null) {
					//System.out.println("k ist null !");
				}
				Room entrance = df.getDungeon().getRoomAt(rooms[0][0],
						RouteInstruction.direction(dir));
				if(entrance == null) {
					//System.out.println("entrance ist null !");
				}
				Door d = null;
				if(locked) {
				d = new Door(rooms[0][0],entrance, k);
				df.itemToDistribute(k);
				}else {
					d = new Door(rooms[0][0],entrance);
				}
				rooms[0][0].setDoor(d, dir,true);
			}
				
		}
		
		List<Item> itemsL = new LinkedList<Item>();
		if (sector_items.size() >= 2) {
			itemsL.add(sector_items.remove((int)(Math.random()*sector_items.size())));
			//itemsL.add(sector_items.remove((int)(Math.random()*sector_items.size())));
		} else {
			Item it1 = ItemPool.getRandomItem(25, 1);
			Item it2 = ItemPool.getRandomItem(30, 1.5);
			if (it1 != null) {
				itemsL.add(it1);
			}
			if (it2 != null) {
				itemsL.add(it2);
			}
		}
		Chest ch2 = new Chest(itemsL);
		if(s != null) {
			rooms[0][0].setLocation(s,true);
		}
		rooms[0][0].setChest(ch2);
		Monster m = df.getBigMonster(1000);
		df.equipMonster(m,1);
		rooms[0][0].figureEnters(m,0, -1);
		return true;
	}

}
