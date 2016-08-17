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
import item.Key;

import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.util.RouteInstruction;

public class RoomQuest_XxY extends RoomQuest{
	
	private boolean locked;
	private Key theKey;

	public RoomQuest_XxY(int x, int y, JDPoint p, DungeonFiller df,
			 boolean locked) {
		super(p,df,x,y);
		this.locked = locked;
		
	}

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
				return false;
			}
		}

		//mache Wand
		makeWall();

		//mache Tuer
		entrenceRoom = df.getDungeon().getPoint(p.getX() + location.getX(),p.getY() + location.getY());;
		entrenceDirection = k;
		if(locked) {
			theKey = df.getNextKey();
			df.itemToDistribute(theKey);
			Room other = df.getDungeon().getRoomAt(
					df.getDungeon().getRoom(entrenceRoom),
					RouteInstruction.direction(k));
			Door d = new Door(df.getDungeon().getRoom(entrenceRoom),other,theKey);
			df.getDungeon().getRoom(entrenceRoom).setDoor(d,k,true);
			
			
		}else {
			rooms[p.getY()][p.getX()].setDoor(df.getDungeon(),k,true);
		}
		return true;
	}
	
	private void makeWall() {
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeY; j++) {
				int k = (isPossibleEntrance(df.getDungeon().getPoint(i,j)));
				Room r = rooms[j][i];
				if(k != 0 && (!r.getDoor(k).isHallDoor())) {
					r.removeDoor(k,true);
				}	
			}
		}
		JDPoint []ecken = new JDPoint[4];
		ecken[0] = new JDPoint(0,0);
		ecken[1] = new JDPoint(0,sizeX-1);
		ecken[2] = new JDPoint(sizeY-1,sizeX-1);
		ecken[3] = new JDPoint(sizeY-1,0);
		for(int i = 0; i < 4; i++) {

			Room r = rooms[ecken[i].getX()][ecken[i].getY()];	
			int k = (isPossibleEntrance(new JDPoint(ecken[i].getY(),ecken[i].getX())));
				if(k != 0 && (!r.getDoor(k).isHallDoor())) {
					r.removeDoor(k,true);
				}
		}
		
		if (sizeY == 1 || sizeX == 1) {
			for (int i = 0; i < sizeX; i++) {
				for (int j = 0; j < sizeY; j++) {
					int k = (isPossibleEntrance(df.getDungeon().getPoint(i, j)));
					Room r = rooms[j][i];
					if (k != 0 && (!r.getDoor(k).isHallDoor())) {
						r.removeDoor(k, true);
					}
				}
			}
		}
	}

}