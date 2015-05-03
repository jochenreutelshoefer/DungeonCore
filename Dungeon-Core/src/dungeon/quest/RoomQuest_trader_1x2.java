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
import item.Key;

import java.util.List;

import shrine.Trader;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.generate.DungeonFiller;
import dungeon.util.RouteInstruction;
public class RoomQuest_trader_1x2 extends RoomQuest {


	public RoomQuest_trader_1x2(JDPoint p, DungeonFiller df, List<Item> toPutIn) {
		super(p,df,2,1,toPutIn);	
	}
	/**
	 * @see RoomQuest#setUp()
	 */
	@Override
	public boolean setUp() {
		if (!accessible(rooms[0][0], RouteInstruction.WEST)) {
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
		
		if(rooms[0][1].getShrine() != null) {
			return false;
		}
		
		Key k = df.getNextKey();
		rooms[0][0].removeDoor(RouteInstruction.NORTH, true);
		rooms[0][0].removeDoor(RouteInstruction.SOUTH, true);
		rooms[0][0].setDoor(df.getDungeon(), RouteInstruction.WEST, true);
		rooms[0][1].removeDoor(RouteInstruction.NORTH,true);
		rooms[0][1].removeDoor(RouteInstruction.SOUTH,true);
		rooms[0][1].removeDoor(RouteInstruction.EAST,true);
		Door keyDoor = new Door(rooms[0][1], rooms[0][0], k);
		rooms[0][1].setDoor(keyDoor, RouteInstruction.WEST, true);
		toPutIn.add(k);
		rooms[0][0].figureEnters(df.getBigMonster(1500),0);
		rooms[0][0].figureEnters(df.getBigMonster(1500),0);
		
		Trader trader = new Trader(70,df.getDungeon());
		df.getDungeon().addShrine(trader);
		rooms[0][1].setShrine(trader,true);
		
		
		return true;
	}

}
