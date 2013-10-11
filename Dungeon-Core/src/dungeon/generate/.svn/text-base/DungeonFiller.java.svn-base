/**
 * Abstrakte Oberklasse zum Fuellen eines Dungeons. 
 * Sie stellt eine Reihe von Werkzeugen zum gestalten des Dungeons zur
 * Verfuegung, wie Erstellung von Monstern, Schluesseln und Schreinen,
 * Methoden zum setzen und entfernen von Tueren und Monstern und 
 * einen Algorithmus der die ein Netzwerk von Raeumen auf Zusammenhaengigkeit testet. 
 *
 */

package dungeon.generate;

import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import game.Game;
import item.DustItem;
import item.HealPotion;
import item.Item;
import item.ItemPool;
import item.Key;
import item.quest.Rune;

import java.util.LinkedList;

import shrine.Shrine;
import shrine.Statue;
import util.Arith;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RouteInstruction;

//import JDMonster.*;
public abstract class DungeonFiller {

	public static int BEAR = 1;
	public static int GHUL = 2;
	public static int OGRE = 3;
	public static int ORC = 4;
	public static int SKELETON = 5;
	public static int WOLF = 6;

	/**
	 * 
	 * @uml.property name="d"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Dungeon d;

	int value;

	/**
	 * 
	 * @uml.property name="game"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	Game game;

	/**
	 * 
	 * @uml.property name="runen"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	Rune[] runen;

	int level;
	int[][] map;

	/**
	 * 
	 * @uml.property name="keyList"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="item.Key"
	 */
	LinkedList keyList = new LinkedList();

	String[] keyStrings =
		{
			"Kupfer",
			"Eisen",
			"Silber",
			"Gold",
			"Platin",
			"Bronze",
			"Blech",
			"Stahl",
			"Piponium" };

	

	public DungeonFiller(
		Dungeon d,
		int value,
		Game game,
		Rune[] runen,
		int level) {
		this.d = d;
		this.value = value;
		this.game = game;
		this.runen = runen;
		this.level = level;
		this.map = getMap();
//		if (map == null) {
//			this.map = this.map2;
//		}
		//setWalls();
		//setDoors();
		//unsetDoors();
		for (int i = 0; i < keyStrings.length; i++) {

			keyList.add(new Key(keyStrings[i] + " groß"));
			keyList.add(new Key(keyStrings[i] + " klein"));
		}

	}

	public Key getNextKey() {
		if (keyList.size() > 0) {
			int i = (int) (Math.random() * keyList.size());
			Key k = (Key) keyList.get(i);
			keyList.remove(i);
			//System.out.println(keyList.size()+" Schl�ssel noch uebrig!");
			return k;
		} else {
			//System.out.println("Kein SChl�ssel mehr ueberig!");
			return null;
		}
	}

	protected abstract int[][] getMap();

	protected void plugShrine(Shrine s, Room r) {
		d.addShrine(s);
		r.setShrine(s, true);

	}

	/**
	 * 
	 * @uml.property name="d"
	 */
	public Dungeon getDungeon() {
		return d;
	}

	protected void makeRandomLabyrinth(int sector, int cnt) {
		//cnt ist wieviel Tueren versucht werden wegzumachen

		int undos = 0;
		while (cnt > 0) {

			JDPoint p1 = this.getPoint(sector);
			Room r1 = d.getRoom(p1);
			int k = 1 + ((int) Math.random() * 4);
			Door d1 = r1.getDoor(k);
			if (d1 != null) {

				int cnt1 = validateNetCnt(r1, false);
				r1.removeDoor(d1, true);
				int cnt2 = validateNetCnt(r1, false);

				if (cnt1 != cnt2) {
					undoDoorRemove(r1, k, d1);
					undos++;
				} else {
					//System.out.println("Door removed! " + d1.toString());
					cnt--;
					undos = 0;
				}
				if (undos > 20) {
					break;
				}
			}
		}
	}

	
	protected void undoDoorRemove(Room r1, int dir, Door d1) {
		r1.setDoor(d1, dir, true);
	}

	protected boolean validateNet(boolean print, int u) {
		LinkedList hash = new LinkedList();
		Room first = d.getRoomNr(18, 39);
		hash.add(first);
		int k = 0;
		while (k < hash.size()) {
			Room r = (Room) hash.get(k);

			Door[] doors = r.getDoors();
			for (int i = 0; i < 4; i++) {
				if (doors[i] != null) {
					Room a = doors[i].getOtherRoom(r);
					if (!hash.contains(a)) {
						hash.add(a);
					}
				}
			}
			k++;

		}
		int l = hash.size();
		//if(print) {

		//System.out.println("Anzahl erreichbarer R�ume: " + hash.size());
		//}
		if (l == 375) {
			return true;
		} else {
			return false;
		}

	}
	
	protected void printList(LinkedList l) {
//		for(int i = 0; i < l.size(); i++) {
//			System.out.println(((room)l.get(i)).getNumber().toString());
//		}
//		System.out.println("-----");
	}

	protected int validateNetCnt(Room startRoom, boolean print) {
		LinkedList hash = new LinkedList();
		Room first = startRoom;
		hash.add(first);
		int k = 0;
		while (k < hash.size()) {
			if(print) {
			printList(hash);
			}
			Room r = (Room) hash.get(k);
			if (r.getShrine() != null
				&& (r.getShrine() instanceof Statue)) {
					//System.out.println("ValidateCnt: StatueRoom wird nicht expandiert!");
					
			} else {
				Door[] doors = r.getDoors(); 
				for (int i = 0; i < 4; i++) {
					if (doors[i] != null) {
						Room a = doors[i].getOtherRoom(r);
						if (!hash.contains(a)) {
							hash.add(a);
						}
					}
				}
			}
			k++;

		}
		int l = hash.size();
		if (print) {

			//System.out.println("Anzahl erreichbarer R�ume: " + hash.size());
		}
		return l;

	}

	public abstract void fillDungeon() throws DungeonGenerationFailedException;
	//		{
	//		
	//		
	//		int value1 = 500;
	//		setMonsters1(value1);
	//		int value2 = 1200;
	//		setMonsters2(value2);
	//		int dy = (int) (Math.random() * 3);
	//		int dx = (int) (Math.random() * 8);
	//
	//		point ph = new point(12 + dx, 9 + dy);
	//		//System.out.println("healthfountain auf: " + ph.toString());
	//		room rx = d.getRoom(ph);
	//
	//		shrine fntn = new health_fountain(30, 1, ph);
	//		plugShrine(fntn,rx);
	//		int value3 = 1000;
	//		int a = (int) (Math.random() * 3);
	//		int b = (int) (Math.random() * 2);
	//		if (a == 0) {
	//			setMonsters3(value3, ORC, 5);
	//			if (b == 0) {
	//				setMonsters4(value3, WOLF, 5);
	//				setMonsters5(value3, SKELETON, 5);
	//			} else {
	//				setMonsters4(value3, SKELETON, 5);
	//				setMonsters5(value3, WOLF, 5);
	//			}
	//		} else if (a == 1) {
	//			setMonsters3(value3, SKELETON, 5);
	//			if (b == 0) {
	//				setMonsters4(value3, WOLF, 5);
	//				setMonsters5(value3, ORC, 5);
	//			} else {
	//				setMonsters4(value3, ORC, 5);
	//				setMonsters5(value3, WOLF, 5);
	//			}
	//		} else if (a == 2) {
	//			setMonsters3(value3, WOLF, 5);
	//			if (b == 0) {
	//				setMonsters4(value3, ORC, 5);
	//				setMonsters5(value3, SKELETON, 5);
	//			} else {
	//				setMonsters4(value3, SKELETON, 5);
	//				setMonsters5(value3, ORC, 5);
	//			}
	//		}
	//
	//		int value6 = 1500;
	//		setMonsters6(value6);
	//		int value7 = 1000;
	//		setMonsters7(value7);
	//
	//		shrine[] finder = new shrine[runen.length];
	//		//System.out.println("Runen: "+runen.length);
	//		for (int i = 0; i < runen.length; i++) {
	//			//System.out.println("vor");
	//			point p = getPoint( 7);
	//			//System.out.println("nach");
	//			finder[i] = new runeFinder_shrine(runen[i], p);
	//			room r = d.getRoom(p);
	//			plugShrine(finder[i],r);
	//			//System.out.println("vor monster gefunden!");
	//			monster m = getMonsterIn(7, 0);
	//			//System.out.println("Monster gefunden: " + m.toString());
	//			m.addItem(runen[i], null);		}
	//
	//		quest q = new encompass_quest(d.getRoomNr(1, 1), d, 3, game);
	//		
	//		//System.out.println("dungeon fertig gefuellte!");
	//	}
	//
	protected void setMonsters1(int value1) {

		//Einen orc zuf�llig in Sektor 1
		JDPoint orc1P = getPoint(1);
		Orc orc1 = new Orc(value1,d);
		equipMonster(orc1, 3);
		d.getRoom(orc1P).figureEnters(orc1,0);

		//Einen wolf zuf�llig in Sektor 1
		JDPoint wolf1P = getPoint(1);
		Wolf wolf1 = new Wolf(value1,d);
		equipMonster(wolf1, 3);
		d.getRoom(wolf1P).figureEnters(wolf1,0);

		//Ein skellet zuf�llig in Sektor 1
		JDPoint skeleton1P = getPoint(1);
		Skeleton skeleton1 = new Skeleton(value1,d);
		equipMonster(skeleton1, 3);
		d.getRoom(skeleton1P).figureEnters(skeleton1,0);

	}

	protected int getSectorRoomsCount(int sector) {

		int cnt = 0;
		for (int i = 0; i < d.getSize().getX(); i++) {
			for (int j = 0; j < d.getSize().getY(); j++) {
				int k = map[j][i] / 10;
				if (k == sector) {
					cnt++;
				}
			}
		}
		return cnt;
	}

	protected void setMonsters2(int value2) {

		//Einen Oger
		JDPoint ogre1P = getPoint(2);
		Ogre ogre1 = new Ogre(value2);
		equipMonster(ogre1, 3);
		d.getRoom(ogre1P).figureEnters(ogre1,0);

		//einen B�ren
		JDPoint bear1P = getPoint(2);
		Spider bear1 = new Spider(value2);
		equipMonster(bear1, 3);
		d.getRoom(bear1P).figureEnters(bear1,0);

		//einen Ghul
		JDPoint ghul1P = getPoint(2);
		Ghul ghul1 = new Ghul(value2);
		equipMonster(ghul1, 3);
		d.getRoom(ghul1P).figureEnters(ghul1,0);

		createRandomMonster(2, 1, 12, 1200, 800);

	}

	protected void setMonsters3(int value3, int monster, int count) {
		for (int i = 0; i < count; i++) {
			JDPoint p = getPoint(3);
			createMonster(monster, value3, p);
		}

	}

	protected void setMonsters4(int value4, int monster, int count) {
		for (int i = 0; i < count; i++) {
			JDPoint p = getPoint(4);
			createMonster(monster, value4, p);
		}

	}
	protected void setMonsters5(int value5, int monster, int count) {
		for (int i = 0; i < count; i++) {
			JDPoint p = getPoint(5);
			createMonster(monster, value5, p);
		}

	}
	protected void setMonsters6(int value6) {
		for (int i = 0; i < 3; i++) {
			JDPoint p = getPoint(6);
			Monster m = getBigMonster(value6);
			equipMonster(m, 3);
			d.getRoom(p).figureEnters(m,0);
		}
	}

	protected void setMonsters7(int value6) {
		////System.out.println("setMonsters7");
		createRandomMonster(7, 0, 40, 1000, 1500);
		////System.out.println("setMosters7 fertig");

	}

	protected void createMonster(int type, int value, JDPoint p) {
		Monster m = null;
		if (type == BEAR) {
			m = new Spider(value);
		} else if (type == GHUL) {
			m = new Ghul(value);
		} else if (type == OGRE) {
			m = new Ogre(value);
		} else if (type == ORC) {
			m = new Orc(value, d);
		} else if (type == SKELETON) {
			m = new Skeleton(value);
		} else if (type == WOLF) {
			m = new Wolf(value);
		}
		equipMonster(m, 3);
		d.getRoom(p).figureEnters(m,0);
	}

	protected void createRandomMonster(int sector,
	//Der Sektor wo die zuf�lligen Monster in einen zuf�lligen Raum reinkommen
	int size, //Code fuer die groe�e (gro�/klein) 1=kleine 2= grosse 0=zuf�llig
	int count, //wieviele Monster
	int bigValue, //wie stark die grossen sein sollen
	int smallValue //wie stark die kleinen sein sollen
	) {

		if (size == 0) {
			for (int i = 0; i < count; i++) {
				JDPoint p = getPoint(sector);
				Monster m = null;

				if (Math.random() * 10 < 6) {

					m = getSmallMonster(smallValue);

				} else {
					m = getBigMonster(bigValue);
				}
				equipMonster(m, 3);
				d.getRoom(p).figureEnters(m,0);

			}
		} else if (size == 1) {
			for (int i = 0; i < count; i++) {
				JDPoint p = getPoint(sector);
				Monster m = getSmallMonster(smallValue);
				equipMonster(m, 3);
				d.getRoom(p).figureEnters(m,0);
			}
		}
		if (size == 2) {
			for (int i = 0; i < count; i++) {
				JDPoint p = getPoint(sector);
				Monster m = getBigMonster(smallValue);
				equipMonster(m, 3);
				d.getRoom(p).figureEnters(m,0);
			}
		}

	}

	//Gibt zuf�llig einen B�ren, Ghul oder Oger zur�ck
	public Monster getBigMonster(int value) {
		int k = (int) (Math.random() * 3);
		if (k == 0)
			return new Spider(value);
		if (k == 1)
			return new Ogre(value);
		else
			return new Ghul(value);

	}

	public static Monster getBigMonster(int value, Game game) {
		int k = (int) (Math.random() * 3);
		if (k == 0)
			return new Spider(value);
		if (k == 1)
			return new Ogre(value);
		else
			return new Ghul(value);

	}

	//gibt zuf�llig einen wolf, orc oder skellet zur�ck
	public Monster getSmallMonster(int value) {
		int k = (int) (Math.random() * 3);
		if (k == 0)
			return new Wolf(value);
		if (k == 1)
			return new Orc(value);
		else
			return new Skeleton(value);
	}
	private void setWalls() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				JDPoint p = d.getPoint(j, i);
				////System.out.println("Punkt: "+ p.toString());

				if (map[i][j] == 0) {
					d.getRoom(p).setIsWall(true);
				}
			}
		}
	}

	private void setDoors() {
		for (int i = 0; i < d.getSize().getX(); i += 2) {
			for (int j = 0; j < d.getSize().getY(); j += 2) {
				Room r = d.getRoomNr(i, j);
				if (r != null) {
					setDoors(r);
				}
			}
		}
		for (int i = 1; i < d.getSize().getX(); i += 2) {
			for (int j = 1; j < d.getSize().getY(); j += 2) {
				Room r = d.getRoomNr(i, j);
				if (r != null) {
					setDoors(r);
				}
			}
		}

	}
	private void unsetDoors() {
		//System.out.println(map.length + " : " + map[0].length);
		for (int i = 0; i < map[0].length; i++) {
			for (int j = 0; j < map.length; j++) {
				int k = map[j][i] % 10;
				if (k == 1) {
					d.getRoomNr(i, j).removeDoor(RouteInstruction.EAST, true);
				}
				if (k == 2) {
					d.getRoomNr(i, j).removeDoor(RouteInstruction.SOUTH, true);
				}
				if (k == 3) {
					d.getRoomNr(i, j).removeDoor(RouteInstruction.EAST, true);
					d.getRoomNr(i, j).removeDoor(RouteInstruction.SOUTH, true);
				}
			}
		}
	}

	private void setDoors(Room r) {
		//Nordlicher Raum
		Room north =
			d.getRoomNr(r.getNumber().getX(), r.getNumber().getY() - 1);
		if (north != null) {
			Door d1 = new Door(r, north);
			r.setDoor(d1, RouteInstruction.NORTH, false);
			north.setDoor(d1, RouteInstruction.SOUTH, false);
		}
		Room east = d.getRoomNr(r.getNumber().getX() + 1, r.getNumber().getY());
		if (east != null) {
			Door d1 = new Door(r, east);
			r.setDoor(d1, RouteInstruction.EAST, false);
			east.setDoor(d1, RouteInstruction.WEST, false);
		}
		Room south =
			d.getRoomNr(r.getNumber().getX(), r.getNumber().getY() + 1);
		if (south != null) {
			Door d1 = new Door(r, south);
			r.setDoor(d1, RouteInstruction.SOUTH, false);
			south.setDoor(d1, RouteInstruction.NORTH, false);
		}
		Room west = d.getRoomNr(r.getNumber().getX() - 1, r.getNumber().getY());
		if (west != null) {
			Door d1 = new Door(r, west);
			r.setDoor(d1, RouteInstruction.WEST, false);
			west.setDoor(d1, RouteInstruction.EAST, false);
		}
	}

	/**
	 * Ruestet ein Monster eventuell mit zuf�lligen Gegenst�nden aus.
	 * Die wahrscheinlchkeit, dass ein Monster etwas bekommt ist 1/rate.
	 * rate = 1 => monster bekommt immer was
	 * rate = 3 => jedes dritte bekommt was
	 * @param m
	 * @param rate
	 */
	public void equipMonster(Monster m, int rate) {
		//itemwert = Wurzel(monsterwert) mal einhalb

		if (Math.random() * rate < 1) { //jedes dritte Monster hat was
			int value = (int) (0.32 * Math.sqrt(m.getWorth()));
			//Wert des Monsters hoch 3/8
			//int value =  (int) (1 *(Math.sqrt(Math.sqrt(Math.sqrt(m.getWorth()*m.getWorth()*m.getWorth())))));

			double scattered = Arith.gauss(value, ((double)value)/5);
			double quotient = scattered / value;
			Item i = ItemPool.getRandomItem((int) scattered, quotient);
			if ((i != null) && (scattered > 0)) {
				////System.out.println(i.toString());
				m.takeItem(i, null);
			}
		}
		if (Math.random() * 3 < 1) {

			int value = (int) (0.2 * Math.sqrt(m.getWorth()));
			Item i = new DustItem(value);
			if (i != null) {
				////System.out.println(i.toString());
				m.takeItem(i, null);
			}
		}
		if (Math.random() * 3 < 0.7) {
			int value = (int) (0.3 * Math.sqrt(m.getWorth()));
			Item i = new HealPotion(value);
			if (i != null) {
				////System.out.println(i.toString());
				m.takeItem(i, null);
			}
		}
	}

	public static void equipAMonster(Monster m) {
		//itemwert = Wurzel(monsterwert) mal einhalb
		if (Math.random() * 3 < 1) { //jedes zweite Monster hat was
			int value = (int) (0.35 * Math.sqrt(m.getWorth()));
			double scattered = Arith.gauss(value, ((double)value)/5);
			double quotient = scattered / value;
			Item i = ItemPool.getRandomItem((int) scattered, quotient);
			if ((i != null) && (scattered > 0)) {
				////System.out.println(i.toString());
				m.takeItem(i, null);
			}
		}
		if (Math.random() * 3 < 1) {
			int value = (int) (0.2 * Math.sqrt(m.getWorth()));
			Item i = new DustItem(value);
			if (i != null) {
				////System.out.println(i.toString());
				m.takeItem(i, null);
			}
		}
		if (Math.random() * 2 < 1) {
			int value = (int) (0.3 * Math.sqrt(m.getWorth()));
			Item i = new HealPotion(value);
			if (i != null) {
				////System.out.println(i.toString());
				m.takeItem(i, null);
			}
		}
	}

	protected JDPoint getPoint(int sector) {
		LinkedList points = new LinkedList();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				int sec = map[i][j] / 10;
				////System.out.print(sector+ " ");
				////System.out.println(sec);
				if (sec == sector) {
					////System.out.println("adding point!");
					points.add(d.getPoint(j, i));
				}
			}
		}
		int k = (int) (Math.random() * points.size());
		if (points.size() == 0) {
			//System.out.println(
			//	"Keine Felder dieses Sektors gefunden! " + sector);
		}
		JDPoint p = (JDPoint) points.get(k);

		if ((p.getX() == d.getHeroPosition().getX())
			&& (p.getY() == d.getHeroPosition().getY())) {
			p = getPoint(sector);
		}
		return p;
	}

	protected void giveItem(Monster m, int k) {

	}

	protected Monster getMonsterIn(int sector, int cnt) {
		if (cnt > 100) {
			//System.out.println(
			//	"Kein Monster in Sektor "
			//		+ sector
			//		+ " in 1000000 Versuchen gefunden!");
			return null;
		}
		int a = 0;
		int b = 0;

		while (map[a][b] / 10 != sector) {
			cnt++;
			a = (int) (Math.random() * map.length);
			b = (int) (Math.random() * map[0].length);
		}

		Room r = d.getRoomNr(b, a);
		if ((r == null) || (r.getRoomFigures().size() == 0)) {
			return getMonsterIn(sector, cnt);
		} else {
			return (Monster) r.getRoomFigures().get(0);
		}

	}

//	protected Key makeKey(Door d, Room r, int dir, String key) {
//		Key k = new Key(key);
//		Door neu = new Door(d, k);
//		r.setDoor(neu, dir, true);
//		return k;
//	}

}
