/**
 * Wird zur Gestaltung des Dungeons benutzt. 
 * Eine Halle stellt ein System zusammenhaengender Raeume dar, 
 * das nach au�en hin mehr oder weniger abgeschlossen ist. 
 * 
 * Enth�lt den Algorithmus zum generieren einer Halle von einem Startraum aus, 
 * es werden dann immer benachbarte Raeume hinzugenommen, welche noch nicht zu anderen
 * Hallen gehoeren, bis die gewuenschte Anzahl erreicht ist oder kein Platz mehr ist.
 * Enthaelt des Weiteren Algorithmen zum zufaelligen Verteilen von Monstern, Truhen und Schreinen
 */
package dungeon.generate;

import java.util.*;
import java.util.List;
import java.awt.*;

import dungeon.Chest;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RouteInstruction;
import dungeon.quest.RoomQuest_XxY;

import shrine.Shrine;

import figure.Figure;
import figure.monster.Monster;

public class Hall {

	public final static int SPOT_SHRINE = 1;
	public final static int SPOT_CHEST = 2;

	int preferredSize;
	int avMonsterStrength;
	//LinkedList subHalls = new LinkedList();

	
	JDPoint startPoint;

	int direction;

	
	Sector sec;

	
	LinkedList<Room> rooms = new LinkedList<Room>();

	
	Dungeon d;

	
	String name;

	
	List<Figure> monsters = new LinkedList<Figure>();

	
	int floorIndex;

	
	public JDPoint getStartPoint() {
		return startPoint;
	}


	public Hall(
		Dungeon d,
		int size,
		int avMonsterStrength,
		int subhalls,
		JDPoint p,
		int dir,
		int roomQuests,
		Sector sec,
		String name,
		int floorIndex) {

		this.name = name;
		this.floorIndex = floorIndex;
		this.sec = sec;
		this.preferredSize = size;
		this.avMonsterStrength = avMonsterStrength;
		startPoint = p;
		direction = dir;
		this.d = d;

	}

	
	public int getFloorIndex() {
		return floorIndex;
	}


	private void claimRooms() {
		for (int i = 0; i < rooms.size(); i++) {
			Room r = (Room) rooms.get(i);
			boolean ok = r.setHall(this);

			//Fehler in Levelgenerierung
			if (!ok) {
				((SectorDungeonFiller1) sec.df).succes = false;
			}
			r.setSec(sec);
		}
	}

	private int getDoors(Room r) {
		Door[] doors = r.getDoors();
		int k = 0;
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				if (doors[i].isHallDoor()) {
					return 5;
				}
				k++;
			}

		}
		return k;
	}

	public boolean plugChest(Chest ch) {
		Room r = getSpotRoom(SPOT_CHEST);
		if (r != null) {
			r.setChest(ch);
			return true;
		} else {
			return false;
		}
	}

	public boolean plugShrine(Shrine s) {
		Room r = getSpotRoom(SPOT_SHRINE);
		if (r != null) {
			r.setShrine(s, true);
			return true;
		} else {
			return false;
		}

	}

	private Room getSpotRoom(int code) {
		Room r = null;
		int k = 6;
		for (int i = 0; i < rooms.size(); i++) {
			Room p = (Room) rooms.get(i);
			////System.out.println("möglicher Raum fuer spot: " + p.toString());
			int pD = getDoors(p);
			////System.out.println("Tueren: " + pD);
			if (pD < k || ((pD == k) && (Math.random() < 0.5))) {
				////System.out.println("Anzahl passt");
				if ((!((code == SPOT_SHRINE) && (p.getShrine() != null)))
					&& (!((code == SPOT_CHEST) && (p.getChest() != null)))) {
					////System.out.println("wird gesetzt!");
					r = p;
				}
			}
		}
		return r;

	}

	public void removeDoors(int cnt) {
		boolean ok = validateNet();
		int undos = 0;
		int rounds = 0;
		while (cnt > 0) {
			rounds++;
			int e = (int) (Math.random() * rooms.size());
			//System.out.println("e: " + e);
			Room r1 = (Room) rooms.get(e);
			//System.out.println(r1.hasHallDoor());
			int k = 1 + ((int) Math.random() * 4);
			Door d1 = r1.getDoor(k);
			
			if (d1 != null && (!d1.isHallDoor()) && (!d1.hasLock()) && (!d1.partOfRoomQuest())) {
				//System.out.println("Removing Door: "+d1.toString());
				r1.removeDoor(d1,true);
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
			if (undos > 20) {
				break;
			}
			if (rounds > 1000) {
				//	"koennen nicht genug Tueren geloescht werden --> bleiben");
				break;
			}

		}

	}
	protected void undoDoorRemove(Room r1, int dir, Door d1) {
		r1.setDoor(d1, dir, true);

	}
	public boolean validateNet() {
		LinkedList<Room> hash = new LinkedList<Room>();
		List<Room> not_rq_rooms = new LinkedList<Room>();
		//System.out.println();
		//System.out.println();
		////System.out.println("rooms ohne rq:");
		for (int i = 0; i < rooms.size(); i++) {
			Room r = (Room) rooms.get(i);
			if (r.getRoomQuest() == null) {
				////System.out.println(i+": adding" +r.toString());
				not_rq_rooms.add(r);
			}
		}
		Room first = d.getRoom(startPoint);
		//System.out.println();
		//System.out.println();
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
						&& (a.getHall() == this)
						&& (a.getRoomQuest() == null)) {
						////System.out.println("adding: "+a.toString());
						hash.add(a);
					}
				}
			}
			k++;

		}
		int l = hash.size();
		if (l == not_rq_rooms.size()) {
			return true;
		} else {
			//System.out.println("validate fehlgeschlagen!!!!");
			return false;
		}

	}

	public Room getRandomRoom() {
		int k = (int) (Math.random() * rooms.size());

		//bei fehlerhafter generierung
		if (rooms.size() == 0) {
			((SectorDungeonFiller1) sec.df).succes = false;
			return d.getRoomNr(0, 0);
		}

		Room r = (Room) rooms.get(k);
		return r;

	}

	public Room getRandomRoomForShrine() {
		int k = (int) (Math.random() * rooms.size());
		if (rooms.size() == 0) {
			((SectorDungeonFiller1) sec.df).succes = false;
			return d.getRoomNr(0, 0);
		}

		while (true) {
			if(((Room) rooms.get(k)).getShrine() == null) {
				break;
			}
			k = (int) (Math.random() * rooms.size());
		}

		return (Room) rooms.get(k);

	}
	public Room getRimRoom(int dir) {
		int rounds = 0;
		int k = (int) (Math.random() * rooms.size());
		Room r = (Room) rooms.get(k);
		while (!isRimRoom(r, dir)) {
			rounds++;
			k = (int) (Math.random() * rooms.size());
			r = (Room) rooms.get(k);
			if (rounds > 50) {
				return null;

			}

		}
		return r;
	}
	
	public int getSize() {
		return rooms.size();
	}


	public RoomQuest_XxY insertRQXY(int x, int y, boolean locked, Shrine s) {


		//System.out.println("halle.insertRQ: "+x+" - "+y);
		Room r = null;
		RoomQuest_XxY rq = null;
		boolean ok = false;
		List<Integer> indexList = new LinkedList<Integer>();
		for(int i = 0; i < rooms.size(); i++) {
			indexList.add(new Integer(i));	
		}
		

		int rounds = 0;
		while (!ok) {
			Integer i =  (Integer)indexList.get((int)(Math.random()*indexList.size()));
			indexList.remove(i);
			r = (Room)rooms.get(i.intValue());
			if ((!this.isHereRimRoom(r, 0))
				&& (!this
					.isHereRimRoom(
						sec.df.getDungeon().getRoomNr(
							r.getNumber().getX() + x-1,
							r.getNumber().getY() + y-1),
						0))
				&& (!this
					.isHereRimRoom(
						sec.df.getDungeon().getRoomNr(
							r.getNumber().getX() ,
							r.getNumber().getY() + y-1),
						0))
				&& (!this
					.isHereRimRoom(
						sec.df.getDungeon().getRoomNr(
							r.getNumber().getX() + x-1,
							r.getNumber().getY() ),
						0))
				) {
				rq = new RoomQuest_XxY(x, y, r.getNumber(), sec.df, null, locked);
				ok = rq.isPossible();
				if(x == 1 && y == 1) {
					if(!ok) {
					//System.out.println("hall: 1x1 nicht ok !");	
					}
					else {
						//System.out.println("hall: 1x1 ok !");		
					}
				}
				
			}
			else {
				if(x == 1 && y == 1) {
					//System.out.println("Raum nicht geignet!:");
					//System.out.println(r.toString());
				}	
			}
			rounds++;
			if (rounds > 300) {
				//System.out.println("300 Versuche umsonst!");
				return null;
			}
			if(indexList.size() == 0) {
				//System.out.println("Alle R�ume durchprobiert, setzen nicht m�glich!");	
				return null;
			}
		}
		rq.removeDoors(x*y/2);
		if(s != null) {
			rq.plugShrine(s);

		}
		
		return rq;

	}
	
	public Room getRandomRoomForShrineNonRQ() {
		
		
		Room r = null;
		while(true) {
			r = getRandomRoomForShrine();
			if(r.getRoomQuest() == null) {
				break;
			}	
		}
		return r;
	}
	
	public Monster getRandomMonster() {
		boolean b = false;
		Monster m = null;
		int rounds = 0;
		while(!b) {
			Room r = this.getRandomRoom();	
			List<Figure> l = r.getRoomFigures();
			if(l.size() > 0) {
				int k = (int)(Math.random() * l.size());
				m = (Monster)l.get(k);	
				b = true;
			}
			rounds++;
			if( rounds > 100) {
				//System.out.println("Es konnte kein Monster in Halle gefunden werden!!");
				break;
			}	
			
		}
		return m;	
	}

	//Nur Randraum wenn benachbarten Raum noch ganz frei ist
	private boolean isRimRoom(Room r, int dir) {
		//System.out.println("ueberpruefe: " + r.toString());
		List<Room> neighbours = d.getNeighbourRooms(r);
		for (int i = 0; i < neighbours.size(); i++) {
			Room n = (Room) neighbours.get(i);
			if ((!n.isClaimed())) {
				if (dir == 0) {
					return true;
				} else {
					int theDir = Dungeon.getNeighbourDirectionFromTo(r, n);
					if (theDir == dir) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//Zaehlt auch als Rand wenn angrenzender Raum einer anderen Halle gehoert
	private boolean isHereRimRoom(Room r, int dir) {
		if (r == null) {
			//System.out.println("raum r ist null! --> nicht geeignet");
			//au�erhalb
			return true;
		}
		List<Room> neighbours = d.getNeighbourRooms(r);
		for (int i = 0; i < neighbours.size(); i++) {
			Room n = (Room) neighbours.get(i);
			Hall halle = n.getHall();
			
			if ((!n.isClaimed())) {
				if (dir == 0) {
					return true;
				} else {
					int theDir = Dungeon.getNeighbourDirectionFromTo(r, n);
					if (theDir == dir) {
						return true;
					}
				}
			}
			if(((halle != null) && (halle != this))) {
				////System.out.println("Nachbarraim: andere Halle! --> nicht geignet");
				if (dir == 0) {
					return true;
				} else {
					int theDir = Dungeon.getNeighbourDirectionFromTo(r, n);
					if (theDir == dir) {
						return true;
					}
				}
			}
			if((n.getRoomQuest() != null) ) {
				////System.out.println("Nachbarraim: Schon ein RQ!");
				if (dir == 0) {
					return true;
				} else {
					int theDir = Dungeon.getNeighbourDirectionFromTo(r, n);
					if (theDir == dir) {
						return true;
					}
				}
				
			}
		}
		return false;

	}
	public void setMonster(LinkedList<Figure> monsters) {
		while (monsters.size() > 0) {
			int k = (int) (Math.random() * rooms.size());
			////System.out.println("k: " + k);
			Room r = (Room) rooms.get(k);
			if (r.getRoomQuest() == null && (!r.isStart())) {
				Monster mon = (Monster) monsters.removeFirst();
				r.figureEnters(mon,0);
			}
		}
	}

	public void addMonster(Monster m) {
		Room r = getRandomRoom();
		boolean ok =
			((r.getRoomQuest() == null) && (!r.isStart()) && (!r.hasStatue()));
		while (!ok) {
			r = getRandomRoom();
			ok =
				((r.getRoomQuest() == null)
					&& (!r.isStart())
					&& (!r.hasStatue()));

		}
		r.figureEnters(m,0);
	}

	public void addMonsterToList(Monster m) {
		monsters.add(m);
	}
	public void addMonsterToList(List<Figure> l) {
		monsters.addAll(l);
	}

	public void setMonster() {
		//M�sste jetzt hier irgendwelche Monster reinsetzen	
		for (int i = 0; i < monsters.size(); i++) {
			addMonster((Monster) monsters.get(i));
		}
	}

	public boolean makeArea(Dimension d) {
		Dimension dim;
		if (d == null) {
			dim = makeDimension();
		} else {
			dim = d;
		}
		//System.out.println("angestrebte Dimension: " + dim.toString());
		int found = tryFindHall(dim);
		if (found == Integer.MAX_VALUE) {
			//System.out.println("alles perfekt!");
		} else if (found == -2) {
			//System.out.println("keine restlichen R�ume gefunden");
		} else if (found == -1) {
			//System.out.println("Halle 0 x 0 !!!");
		} else {
			//System.out.println("maximale Breite w�re: " + found);
			int anz = dim.height * dim.width;
			dim = new Dimension(anz / found, found);
			return makeArea(dim);

		}
		//System.out.println("Halle hat " + rooms.size() + " R�ume!");
		if (rooms.size() == 0) {
			//System.out.println("returning false");
			return false;
		}
		if (found == Integer.MAX_VALUE) {
			claimRooms();
			makeDoors();
		} else {
			return false;
		}

		////System.out.println("makeArea: " + found);
		return true;

	}

	public void makeDoors() {
		for (int i = 0; i < rooms.size(); i++) {
			Room r = (Room) rooms.get(i);
			////System.out.println("Raum: "+r.toString());
			List<Room> neighbours = d.getNeighbourRooms(r);
			for (int j = 0; j < neighbours.size(); j++) {
				Room n = (Room) neighbours.get(j);
				if ((n != null) && (n.isClaimed()) && n.getHall() == this) {
					Door d = new Door(r, n);
					////System.out.println("Setze Tuer nach: "+n.toString());
					int dir = Dungeon.getNeighbourDirectionFromTo(r, n);
					r.setDoor(d, dir, true);
				}
			}
		}
	}

	private Dimension makeDimension() {
		int a =
			((int) Math.sqrt(preferredSize)) - 1 + (int) (Math.random() * 3);
		if (a == 0) {
			a = 1;
		}
		int b = (preferredSize / a) - (int) (Math.random() * 5);
		if (b == 0) {
			b = 1;
		}
		if (a * b > (int) (((double) preferredSize) * 0.75)) {
			if (Math.random() > 0.5) {
				////System.out.println("a,b");
				return new Dimension(a, b);
			} else {
				////System.out.println("b,a");
				return new Dimension(b, a);
			}
		} else {
			return makeDimension();
		}
	}

	private int tryFindHall(Dimension dim) {
		//System.out.println("Startposition: " + startPoint.toString());
		//System.out.println(
		//	"Richtung: " + routeInstruction.dirToString(direction));

		int dirFirstCorner = RouteInstruction.turnLeft(direction);
		//System.out.println(
		//	"Richtung fuer Ecke1: "
		//		+ routeInstruction.dirToString(dirFirstCorner)
		//		+ " "
		//		+ dirFirstCorner);
		int maxDistFirstCorner = walkTo(startPoint, dirFirstCorner);
		//System.out.println("maxDistFirstCorner: " + maxDistFirstCorner);
		int dirSecondCorner = RouteInstruction.turnRight(direction);
		//System.out.println(
		//	"Richtung fuer Ecke2: "
		//		+ routeInstruction.dirToString(dirSecondCorner));

		int maxDistSecondCorner = walkTo(startPoint, dirSecondCorner);
		//System.out.println("maxDistSecondCorner: " + maxDistSecondCorner);

		int[] hallResult;
		if (maxDistFirstCorner + maxDistSecondCorner + 1 >= dim.height) {
			hallResult =
				tryFindEnoughRows(dim, maxDistFirstCorner, maxDistSecondCorner);
			//System.out.println();
			//System.out.println("Gefundene Halle: ");
			//System.out.println("EckenEntfernung: " + hallResult[3]);
			//System.out.println("breite: " + hallResult[0]);
			//System.out.println("tiefe: " + hallResult[1]);
			//System.out.println();
			if (hallResult[0] == 0 || hallResult[1] == 0) {
				//System.out.println(
				//	"keine gueltige HALLE! - kann eigentlich nicht passiern ");
				return -1;
			}
		} else {
			//Hier m�sste dann mit ner anderen Dimension gesucht werden
			//System.out.println(
			//	"Es konnte in der Breite nicht ausreichend Platz gefunden werden!");
			return maxDistFirstCorner + maxDistSecondCorner + 1;
		}
		generateRoomList(hallResult);
		//if (hallResult[2] == 0) {
		int rawRoomCnt = hallResult[0] * hallResult[1];
		//System.out.println("Anzahl R�um der GrundHalle: " + rawRoomCnt);
		int differenz = preferredSize - rawRoomCnt;
		if (differenz > 0) {
			//System.out.println("m�sste zusa�tzliche suche: " + differenz);

			List<Room> additional = searchAdditionalRoom(differenz, hallResult);
			if (additional != null) {
				rooms.addAll(additional);
			} else {
				return -2;
			}
		}
		//}

		return Integer.MAX_VALUE;
	}

	private void generateRoomList(int[] hallResult) {
		JDPoint firstCorner =
			JDPoint.walkDir(
				startPoint,
				RouteInstruction.turnLeft(direction),
				hallResult[3]);
		for (int i = 0; i < hallResult[1]; i++) {
			for (int j = 0; j < hallResult[0]; j++) {
				JDPoint toAddTemp = JDPoint.walkDir(firstCorner, direction, i);
				JDPoint toAdd =
					JDPoint.walkDir(
						toAddTemp,
						RouteInstruction.turnRight(direction),
						j);
				Room r = d.getRoom(toAdd);
				if (r != null) {
					rooms.add(r);
				} else {
					//System.out.println("Es wurde ein null-raum genommen!");
				}
			}
		}

	}

	private List<Room> searchAdditionalRoom(int cnt, int[] hallResult) {
		List<Room> l = new LinkedList<Room>();
		Room r = getRimRoom(0);
		boolean newRoom = true;
		int running = 0;
		while (cnt > 0) {
			running++;
			if (running > 100) {
				//System.out.println(
				//	"Es konnten keine weiteren R�ume fuer die Halle gefunden werden!");
				break;
			}
			if (newRoom) {

			} else {
				r = getRimRoom(0);
			}
			if (r == null) {
				return null;
			}
			List<Room> neighbours = d.getNeighbourRooms(r);
			newRoom = false;
			for (int i = 0; i < neighbours.size(); i++) {
				Room n = (Room) neighbours.get(i);
				if ((!n.isClaimed())
					&& (!rooms.contains(n))
					&& (!l.contains(n))
					&& cnt > 0) {
					//um sicher zu gehen m�sste ich n 
					////System.out.println(
					//	"fuege Raum zus�tzlich hinzu: " + n.toString());
					l.add(n);
					
					cnt--;
					r = n;
					newRoom = true;
				}
			}

		}

		return l;
	}
	private int[] tryFindEnoughRows(
		Dimension dim,
		int maxDistFirstCorner,
		int maxDistSecondCorner) {
		//System.out.println();
		//System.out.println();
		//System.out.println("Beginne tryFindRows");
		//System.out.println();
		//System.out.println("MaxDistFirstCorner: " + maxDistFirstCorner);
		//System.out.println("MaxDistSecondCorner: " + maxDistSecondCorner);
		int res[] = new int[4];
		int maxKres[] = new int[4];
		int b = dim.width;
		//System.out.println("Tiefe des zu suchenden Raumes: " + b);
		int a = dim.height;
		//System.out.println("Breite des zu suchenden Raumes: " + a);
		int aHalf = a / 2;
		//System.out.println("A-Halbe: " + aHalf);

		JDPoint possibleFirstCorner =
			JDPoint.walkDir(
				startPoint,
				RouteInstruction.turnLeft(direction),
				aHalf);

		//System.out.println(
		//	"moegliche erste Ecke :" + possibleFirstCorner.toString());

		JDPoint possibleSecondCorner =
			JDPoint.walkDir(
				possibleFirstCorner,
				RouteInstruction.turnRight(direction),
				a - 1);

		boolean corners =
			testCorners(
				startPoint,
				possibleFirstCorner,
				possibleSecondCorner,
				maxDistFirstCorner,
				maxDistSecondCorner);
		//System.out.println(
		//	"moegliche zweite Ecke :" + possibleSecondCorner.toString());
		int k = 0;
		int maxK = 0;
		if (corners) {
			k = freeRows(possibleFirstCorner, possibleSecondCorner, a);
			//System.out.println("Reihen gefunden: " + k);
			if (k >= b) {
				//System.out.println("k >= b  -  --> genug Reihen!");
				//if (aHalf <= maxDistFirstCorner
				//	&& (a - aHalf - 1 <= maxDistSecondCorner)) {
				//System.out.println("Bedingungen passen");
				res[0] = a; //Breite der Halle
				res[1] = b; //Tiefe der Halle
				res[2] = 1; //boolean gro� genug
				res[3] = aHalf; //distanceFirstCorner
				//System.out.println("Standard a/2-Dist erfolgreich!");
				return res;
				//}
			} else {
				maxK = k;
				maxKres[0] = a;
				maxKres[1] = k;
				maxKres[2] = 0;
				maxKres[3] = aHalf;
			}
		} else {
			//System.out.println("Diese ecken nicht erfolgreich	");
		}
		//System.out.println(
		//	"Erstes nicht erfolgreich, versuche nach unten zu rutschen: ");
		int down = 1;
		boolean bool1 = (down + aHalf <= a - 1);
		//System.out.println("(down + aHalf <= a - 1) : " + bool1);
		boolean bool2 = (down + aHalf <= maxDistFirstCorner);
		//System.out.println("(down + aHalf <= maxDistFirstCorner) :" + bool2);
		while (bool1 && bool2) {
			//System.out.println("nach unten: " + down);

			possibleFirstCorner =
				JDPoint.walkDir(
					startPoint,
					RouteInstruction.turnLeft(direction),
					aHalf + down);
			//System.out.println(
			//	"moegliche erste Ecke :" + possibleFirstCorner.toString());

			possibleSecondCorner =
				JDPoint.walkDir(
					possibleFirstCorner,
					RouteInstruction.turnRight(direction),
					a - 1);
			//System.out.println(
			//	"moegliche zweite Ecke :" + possibleSecondCorner.toString());
			corners =
				testCorners(
					startPoint,
					possibleFirstCorner,
					possibleSecondCorner,
					maxDistFirstCorner,
					maxDistSecondCorner);

			if (corners) {
				k = freeRows(possibleFirstCorner, possibleSecondCorner, a);
				//System.out.println("Gefundene Reihen: " + k);
				//System.out.println("Kriterien: ");
				boolean b1 = k >= b;
				//System.out.println("(k >= b) : " + b1);
				boolean b2 = (aHalf + down <= maxDistFirstCorner);
				//System.out.println(
				//	"(aHalf + down <= maxDistFirstCorner) : " + b2);
				boolean b3 = (a - aHalf - down - 1 <= maxDistSecondCorner);
				//System.out.println(
				//	"(a-aHalf-down -1<= maxDistSecondCorner) : " + b3);
				if (b1 && b2 && b3) {
					//System.out.println("passt");
					res[0] = a;
					res[1] = b; //Tiefe der Halle
					res[2] = 1; //boolean gro� genug
					res[3] = aHalf + down;
					//System.out.println("erfolgreich!");
					return res;
				} else {
					if (k > maxK) {
						maxK = k;
						maxKres[0] = a;
						maxKres[1] = k;
						maxKres[2] = 0;
						maxKres[3] = aHalf + down;
					}
				}

			} else {
				//System.out.println("diese ecken nicht erfolgreich");
			}
			down++;
			bool1 = (down + aHalf <= a - 1);
			//System.out.println("(down + aHalf <= a - 1) : " + bool1);
			bool2 = (down + aHalf <= maxDistFirstCorner);
			//System.out.println(
			//	"(down + aHalf <= maxDistFirstCorner) :" + bool2);
		}

		//System.out.println(
		//	"Nach unten verschieben nicht erfolgreich, schiebe nach oben: ");
		int up = 1;
		boolean up_1 = (up <= aHalf + 1);
		//System.out.println("(up <= aHalf) :" + up_1);
		boolean up_2 = (up + a - aHalf - 1 <= maxDistSecondCorner);
		//System.out.println(
		//	"(up + a - aHalf -1<= maxDistSecondCorner) :" + up_2);
		while (up_1 && up_2) {
			//System.out.println("nach oben: " + down);

			possibleFirstCorner =
				JDPoint.walkDir(
					startPoint,
					RouteInstruction.turnLeft(direction),
					aHalf - up);
			//System.out.println(
			//	"moegliche erste Ecke :" + possibleFirstCorner.toString());

			possibleSecondCorner =
				JDPoint.walkDir(
					possibleFirstCorner,
					RouteInstruction.turnRight(direction),
					a - 1);

			//System.out.println(
			//	"moegliche zweite Ecke :" + possibleSecondCorner.toString());
			corners =
				testCorners(
					startPoint,
					possibleFirstCorner,
					possibleSecondCorner,
					maxDistFirstCorner,
					maxDistSecondCorner);

			if (corners) {
				k = freeRows(possibleFirstCorner, possibleSecondCorner, a);
				//System.out.println("Gefundene Reihen: " + k);
				//System.out.println("Kriterien: ");
				boolean b1 = k >= b;
				//System.out.println("(k >= b) : " + b1);
				boolean b2 = (aHalf - up <= maxDistFirstCorner);
				//System.out.println(
				//	"(aHalf + down <= maxDistFirstCorner) : " + b2);
				boolean b3 = (a - aHalf + up - 1 <= maxDistSecondCorner);
				//System.out.println(
				//	"(a-aHalf-down -1<= maxDistSecondCorner) : " + b3);
				if (b1 && b2 && b3) {
					//System.out.println("passt!");

					res[0] = a;
					res[1] = b; //Tiefe der Halle
					res[2] = 1; //boolean gro� genug
					res[3] = aHalf - up;
					return res;
				} else {
					if (k > maxK) {
						maxK = k;
						maxKres[0] = a;
						maxKres[1] = k;
						maxKres[2] = 0;
						maxKres[3] = aHalf - up;
					}

				}
			}
			up++;
			up_1 = (up <= aHalf);
			//System.out.println("(up <= aHalf) :" + up_1);
			up_2 = (up + a - aHalf - 1 <= maxDistSecondCorner);
			//System.out.println(
			//	"(up + a - aHalf <= maxDistSecondCorner) :" + up_2);
		}
		//System.out.println(
		//	"Alles nicht erfolgreich, mache so gro� wie m�glich!");
		return maxKres;
	}

	private boolean testCorners(
		JDPoint start,
		JDPoint p1,
		JDPoint p2,
		int maxDistFirstCorner,
		int maxDistSecondCorner) {
		//boolean c1 = testRoom(d.getRoom(p1));
		boolean c1 = JDPoint.getAbsMaxDistXY(start, p1) <= maxDistFirstCorner;
		//System.out.println("erste Ecke im Bereich: " + c1);
		//boolean c2 = testRoom(d.getRoom(p2));
		boolean c2 = JDPoint.getAbsMaxDistXY(start, p2) <= maxDistSecondCorner;
		//System.out.println("zweite Ecke im Bereich: " + c2);
		return c1 && c2;
	}

	private int freeRows(JDPoint firstCorner, JDPoint secondCorner, int length) {
		int k = 0;
		boolean valid =
			testRow(
				JDPoint.walkDir(firstCorner, direction, 1),
				RouteInstruction.turnRight(direction),
				length - 1);
		while (valid) {
			k++;
			valid =
				testRow(
					JDPoint.walkDir(firstCorner, direction, k + 1),
					RouteInstruction.turnRight(direction),
					length - 1);

		}
		return k + 1; //TEST
	}

	private boolean testRow(JDPoint start, int dir, int dist) {
		//System.out.println("Untersuche Reihe: ");
		//System.out.println("Startpunkt: " + start.toString());
		//System.out.println("Richtung: " + dir);
		//System.out.println("laenge: " + dist);
		int k = 0;
		Room toTest = d.getRoom(start);
		if (toTest == null) {
			//System.out.println("Reihe au�erhalb vom dungeon!");
			return false;
		}
		boolean valid = testRoom(toTest);
		while (valid && (k < dist)) {
			k++;
			toTest = d.getRoom(JDPoint.walkDir(toTest.getNumber(), dir, 1));
			valid = testRoom(toTest);
			if (valid == false) {
				//System.out.println("Raum in Reihe ungueltig!");
			}
		}
		return valid;
		//		if (k == dist) {
		//			return true;
		//		} else {
		//			return false;
		//		}

	}

	private int walkTo(JDPoint start, int dir) {
		Room startRoom = d.getRoom(start);
		int k = 0;
		Room toTest = d.getRoomAt(startRoom, dir);
		boolean valid = testRoom(toTest);
		while (valid) {
			k++;
			toTest = d.getRoomAt(toTest, dir);
			valid = testRoom(toTest);

		}

		return k;
	}

	private boolean testRoom(Room r) {
		if (r == null) {
			return false;
		}
		////System.out.println(
		//	"testing room: " + r.toString() + " " + !r.isClaimed());
		return !r.isClaimed();
	}

	public Room getNorthestRoom() {
		int n = 1000;
		Room northest = null;
		for (int i = 0; i < rooms.size(); i++) {
			Room r = (Room) rooms.get(i);
			int y = r.getNumber().getY();
			if (y < n
				&& (!d.getRoomAt(r, RouteInstruction.NORTH).isClaimed())) {
				n = y;
				northest = r;
			}
		}
		return northest;
	}

	/**
	 * Returns the name.
	 * @return String
	 * 
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

}
