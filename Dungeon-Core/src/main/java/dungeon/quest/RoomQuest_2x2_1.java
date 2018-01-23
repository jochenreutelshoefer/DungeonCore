/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package dungeon.quest;

import dungeon.generate.DungeonFiller;
import item.Item;
import item.ItemPool;
import item.Key;

import java.util.LinkedList;
import java.util.List;

import dungeon.Chest;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.generate.AbstractDungeonFiller;
import dungeon.util.RouteInstruction;
import shrine.Shrine;

public class RoomQuest_2x2_1 extends RoomQuest {

	private List<Item> items;
	private Shrine shrine ;

	public RoomQuest_2x2_1(JDPoint p, DungeonFiller df, List<Item> items) {
		super(p, df, 2, 2);
		this.items = items;

	}

	public RoomQuest_2x2_1(JDPoint p, DungeonFiller df, List<Item> items, Shrine shrine) {
		this(p, df, items);
		this.shrine = shrine;
	}

	@Override
	public boolean setUp() {
		////System.out.println("setting up roomQuest");
		if (!accessible(rooms[0][0], RouteInstruction.NORTH)) {
			////System.out.println("rq nicht zug�nglich - abbruch!");
			return false;
		}

		Key k = df.getNextKey();
		rooms[0][0].removeDoor(RouteInstruction.WEST, true);
		rooms[0][0].setDoor(df.getDungeon(), RouteInstruction.NORTH, true);
		rooms[0][0].setDoor(df.getDungeon(), RouteInstruction.EAST, true);
		rooms[0][1].removeDoor(RouteInstruction.EAST, true);
		rooms[0][1].removeDoor(RouteInstruction.NORTH, true);
		rooms[0][1].setDoor(df.getDungeon(), RouteInstruction.SOUTH, true);
		rooms[1][1].removeDoor(RouteInstruction.EAST, true);
		rooms[1][1].removeDoor(RouteInstruction.SOUTH, true);
		rooms[1][1].setDoor(df.getDungeon(), RouteInstruction.WEST, true);
		rooms[1][0].removeDoor(RouteInstruction.SOUTH, true);
		rooms[1][0].removeDoor(RouteInstruction.WEST, true);
		Door keyDoor = new Door(rooms[1][0], rooms[0][0], k);
		rooms[1][0].setDoor(keyDoor, RouteInstruction.NORTH, true);
		Chest ch1 = new Chest(k);
		List<Item> itemsL = new LinkedList<Item>();
		if (items.size() >= 2) {
			itemsL.add(items.remove(0));
			itemsL.add(items.remove(0));
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
		Chest ch2 = new Chest(itemsL, k);

		rooms[1][1].setChest(ch2);
		rooms[1][0].setChest(ch1);
		rooms[1][0].figureEnters(df.getSmallMonster(800),0);
		if(shrine != null) {
			rooms[1][0].setShrine(shrine);
		}
		for (int i = 0; i < 3; i++) {
			rooms[0][1].figureEnters(df.getBigMonster(1200),0);
		}

		return true;

	}

}
