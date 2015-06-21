/**
 * Ein RoomQuest soll eine geschickte/interessante/sinnvolle Struktur von Raeumen/Tueren sein,
 * mit Raetselcharkter (Schluessel etc.)
 */
package dungeon.quest;

import item.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import shrine.Shrine;
import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.AbstractDungeonFiller;
import dungeon.generate.Hall;
import dungeon.util.DungeonUtils;
import dungeon.util.RouteInstruction;
import figure.DungeonVisibilityMap;
import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
public abstract class RoomQuest /*extends JDEnv */{


	public static final int TYPE_ANY = 0;
	public static final int TYPE_SMALL = 1;
	public static final int TYPE_BIG = 2;
	public static final int TYPE_NATURE = 3;
	public static final int TYPE_CREATURE = 4;
	public static final int TYPE_UNDEAD = 5;
	public static final int TYPE_ORC = 6;
	public static final int TYPE_WOLF = 7;
	public static final int TYPE_SKELETON = 8;
	public static final int TYPE_OGRE = 9;
	public static final int TYPE_BEAR = 10;
	public static final int TYPE_GHUL = 11;
	
	protected final Room[][] rooms;
	protected final int sizeX;
	protected final int sizeY;

	
	protected JDPoint entrenceRoom;

	protected int entrenceDirection;

	protected final AbstractDungeonFiller df;

	
	protected final JDPoint location;

	
	protected boolean valid = true;

	protected boolean doorsRemoved = false;
	protected final boolean wallMade = false;

	
	protected List<Item> toPutIn = new LinkedList<Item>();

	
	protected final Hall halle;


	public RoomQuest(
		JDPoint p,
		AbstractDungeonFiller df,
		int x,
		int y,
			List<Item> toPutIn) {
			
		sizeX = x;
		sizeY = y;
		rooms = new Room[sizeY][sizeX];
		this.df = df;
		location = p;
		halle = df.getDungeon().getRoom(location).getHall();
		////System.out.println("RQ auf Halle: " + halle.getName());
		this.toPutIn = toPutIn;

		////System.out.println();
		////System.out.println();
		////System.out.println(this.getClass());
		////System.out.println("Versuche rq zu setzen auf: "+p.toString());
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				Room r = df.getDungeon().getRoomNr(p.getX() + i, p.getY() + j);

				if ((r != null)
					&& (r.getHall() == halle)
					&& (r.getRoomQuest() == null)
					&& (!r.isStart())
					&& (!r.hasHallDoor())
					&& (!r.hasLockedDoor())) {
						if(sizeX == 1 && sizeY == 1) {
							
							//System.out.println("1x1 ist ok!: "+r.toString());
						}
					rooms[j][i] = r;
				} else {
					if (this instanceof RoomQuest_XxY) {
						////System.out.println();
						////System.out.println("Raum nicht in Ordnung: ");
						if (r != null) {
							////System.out.println(r.toString());
							if (r.getHall() != null) {
								////System.out.println(
								//	"Halle: " + r.getHall().getName());
							} else {
								////System.out.println("Halle ist null!");
							}
							////System.out.println("isStart: " + r.isStart());
							////System.out.println(
							//	"hasHallDoor: " + r.hasHallDoor());
							////System.out.println(
							//	"hasLockedDoor: " + r.hasLockedDoor());
						}
					} else {
						////System.out.println("isnull: " + i + " - " + j);
					}
					if(sizeX == 1 && sizeY == 1) {
						//System.out.println("valid1x1 = false !");
					}
					valid = false;
				}
			}
		}
		if(sizeX == 1 && sizeY == 1) {
			if(valid) {
				//System.out.println("ist valid!");
			}
		}
	}

	public static RoomQuest newRoomQuest(
		String type,
		JDPoint rq_point,
			List<Item> restItems, List<Item> toPutIn,
		Shrine s,
		AbstractDungeonFiller df) {

		RoomQuest rq = null;
		if (type.equals("3x3_1 ")) {
			rq = new RoomQuest_3x3_1(rq_point, df, restItems, toPutIn);
		}
		if (type.equals("2x2_1 ")) {
			rq = new RoomQuest_2x2_1(rq_point, df, restItems, toPutIn);
		}
		if (type.equals("1x1_1 ")) {
			rq =
				new RoomQuest_1x1_1(
					rq_point,
					df,
					true,
					true,
					s,
					restItems,
					toPutIn);
		}

		return rq;
	}

	protected void claimRooms() {
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				rooms[j][i].setRoomQuest(this);
			}
		}
	}
	protected void unClaimRooms() {
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				rooms[j][i].setRoomQuest(null);
			}
		}
	}
	
	public Room getEntrenceRoom () {
		return df.getDungeon().getRoom(entrenceRoom);
	}

	protected boolean accessible(Room r, int dir) {
		boolean b = false;
		Room entrance = df.getDungeon().getRoomAt(r,
				RouteInstruction.direction(dir));
		if (entrance == null) {
			return false;
		}
		boolean room_ok = true;
		if (entrance.getRoomQuest() != null) {
			//System.out.println("kein Eingang");
			room_ok = false;
		}
		if (entrance.getHall() != r.getHall()) {
			//System.out.println("kein Eingang");
			room_ok = false;
		}

		boolean valid_connection = false;
		Door d = r.getConnectionTo(entrance);
		if ((d != null) && (!d.isHallDoor()) && (!d.hasLock())) {
			valid_connection = true;
		}

		return room_ok && valid_connection;
	}

	public boolean isPossible() {
		if(sizeX == 1 && sizeY == 1) {
			//System.out.println("valid1x1: " + valid);
		}
		boolean b = false;
		if(valid) {
			b = setUp();
		}
		if(sizeX == 1 && sizeY == 1) {
			//System.out.println("RQ1x1 setup hat geklappt: " + b);
		}
		
		return valid && b;
	}

	protected int isPossibleEntrance(JDPoint p) {
		int x = p.getX();
		int y = p.getY();
		////System.out.println("rooms.length: "+rooms.length);
		////System.out.println("rooms[0].length: "+rooms[0].length);
		////System.out.println("x: "+x);
		////System.out.println("y: "+y);
		Room r = rooms[y][x];
		boolean found = false;
		int dir = 0;
		if (x == 0) {
			if (this.accessible(r, RouteInstruction.WEST)) {
				found = true;
				dir = RouteInstruction.WEST;
			}
		}
		if (x == sizeX - 1) {
			if (this.accessible(r, RouteInstruction.EAST)) {
				found = true;
				dir = RouteInstruction.EAST;
			}
		}
		if (y == 0) {
			if (this.accessible(r, RouteInstruction.NORTH)) {
				found = true;
				dir = RouteInstruction.NORTH;
			}
		}
		if (y == sizeY - 1) {
			if (this.accessible(r, RouteInstruction.SOUTH)) {
				found = true;
				dir = RouteInstruction.SOUTH;
			}
		}
		return dir;
	}

	protected JDPoint getRimRoom() {
		////System.out.println();
		////System.out.println("Sucher rq-RimRoom: " + sizeX + " - " + sizeY);
		int x = (int) (Math.random() * sizeX);
		////System.out.println("ein X: " + x);
		int y = (int) (Math.random() * sizeY);
		////System.out.println("ein Y: " + y);
		if ((x == 0) || (x == sizeX - 1) || (y == 0) || (y == sizeY - 1)) {
			////System.out.println("ist Rand!");
			return df.getDungeon().getPoint(x, y);
		} else {
			////System.out.println("ist nicht --> nochmal");
			return getRimRoom();
		}
	}
	protected boolean isRimRoom(JDPoint p) {
		int y = p.getY();
		int x = p.getX();
		return ((x == 0) || (x == sizeX - 1) || (y == 0) || (y == sizeY - 1));
	}

	public abstract boolean setUp();

	/**
	 * Returns the valid.
	 * @return boolean
	 * 
	 * @uml.property name="valid"
	 */
	public boolean isValid() {
		return valid;
	}

	
	public void plugMonster(int avStrength, double cntProRoom, int typeCode) {
		//LinkedList monsterToPlug = new LinkedList();
		int cnt = (int)(cntProRoom * sizeX * sizeY);
		for(int i = 0; i < cnt; i++) {
			Monster m = getMonster(avStrength,typeCode);
			df.equipMonster(m,2);
			getRandomRoom().figureEnters(m,0);
			
			
		}
		
	}
	
	private Room getRoomForShrine() {
		Room r =  this.getRandomRoom();	
		if(r.getShrine() == null) {
			return r;
		}
		else {
			return getRoomForShrine();	
		}
		
	}
	
	
	public void plugShrine(Shrine s) {
		
		
		List<Room> roomList = new LinkedList<Room>();
		for(int i = 0; i < rooms.length; i++) {
			for(int j = 0; j < rooms[0].length; j++) {
				
				roomList.add(rooms[i][j]);
				
			}
				
		}
		Map<List<Room>, Room> ways = new HashMap<List<Room>, Room>();
		for(int i = 0 ; i < roomList.size(); i++) {
			Room to = roomList.get(i);
			List<Room> way = DungeonUtils.findShortestWayFromTo(df.getDungeon()
					.getRoom(this.entrenceRoom), to, DungeonVisibilityMap
					.getAllVisMap(df.getDungeon()));
			
			if(way != null) {
			ways.put(way,roomList.get(i));
			//System.out.println("Der Weg hat Laenge: "+way.size());
			}else {
				//System.out.println("Weg nach: "+to.toString()+" konnte nicht gefunden werden!");
			}
			
		}
		Set<List<Room>> set = ways.keySet();
		Iterator<List<Room>> it = set.iterator();
		int maxWay = -1;
		List<Room> longest = null;
		while(it.hasNext()) {
			List<Room> way = it.next();
			if(way.size() > maxWay) {
					longest = way;
					maxWay = way.size();
			}
			
		}
		//System.out.println("laengster Weg: "+longest.size());
		Room farest = ways.get(longest);
		//System.out.println("Setze Schrein auf: "+farest.toString());
		farest.setShrine(s,true);
		
	}
	
	protected boolean validateNet() {
		LinkedList hash = new LinkedList();
		
		////System.out.println();
		////System.out.println();
		////System.out.println("rooms ohne rq:");
		LinkedList roomList = new LinkedList();
		for(int i = 0; i < rooms.length; i++) {
			for(int j = 0; j < rooms[0].length; j++) {
				roomList.add(rooms[i][j]);
			}
		}
		
		
		Room first = df.getDungeon().getRoom(location);
		////System.out.println();
		////System.out.println();
		////System.out.println("mache netz; ");
		////System.out.println("adding: "+first.toString());
		hash.add(first);
		int k = 0;
		while (k < hash.size()) {
			Room r = (Room) hash.get(k);
			Door[] doors = r.getDoors();
			for (int i = 0; i < 4; i++) {
				if (doors[i] != null) {
					Room a = doors[i].getOtherRoom(r);
					if (!hash.contains(a)
						&& (a.getRoomQuest() == this)) {
						////System.out.println("adding: "+a.toString());
						hash.add(a);
					}
				}
			}
			k++;

		}
		int l = hash.size();
		////System.out.println("Anzahl R�ume insgesamt: "+roomList.size());
		////System.out.println("Anzahl erreichbarer R�ume: " + l);
		////System.out.println("Anzahl r�ume ohne rq: "+not_rq_roomList.size());
		if (l == roomList.size()) {
			return true;
		} else {
			//System.out.println("validate fehlgeschlagen!!!!");
			return false;
		}

	}
	
	public boolean isEntrenceDoor(Door d) {
		Room r = df.getDungeon().getRoom(entrenceRoom);
		////System.out.println("entrenceRoom: "+r.toString());
		////System.out.println("Richtung: "+entrenceDirection);	
		Door d2 = r.getDoor(entrenceDirection);
		boolean b =  d == d2;
		////System.out.println("Ist Eingangstuer: "+b);
		return b;
	}
	
	public void removeDoors(int cnt) {
		boolean ok = validateNet();
		doorsRemoved = true;
		int undos = 0;
		int rounds = 0;
		while (cnt > 0) {
			rounds++;
			
			Room r1 = getRandomRoom();
			////System.out.println(r1.hasHallDoor());
			int k = 1 + ((int) Math.random() * 4);
			Door d1 = null;
			if(!isEntrenceDoor(r1.getDoor(k))) {
				d1 = r1.removeDoor(k, true);
			}

			if (d1 != null ) {

				cnt--;

				ok = validateNet();
				if (!ok) {

					undoDoorRemove(r1, k, d1);
					cnt++;
					undos++;
				} else {
					undos = 0;
				}

			}
			if (undos > 30) {
				break;
			}
			if (rounds > 1000) {
				//System.out.println();
				//System.out.println();
				//System.out.println();
				//System.out.println();
				//System.out.println("endlosschleife hall.removeDoors-()!");
				//System.out.println(
				//	"koennen nicht genug Tueren geloescht werden --> bleiben");
				//System.out.println();
				//System.out.println();
				break;
			}

		}
		//System.out.println("cnt uebrig: " + cnt);

	}
	protected void undoDoorRemove(Room r1, int dir, Door d1) {
		r1.setDoor(d1, dir, true);

	}

	
	
	private Room getRandomRoom() {
		int x = (int)(Math.random() * rooms.length);
		int y = (int)(Math.random() * rooms[0].length);
		return rooms[x][y];	
	}
	
	private Monster getMonster(int strength, int typeCode) {
			if(typeCode == TYPE_ANY) {
				int k = 6 + (int)(Math.random() * 6);
				return getMonster(strength,k);
				
			}
			else if(typeCode == TYPE_SMALL) {
				int k  = (int)(Math.random() * 3);
				if(k < 1) {
					return getMonster(strength,this.TYPE_WOLF);
				}else if(k < 2) {
					return getMonster(strength,this.TYPE_ORC);
				}else {
					return getMonster(strength,this.TYPE_SKELETON);
				}
			}	
			else if(typeCode == TYPE_BIG) {
				int k  = (int)(Math.random() * 3);
				if(k < 1) {
					return getMonster(strength,this.TYPE_BEAR);
				}else if(k < 2) {
					return getMonster(strength,this.TYPE_OGRE);
				}else {
					return getMonster(strength,this.TYPE_GHUL);
				}
			}
			else if(typeCode == TYPE_NATURE) {
				if(Math.random() < 0.5) {
					return getMonster(strength,this.TYPE_WOLF);
				}else {
					return getMonster(strength,this.TYPE_BEAR);
				}
				
			}
			else if(typeCode == TYPE_CREATURE) {
				if(Math.random() < 0.5) {
					return getMonster(strength,this.TYPE_ORC);
				}else {
					return getMonster(strength,this.TYPE_OGRE);
				}
				
			}
			else if(typeCode == TYPE_UNDEAD) {
				if(Math.random() < 0.5) {
					return getMonster(strength,this.TYPE_SKELETON);
				}else {
					return getMonster(strength,this.TYPE_GHUL);
				}
				
			}
			else if(typeCode == TYPE_ORC) {
				return new Orc((int)(0.8 * strength));
				
			}else if(typeCode == TYPE_WOLF) {
				return new Wolf((int)(0.8 * strength));
				
			}else if(typeCode == TYPE_SKELETON) {
				return new Skeleton((int)(0.8 * strength));
				
			}else if(typeCode == TYPE_BEAR) {
				return new Spider((int)(1.2 * strength));
				
			}
			else if(typeCode == TYPE_OGRE) {
				return new Ogre((int)(1.2 * strength));
				
			}
			else if(typeCode == TYPE_GHUL) {
				return new Ghul((int)(1.2 * strength));
				
			}
			else return null;
	}

}
