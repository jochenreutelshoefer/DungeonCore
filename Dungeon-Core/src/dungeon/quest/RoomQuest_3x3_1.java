/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package dungeon.quest;

import item.Item;
import item.ItemPool;
import item.quest.Rune;

import java.util.LinkedList;
import java.util.List;

import dungeon.Chest;
import dungeon.JDPoint;
import dungeon.generate.DungeonFiller;
import dungeon.util.RouteInstruction;
public class RoomQuest_3x3_1 extends RoomQuest {
	
	List<Item> takeItems;
	
	int [][] doors = { {0,1,1,0},
						{1,1,1,1},
						{0,0,1,1},
						{1,1,1,1},
						{1,1,1,1},
						{1,1,1,1},
						{1,1,0,0},
						{1,1,1,1},
						{1,0,0,1} };

	public RoomQuest_3x3_1(JDPoint p, DungeonFiller df, List<Item> toPutIn) {
		super(p,df,3,3, toPutIn);
	}
	
	public RoomQuest_3x3_1(JDPoint p, DungeonFiller df, List<Item> takeItems,
			List<Item> toPutIn) {
		super(p,df,3,3, toPutIn);
		this.takeItems = takeItems;
	}

	/**
	 * @see RoomQuest#setUp()
	 */
	@Override
	public boolean setUp() {
		if (!accessible(rooms[1][0], RouteInstruction.WEST)) {
			////System.out.println("rq nicht zug�nglich - abbruch!");
			return false;
		}
		if (!accessible(rooms[0][1], RouteInstruction.NORTH)) {
			////System.out.println("rq nicht zug�nglich - abbruch!");
			return false;
		}
		if (!accessible(rooms[1][2], RouteInstruction.EAST)) {
			////System.out.println("rq nicht zug�nglich - abbruch!");
			return false;
		}
		if (!accessible(rooms[2][1], RouteInstruction.SOUTH)) {
			////System.out.println("rq nicht zug�nglich - abbruch!");
			return false;
		}
		claimRooms();
		if (df.getDungeon().getRoom(this.location).getHall().validateNet()) {
			////System.out.println("Halle mit rq erfolgreich validiert!");
		} else {
			////System.out.println(
			//	"Halle mit rq nicht mehr validierbar! - abbruch");
			unClaimRooms();
			return false;
		}
		setDoors();	
		for(int i = 0; i < 3; i++) {
			rooms[0][1].figureEnters(df.getBigMonster(1900),0);	
			rooms[1][0].figureEnters(df.getBigMonster(1900),0);	
			rooms[1][2].figureEnters(df.getBigMonster(1900),0);	
			rooms[2][1].figureEnters(df.getBigMonster(1900),0);	
		}
		for(int i = 0; i < 4; i++) {
			rooms[1][1].figureEnters(df.getSmallMonster(800),0);	
		}
		List<Item> items = new LinkedList<Item>();
		if(takeItems == null || takeItems.size() == 0) {
		Item it1 = ItemPool.getRandomItem(30,1);
		Item it2 = ItemPool.getRandomItem(30,2);
		
		if(it1 != null) {
			items.add(it1);
		}
		if(it2 != null) {
			items.add(it2);
		}
		}
		else {
			if(takeItems.size() >= 2) {
				for(int i = 0; i < 2; i++) {
					Item it = takeItems.remove((int)(Math.random()*takeItems.size()));
					if(it instanceof Rune) {
						//System.out.println("3x3 schnappt sich "+it.toString());	
					}
					items.add(it);	
				}	
			}
			else {
				items.add(takeItems.remove(0));
			}	
			
		}
		Chest ch = new Chest(items);
		rooms[1][1].setChest(ch);
		return true;
	}
	
	private void setDoors() {
		for(int i = 0; i < doors.length; i++) {
			for(int j = 0; j < doors[0].length; j++) {
				int k  = doors[i][j];
				int x = i / 3;
				int y = i % 3;
				if(k == 1) {
					rooms[x][y].setDoor(df.getDungeon(),j+1,true);
				}
				else if(k == 0) {
					rooms[x][y].removeDoor(j+1, true);	
				}
			}
		}	
	}
	
	
}
