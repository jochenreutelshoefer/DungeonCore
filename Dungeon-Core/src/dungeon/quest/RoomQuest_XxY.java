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

import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.AbstractDungeonFiller;
import dungeon.util.RouteInstruction;

;
public class RoomQuest_XxY extends RoomQuest{
	
	
	public static int cnt = 0;
	//int sizeX;
	//int sizeY;
	boolean locked;

	
	Key theKey;

	
	public RoomQuest_XxY(int x, int y, JDPoint p, AbstractDungeonFiller df,
			List<Item> toPutIn, boolean locked) {
		
		super(p,df,x,y,toPutIn);
		
		////System.out.println();
		////System.out.println();
		////System.out.println();
		////System.out.println();
		
		////System.out.println("Neues rqXY: "+"x: "+x+" - y: "+y);
		////System.out.println("Nummer: "+cnt);
		////System.out.println("location: "+p.toString());
		this.locked = locked;
		
	}

	/**
	 * 
	 * @uml.property name="theKey"
	 */
	public Key getKey() {
		return theKey;
	}

	
	@Override
	public boolean setUp() {
		JDPoint p = this.getRimRoom();
		int k = this.isPossibleEntrance(p);
		int rounds = 0;
		while(k == 0) {
			
			p = this.getRimRoom();
			k = this.isPossibleEntrance(p);
			rounds++;
			if(rounds > 300) {
				if(sizeX == 1 && sizeY == 1) {
				//System.out.println("rqXY Haette keinen Eingang");
				}
				//System.out.println("pech - kein Eingang");
				return false;
			}
		}
		claimRooms();
		if (df.getDungeon().getRoom(this.location).getHall().validateNet()) {
			////System.out.println("Halle mit rq erfolgreich validiert!");
		} else {
			if(sizeX == 1 && sizeY == 1) {
			//System.out.println(
			
			//	"Halle mit rqxy nicht mehr validierbar! - abbruch");
			}
			unClaimRooms();
			return false;
		}
		//mache Wand
		makeWall();
		//mache Tuer
		
		entrenceRoom = df.getDungeon().getPoint(p.getX() + location.getX(),p.getY() + location.getY());;
		entrenceDirection = k;
		if(locked) {
			theKey = df.getNextKey();
			Room other = df.getDungeon().getRoomAt(
					df.getDungeon().getRoom(entrenceRoom),
					RouteInstruction.direction(k));
			Door d = new Door(df.getDungeon().getRoom(entrenceRoom),other,theKey);
			df.getDungeon().getRoom(entrenceRoom).setDoor(d,k,true);
			
			
		}else {
			rooms[p.getY()][p.getX()].setDoor(df.getDungeon(),k,true);
		}
		
		
		cnt++;
		//System.out.println("RQXY Nummer: "+cnt+ " erfolgreich!");
		return true;	
	}
	
	private void makeWall() {
		////System.out.println("rqxy.makeWall()"+" sizeX: "+sizeX+"  - "+"sizeY: "+sizeY);
		for(int i = 0; i < sizeX; i++) {
			//System.out.println("i: "+i);
			for(int j = 0; j < sizeY; j++) {
				//System.out.println("j: "+j);
				////System.out.println("makeWall bei Raum: "+i+" - "+j);
				int k = (isPossibleEntrance(df.getDungeon().getPoint(i,j)));
				////System.out.println("k: "+k);
				Room r = rooms[j][i];
				////System.out.println("Raum: "+r.toString());
				if(k != 0 && (!r.getDoor(k).isHallDoor())) {
					////System.out.println("removing Door!");
					r.removeDoor(k,true);	
				}	
				
			}
		}
		////System.out.println();
		////System.out.println("Ecken bereinigen!");
		JDPoint []ecken = new JDPoint[4];
		ecken[0] = new JDPoint(0,0);
		ecken[1] = new JDPoint(0,sizeX-1);
		ecken[2] = new JDPoint(sizeY-1,sizeX-1);
		ecken[3] = new JDPoint(sizeY-1,0);
		for(int i = 0; i < 4; i++) {
			////System.out.println("rooms.length: "+rooms.length);
			////System.out.println("rooms[0].length: "+rooms[0].length);
		
			Room r = rooms[ecken[i].getX()][ecken[i].getY()];	
			int k = (isPossibleEntrance(new JDPoint(ecken[i].getY(),ecken[i].getX())));
			////System.out.println("k: "+k);
				////System.out.println("Raum: "+r.toString());
				if(k != 0 && (!r.getDoor(k).isHallDoor())) {
					////System.out.println("removing Door!  "+k);
					r.removeDoor(k,true);	
				}
		}
		
		if (sizeY == 1 || sizeX == 1) {
			for (int i = 0; i < sizeX; i++) {
				////System.out.println("i: "+i);
				for (int j = 0; j < sizeY; j++) {
					////System.out.println("j: "+j);
					////System.out.println("makeWall bei Raum: "+i+" - "+j);
					int k = (isPossibleEntrance(df.getDungeon().getPoint(i, j)));
					////System.out.println("k: "+k);
					Room r = rooms[j][i];
					////System.out.println("Raum: "+r.toString());
					if (k != 0 && (!r.getDoor(k).isHallDoor())) {
						////System.out.println("removing Door!"+k);
						r.removeDoor(k, true);
					}

				}
			}
		}
	}
	
	

}
