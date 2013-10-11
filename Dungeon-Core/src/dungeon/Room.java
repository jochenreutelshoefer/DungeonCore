package dungeon;

import fight.Fight;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.memory.MemoryObject;
import figure.memory.RoomMemory;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import figure.percept.InfoPercept;
import figure.percept.OpticalPercept;
import figure.percept.Percept;
import figure.percept.TextPercept;
import game.ControlUnit;
import game.InfoEntity;
import game.InfoProvider;
import game.JDEnv;
import gui.Paragraph;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.interfaces.ItemOwner;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import shrine.Shrine;
import shrine.Statue;
import dungeon.generate.Hall;
import dungeon.generate.Sector;
import dungeon.quest.Quest;
import dungeon.quest.RoomQuest;


/**
 * Ein Raum des Dungeons. Kann dynamisch enthalten: Gegenstaende, Monster, Held ;
 * Kann statisch enthalten: Truhe, Schrein, Versteck, 4 Tueren;
 * 
 * Ein Raum weis zu welchen Quest, Halle, Sektor, Dungeon er gehoert Der
 * aktuelle Sichtbarkeitsstatus des Helden auf diesen Raum wird hier
 * gespeichert.
 * 
 */
public class Room /* extends JDEnv */extends DungeonWorldObject implements
		ItemOwner {

	public static final int NO = 0;

	public static final int LITTLE = 1;

	public static final int ALL = 2;

	boolean isWall = false;

	private Map<Figure,Integer> observer = new HashMap<Figure,Integer>();;

	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new RoomInfo(this, map);
	}

	public void setObserverStatus(Figure f, int vis) {
		if (vis < RoomObservationStatus.VISIBILITY_FIGURES) {
			observer.remove(f);

		} else {
			observer.put(f, new Integer(vis));
		}
	}

	public double calcFleeDiff() {
		double diff = 0;
		List<Figure> l = getFight().getFightFigures();
		for (int i = 0; i < l.size(); i++) {
			diff += ((Figure) l.get(i)).getAntiFleeValue();
		}
		if (l.size() == 2) {
			diff = diff * 4 / 5;
		} else if (l.size() == 3) {
			diff = diff * 3 / 4;
		} else if (l.size() == 4)
			diff = diff * 1 / 2;
		{

		}
		return diff;
	}

	Position[] positions = new Position[8];

	HiddenSpot spot;

	private Fight fight = null;

	private Shrine s;

	private Chest Chest;

	private Dungeon d;

	private List<Quest> quests = new LinkedList<Quest>();

	private List<Figure> roomFigures;

	private List<Item> Items;

	private final JDPoint number;

	public void checkFight(Figure movedIn) {

		boolean fight = false;
		for (Iterator<Figure> iter = roomFigures.iterator(); iter.hasNext();) {
			Figure element = (Figure) iter.next();
			ControlUnit c = element.getControl();
			if (c != null
					&& (c.isHostileTo(FigureInfo.makeFigureInfo(movedIn,
							element.getRoomVisibility())) || movedIn
							.getControl().isHostileTo(
									FigureInfo.makeFigureInfo(element, movedIn
											.getRoomVisibility())))) {
				fight = true;
			}
		}
		if (fight) {
			startFight(movedIn);
		}
	}

	private void startFight(Figure movedIn) {
		List<Figure> l = new LinkedList<Figure>();
		l.addAll(this.roomFigures);
		Comparator<Figure> comp = new MyFightOrderComparator();
		Collections.sort(l, comp);
		this.fight = new Fight(this, l);
	}

	public Position getFreePosition() {
		for (int i = 0; i < positions.length; i++) {
			if (positions[i].getFigure() == null) {
				return positions[i];
			}
		}
		return null;
	}

	public MemoryObject getMemoryObject(FigureInfo info) {
		return new RoomMemory(this,info);
	}

	public boolean monstersThere() {
		for (Iterator<Figure> iter = this.roomFigures.iterator(); iter.hasNext();) {
			Figure element = (Figure) iter.next();
			if (element instanceof Monster) {
				return true;
			}

		}
		return false;
	}

	private int visited;

	boolean start = false;

	private boolean part_scouted = false;

	private Item[] itemArray = new Item[4];


	int floorIndex;

	public String oldInfos;

	RoomQuest rquest;

	Hall hall = null;

	Sector sec = null;

	Door[] doors = new Door[4];

	public Room(int x, int y, Dungeon d) {
		this.d = d;
		roomFigures = new LinkedList<Figure>();
		Items = new LinkedList<Item>();
		number = d.getPoint(x, y);
		oldInfos = new String("");
		for (int i = 0; i < positions.length; i++) {

			positions[i] = new Position(this, i);

		}
		for (int i = 0; i < positions.length; i++) {
			int last = (i - 1) % 8;
			if (i == 0) {
				last = 7;
			}
			positions[last].setLast(positions[i]);
			positions[(i + 1) % 8].setNext(positions[i]);

		}
	}

	public boolean fightRunning() {
		return fight != null;
	}

	private void tickFigures(int round) {
		for (Iterator<Figure> i = roomFigures.iterator(); i.hasNext();) {
			Figure element = (Figure) i.next();
			element.timeTick(round);
		}
	}

	public void turn(int round) {
		tickFigures(round);

		if (fight == null) {
			turnNormal(round);
		} else {

			fight.doFight();
		}
	}

	private void turnNormal(int round) {
		List<Figure> figures = new LinkedList<Figure>();
		figures.addAll(roomFigures);
		for (Iterator<Figure> iter = figures.iterator(); iter.hasNext();) {
			if (this.d.getGame().isGameOver()) {
				break;
			}
			Figure element = (Figure) iter.next();
			element.turn(round);

		}
	}

	public void setShrine(Shrine s, boolean setShrineLocation) {

		this.s = s;
		if (setShrineLocation) {
			s.setLocation(this);
		}

	}

	public int getFloorIndex() {
		if (hall == null) {
			return floorIndex;
		}
		return hall.getFloorIndex();
	}

	// public Fighter getFighterNumber(int i) {
	// return (Fighter)dieMonster(i);
	// }

	public int[] makeDoorInfo() {
		int[] infos = new int[4];
		for (int i = 0; i < 4; i++) {
			infos[i] = Door.NO_DOOR;
			if (doors[i] != null) {
				if (doors[i].hasLock()) {
					if (doors[i].getLocked()) {
						infos[i] = Door.DOOR_LOCK_LOCKED;
					} else {
						infos[i] = Door.DOOR_LOCK_OPEN;
					}
				} else {
					infos[i] = Door.DOOR;
				}
			}

		}
		return infos;
	}

	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[Items.size()];
		for (int i = 0; i < Items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo((Item) Items.get(i), map);
		}
		return array;
	}

	public Item getItem(ItemInfo wrapped) {
		Item[] items = getItemArray();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (ItemInfo.makeItemInfo(items[i], null).equals(wrapped)) {
					return items[i];
				}
			}
		}
		return null;
	}

	public boolean hasStatue() {
		if ((s != null) && (s instanceof Statue)) {
			return true;
		} else {
			return false;

		}
	}

	public Item getItemNumber(int i) {
		return (Item) Items.get(i);
	}

	public boolean directionPassable(int dir) {
		if (doors[dir - 1] != null) {
			return doors[dir - 1].isPassable(null);
		}
		return false;
	}

	public boolean removeItem(Item i) {
		boolean b = false;
		if (Items.remove(i)) {
			this.remItemFromArray(i);
			b = true;
		}
		return b;
	}

	public boolean hasItem(Item i) {
		return Items.contains(i);
	}

	public Room getNeighbourRoom(int dir) {
		if (dir == 1) {
			return d.getRoom(d.getPoint(number.getX(), number.getY() - 1));
		} else if (dir == 2) {
			return d.getRoom(d.getPoint(number.getX() + 1, number.getY()));
		} else if (dir == 3) {
			return d.getRoom(d.getPoint(number.getX(), number.getY() + 1));
		} else if (dir == 4) {
			return d.getRoom(d.getPoint(number.getX() - 1, number.getY()));
		} else {
			return null;
		}

	}

	public Room getRoom() {
		return this;
	}

	public JDPoint getLocation() {
		return number;
	}

	/**
	 * 
	 * @uml.property name="rquest"
	 */
	public void setRoomQuest(RoomQuest q) {
		rquest = q;
	}

	/**
	 * 
	 * @uml.property name="rquest"
	 */
	public RoomQuest getRoomQuest() {
		return rquest;
	}

	public void setStart() {
		start = true;
	}

	/**
	 * 
	 * @uml.property name="start"
	 */
	public boolean isStart() {
		return start;
	}

	public boolean addItems(List<Item> l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (Item) (l.get(i));
			this.takeItem(it, o);
		}
		return true;
	}

	public String getDirectionString(Room other) {
		JDPoint b = other.getLocation();
		JDPoint a = number;
		int dx = a.getX() - b.getX();
		int dy = a.getY() - b.getY();
		String s = "";

		if (Math.abs(dx) <= 1) {
			if (dy < 0) {
				s += JDEnv.getResourceBundle().getString("southern");
			} else {
				s += JDEnv.getResourceBundle().getString("northern");
			}
		} else if (Math.abs(dy) <= 1) {
			if (dx < 0) {
				s += JDEnv.getResourceBundle().getString("eastern");
			} else {
				s += JDEnv.getResourceBundle().getString("western");
				;
			}
		} else {
			if (dy > 0) {
				if (dx > 0) {
					s += JDEnv.getResourceBundle().getString("northwestern");
				} else {
					s += JDEnv.getResourceBundle().getString("northeastern");
				}
			} else {
				if (dx > 0) {
					s += JDEnv.getResourceBundle().getString("southwestern");
				} else {
					s += JDEnv.getResourceBundle().getString("southeastern");
				}
			}
		}

		return s + " ";
	}

	public Item[] getItemArray() {
		LinkedList<Item> workList = new LinkedList<Item>(Items);
		// //System.out.println("workliste: " + workList.size());
		for (int i = 0; i < 4; i++) {
			boolean b;
			if (itemArray[i] != null) {
				b = Items.contains(itemArray[i]);

				if (b) {
					workList.remove(itemArray[i]);
				} else {
					itemArray[i] = null;
				}
			} else {
				boolean done = false;
				if (workList.size() == 0) {
					done = true;
					break;
				}
				Item it = (Item) workList.getFirst();
				while (itemArrayContains(it)) {
					workList.removeFirst();
					if (workList.size() == 0) {
						done = true;
						break;
					}
					it = (Item) workList.getFirst();
				}
				if (done) {
					itemArray[i] = null;
					// //System.out.println("fertig");
					break;
				}
				itemArray[i] = it;
			}

		}
		return itemArray;
	}


	public FigureInfo[] getMonsterInfoArray(DungeonVisibilityMap map) {
		FigureInfo[] mons = new FigureInfo[roomFigures.size()];
		for (int i = 0; i < roomFigures.size(); i++) {
			mons[i] = MonsterInfo.makeFigureInfo(((Figure) roomFigures.get(i)),
					map);
		}
		return mons;
	}

	public void distributePercept(Percept p) {

		if (p instanceof OpticalPercept || p instanceof TextPercept|| p instanceof InfoPercept) {
			for (Iterator<Figure> iter = observer.keySet().iterator(); iter.hasNext();) {
				Figure element = (Figure) iter.next();
				Integer visStat = (Integer) observer.get(element);
				if (visStat.intValue() >= RoomObservationStatus.VISIBILITY_FIGURES) {
					element.tellPercept(p);
				}

			}
		}
	}

	public boolean hasHero() {
		for (Iterator<Figure> iter = this.roomFigures.iterator(); iter.hasNext();) {
			Figure element = (Figure) iter.next();
			if (element instanceof Hero) {
				return true;
			}

		}
		return false;
	}

	public Figure getFighterNumber(int i) {
		return (Figure) roomFigures.get(i);
	}

	public int getItemNumber(Item it) {
		for (int i = 0; i < Items.size(); i++) {
			if (it == Items.get(i)) {
				return i;
			}

		}
		return -1;
	}

	public boolean isAccessibleNeighbour(Room r) {
		List<Room> neighbours = this.getScoutableNeighbours();
		for (int i = 0; i < neighbours.size(); i++) {
			Room n = ((Room) neighbours.get(i));
			if (n == r) {
				if (n.hasOpenConnectionTo(r)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean itemArrayContains(Item o) {
		for (int i = 0; i < itemArray.length; i++) {
			if ((itemArray[i] != null) && itemArray[i].equals(o)) {
				return true;
			}
		}
		return false;
	}

	public void setDoor(Door d, int dir, boolean otherRoom) {
		if (dir == RouteInstruction.NORTH) {
			doors[0] = d;
		}
		if (dir == RouteInstruction.EAST) {
			doors[1] = d;
		}
		if (dir == RouteInstruction.SOUTH) {
			doors[2] = d;
		}
		if (dir == RouteInstruction.WEST) {
			doors[3] = d;
		}
		if (otherRoom) {
			Room other = d.getOtherRoom(this);
			other.setDoor(d, RouteInstruction.turnOpp(dir), false);
		}
	}

	public void setDoor(Dungeon dun, int dir, boolean otherRoom) {
		Door d = new Door(this, dun.getRoomAt(this, dir));
		if (dir == RouteInstruction.NORTH) {
			doors[0] = d;
		}
		if (dir == RouteInstruction.EAST) {
			doors[1] = d;
		}
		if (dir == RouteInstruction.SOUTH) {
			doors[2] = d;
		}
		if (dir == RouteInstruction.WEST) {
			doors[3] = d;
		}
		if (otherRoom) {
			Room other = d.getOtherRoom(this);
			other.setDoor(d, RouteInstruction.turnOpp(dir), false);
		}
	}

	public Door getDoor(int dir) {
		Door d = null;
		if (dir == RouteInstruction.NORTH) {
			d = doors[0];
		}
		if (dir == RouteInstruction.EAST) {
			d = doors[1];
		}
		if (dir == RouteInstruction.SOUTH) {
			d = doors[2];
		}
		if (dir == RouteInstruction.WEST) {
			d = doors[3];
		}
		return d;
	}

	public int getDir(Door d) {
		int dir = 0;
		if (d != null) {

			if (d == doors[0]) {

				dir = RouteInstruction.NORTH;
			}
			if (d == doors[1]) {

				dir = RouteInstruction.EAST;
			}

			if (d == doors[2]) {

				dir = RouteInstruction.SOUTH;
			}
			if (d == doors[3]) {

				dir = RouteInstruction.WEST;
			}
		}
		return dir;
	}

	public Door[] getDoors() {
		return doors;
	}

	public boolean removeDoor(Door d, boolean otherRoom) {
		// //System.out.println("removing Door :
		// "+d.toString()+this.getNumber().toString()+" "+otherRoom);
		if (d == null) {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			if (doors[i] == d) {
				doors[i] = null;
				// //System.out.println("gefunden!");
				if (otherRoom) {
					d.getOtherRoom(this).removeDoor(d, false);
				}
				return true;

			}

		}
		return false;

	}

	public boolean leaveable(int dir) {
		// System.out.println("isLeavable : "+dir);

		if (doors[dir - 1] != null) {
			if (doors[dir - 1].isPassable(null))
				// System.out.println("JA");
				if (d.getRoomAt(this, dir - 1).getShrine() != null
						&& d.getRoomAt(this, dir - 1).getShrine() instanceof Statue) {
					return false;
				}
			return true;
		}
		// System.out.println("NEIN");
		return false;
	}

	public int getDoorCount() {
		int cnt = 0;
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				cnt++;
			}

		}

		return cnt;
	}

	public Room getAccessibleNeighbour() {
		for (int i = 0; i < doors.length; i++) {
			Door d = doors[i];
			if (d != null && d.isPassable(null)) {
				return d.getOtherRoom(this);
			}

		}
		return null;
	}

	public List<Room> getNeighboursWithDoor() {
		List<Room> l = new LinkedList<Room>();
		for (int i = 0; i < doors.length; i++) {
			Door d = doors[i];
			if (d != null) {
				l.add(d.getOtherRoom(this));
			}

		}
		return l;
	}

	public List<Room> getScoutableNeighbours() {
		List<Room> l = new LinkedList<Room>();
		for (int i = 0; i < doors.length; i++) {
			Door d = doors[i];
			if (d != null) {
				l.add(doors[i].getOtherRoom(this));
			}

		}
		return l;
	}

	public List<Room> getEightNeighbours() {
		List<Room> l = new LinkedList<Room>();
		int x = number.getX();
		int y = number.getY();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!((i == 0) && (y == 0))) {

					Room toAdd = d.getRoomNr(x + i, y + j);
					if (toAdd != null) {
						l.add(toAdd);
					}
				}
			}
		}
		return l;
	}

	public boolean hasConnectionTo(Room r) {
		Room[] neighbours = new Room[4];
		for (int i = 0; i < neighbours.length; i++) {
			if (doors[i] != null) {
				if (doors[i].getOtherRoom(this) == r) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean hasConnectionTo(JDPoint p) {
		return hasConnectionTo(getDungeon().getRoom(p));
	}

	public boolean hasOpenConnectionTo(Room r) {
		Room[] neighbours = new Room[4];
		for (int i = 0; i < neighbours.length; i++) {
			if (doors[i] != null) {
				if (doors[i].getOtherRoom(this) == r
						&& doors[i].isPassable(null)) {
					return true;
				}
			}
		}

		return false;
	}

	public int getConnectionDirectionTo(Room r) {
		Room[] neighbours = new Room[4];
		for (int i = 0; i < neighbours.length; i++) {
			if (doors[i] != null) {
				if (doors[i].getOtherRoom(this) == r) {
					return i + 1;
				}
			}
		}

		return -1;
	}

	public static int getDoorPosIndex(int dir) {
		if (dir == Dir.NORTH) {
			return 1;
		}
		if (dir == Dir.EAST) {
			return 3;
		}
		if (dir == Dir.SOUTH) {
			return 5;
		}
		if (dir == Dir.WEST) {
			return 7;
		}

		return -1;
	}

	public int getConnectionDirectionTo(JDPoint p) {
		return getConnectionDirectionTo(this.d.getRoom(p));
	}

	public int getConnectionDirectionTo(RoomInfo r) {
		return getConnectionDirectionTo(r.getPoint());
	}

	public boolean hasHallDoor() {
		for (int i = 0; i < doors.length; i++) {
			Door d = doors[i];
			if ((d != null) && (d.isHallDoor())) {
				// //System.out.println(toString() + " hasHallDoor(): true");
				return true;
			}
		}
		// //System.out.println(toString() + " hasHallDoor(): false");

		return false;
	}

	public Door getConnectionTo(Room r) {
		Room[] neighbours = new Room[4];
		for (int i = 0; i < neighbours.length; i++) {
			if (doors[i] != null) {
				if (doors[i].getOtherRoom(this) == r) {
					return doors[i];
				}
			}
		}

		return null;
	}

	public Door getConnectionTo(JDPoint p) {
		return getConnectionTo(d.getRoom(p));
	}

	public boolean hasLockedDoor() {
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null && doors[i].hasLock()) {
				return true;
			}
		}
		return false;
	}

	public Door removeDoor(int dir, boolean otherRoom) {
		// //System.out.println("Will Tuer entfernen: "+toString()+" Richtung:
		// "+dir);
		Door d = null;
		// //System.out.println("removing Door : "+this.getNumber().toString()+"
		// "+otherRoom);
		if ((dir == RouteInstruction.NORTH)) {
			// //System.out.println("removing Door - NORTH");
			d = doors[0];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom && (d != null)) {
					doors[0].getOtherRoom(this).removeDoor(doors[0], false);
				}
				// //System.out.println("Tuer auf null gesetzt!");
				doors[0] = null;
			} else {
				if (d != null) {
					// //System.out.println("Versucht Hallent�r zu entfernen!");
					// //System.out.println(d.isHallDoor());

				}
				return d;
			}

		}
		if ((dir == RouteInstruction.EAST)) {
			// //System.out.println("removing Door - EAST");
			d = doors[1];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom && (d != null)) {
					doors[1].getOtherRoom(this).removeDoor(doors[1], false);
				}
				// //System.out.println("Tuer auf null gesetzt!");
				doors[1] = null;
			} else {
				if (d != null) {
					// //System.out.println("Versucht Hallent�r zu entfernen!");
					// //System.out.println(d.isHallDoor());
				}
				return d;
			}
		}
		if ((dir == RouteInstruction.SOUTH)) {
			// //System.out.println("removing Door - SouTH");
			d = doors[2];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom && (d != null)) {
					doors[2].getOtherRoom(this).removeDoor(doors[2], false);
				}
				// //System.out.println("Tuer auf null gesetzt!");
				doors[2] = null;
			} else {
				if (d != null) {
					// //System.out.println("Versucht Hallent�r zu entfernen!");
					// //System.out.println(d.isHallDoor());
				}
				return d;
			}
		}
		if ((dir == RouteInstruction.WEST)) {
			// //System.out.println("removing Door - WEST");
			d = doors[3];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom && (d != null)) {
					doors[3].getOtherRoom(this).removeDoor(doors[3], false);
				}
				// //System.out.println("Tuer auf null gesetzt!");
				doors[3] = null;
			} else {
				if (d != null) {
					// //System.out.println("Versucht Hallent�r zu entfernen!");
				}
				return d;
			}
		}
		return d;
	}

	public Shrine getShrine() {
		return s;
	}

	public JDPoint getPoint() {
		return number;
	}

	public List<Figure> getRoomFigures() {
		return roomFigures;
	}

	private void setDieMonster(LinkedList<Figure> l) {
		roomFigures = l;
	}

	public List<Item> getItems() {
		return Items;
	}

	private void setItems(List<Item> l) {
		Items = l;
	}

	public void addQuest(Quest q) {
		quests.add(q);
	}

	public boolean partOfQuest(Quest q) {
		if (quests.contains(q)) {
			return true;
		} else {
			return false;
		}
	}

	public void figureEnters(Figure m, int from) {

		if (!this.getDungeon().equals(m.getActualDungeon())) {
			m.setActualDungeon(this.getDungeon());
		}

		int inRoomIndex = -1;
		m.setLocation(number);

		if (from == 0) {
			inRoomIndex = (int) (Math.random() * 8);
			while (positions[inRoomIndex].getFigure() != null) {
				inRoomIndex = (int) (Math.random() * 8);
			}
		}
		if (from == Dir.EAST) {
			inRoomIndex = 7;
		}
		if (from == Dir.WEST) {
			inRoomIndex = 3;
		}
		if (from == Dir.NORTH) {
			inRoomIndex = 5;
		}
		if (from == Dir.SOUTH) {
			inRoomIndex = 1;
		}

		if (m.getPos() != null) {
			m.getPos().figureLeaves();
		}
		if (from != 0) {
			m.setLookDir(from);
		}
		if (m.getRoomVisibility() == null) {
			m.createVisibilityMap(d);
		}
		m.getRoomVisibility().setVisibilityStatus(getNumber(),
				RoomObservationStatus.VISIBILITY_ITEMS);
		m.getRoomVisibility().getStatusObject(getNumber())
				.addVisibilityModifier(m);

		positions[inRoomIndex].figureEntersHere(m);

		roomFigures.add(m);

		if (this.fightRunning()) {
			getFight().figureJoins(m);
		} else {
			this.checkFight(m);
		}
	}
	
	public void figureEntersAtPosition(Figure m, int position) {

		if (!this.getDungeon().equals(m.getActualDungeon())) {
			m.setActualDungeon(this.getDungeon());
		}

		m.setLocation(number);

		if (m.getPos() != null) {
			m.getPos().figureLeaves();
		}
		if (m.getRoomVisibility() == null) {
			m.createVisibilityMap(d);
		}
		m.getRoomVisibility().setVisibilityStatus(getNumber(),
				RoomObservationStatus.VISIBILITY_ITEMS);
		m.getRoomVisibility().getStatusObject(getNumber())
				.addVisibilityModifier(m);

		positions[position].figureEntersHere(m);

		roomFigures.add(m);

		if (this.fightRunning()) {
			getFight().figureJoins(m);
		} else {
			this.checkFight(m);
		}
	}

	// private int getUnusedIndexNumber() {
	// for(int i = 0; i < this.roomFigures.size(); i++) {
	// boolean b = monsterHaveIndex(i);
	// if(!b) {
	// return i;
	// }
	//			
	// }
	// return roomFigures.size();
	// }

	// private boolean monsterHaveIndex(int k) {
	// for(int i = 0; i < this.roomFigures.size(); i++) {
	// Fighter m = (Fighter)roomFigures.get(i);
	// int index = m.getInRoomIndex();
	// if(index == k ) return true;
	//			
	// }
	//		
	// return false;
	// }

	public boolean addItem(Item i) {
		return takeItem(i, null);
	}

	public boolean takeItem(Item i, ItemOwner o) {

		if (i instanceof DustItem) {
			boolean foundother = false;
			for (int k = 0; k < Items.size(); k++) {
				Item a = ((Item) Items.get(k));
				if (a instanceof DustItem) {
					this.removeItem(a);
					DustItem newDust = new DustItem(((DustItem) a).getCount()
							+ ((DustItem) i).getCount());
					Items.add(newDust);
					Item.notifyItem(newDust, this);

					foundother = true;
					break;
				}
			}
			if (!foundother) {
				Items.add(i);
				Item.notifyItem(i, this);
			}
		} else {

			Items.add(i);
			Item.notifyItem(i, this);
		}
		return true;
	}

	public int getVisited() {
		return visited;
	}

	public void setVisited(int i) {
		oldInfos = ("\n\nLetzter Stand\n" + getInfoText(ALL, true));
		// visibility = 1;
		// visited = i;
	}

	// public void setVisibilityStatus(int stat) {
	// //part_scouted = false;
	// visibilityStatus = stat;
	// }

	// public int getVisibilityStatus() {
	// if (game.getVisibility() && isClaimed()) {
	// return 5;
	// }
	// return visibilityStatus;
	// }

	// public void resetVisibility() {
	// visibilityStatus = visibility;
	// if (visibilityStatus < 2) {
	// visibilityStatus = RoomObservationStatus.VISIBILITY_SHRINE;
	// }
	// }

	// public void found() {
	// visibility = RoomObservationStatus.VISIBILITY_FOUND;
	// part_scouted = true;
	// if (visibilityStatus < visibility) {
	// visibilityStatus = visibility;
	// }
	//
	// }

	public boolean figureLeaves(Figure m) {
		prepareFigureLeaves(m); 
		return roomFigures.remove(m);
	}

	private void prepareFigureLeaves(Figure m) {
		m.getRoomVisibility().getStatusObject(getNumber())
				.removeVisibilityModifier(m);
		m.getRoomVisibility().resetVisibilityStatus(this.number);

		m.getPos().figureLeaves();
		if (fight != null) {
			fight.figureLeaves(m);
		}
	}


	private void remItemFromArray(Item i) {
		for (int j = 0; j < itemArray.length; j++) {
			if (itemArray[j] != null && itemArray[j].equals(i)) {
				itemArray[j] = null;
				break;
			}

		}
	}

	public String getInfoText(int c, boolean scouted) {
		String info = new String();
		if (!isWall) {
			info += ("Raum " + number.toString() + ":\n" + "Besucht: ");
		} else {
			info += ("Stelle: " + number.toString() + ":\n" + ": ");

		}
		if (visited == 0) {
			info += ("nein" + "\n");
		} else {
			info += (Integer.toString(visited) + "\n");
		}

		if (c >= LITTLE) {
			int i = roomFigures.size();
			if (i != 0) {
				info += ("Monster: ");
				for (int j = 0; j < i; j++) {
					info += ("  " + roomFigures.get(j).toString() + ", ");
				}
			} else {
				info += ("Keine Monster.\n");
			}

			if (s != null) {
				info += ("\n" + s.toString()) + "\n ";
			}
		}
		if (c == ALL) {
			int k = Items.size();
			// //System.out.println("k = "+Integer.toString(k));
			if (k != 0) {
				info += ("\nGegenstande: " + "\n");
				for (int j = 0; j < k; j++) {
					// //System.out.println("Gegenstand: "+Integer.toString(j));
					info += ("  " + Items.get(j).toString() + "\n");
				}
			} else {
				info += ("Keine Gegenstaende.");
			}

		}
		if (scouted == false)
			info += oldInfos;
		return info;
	}

	public JDPoint getNumber() {
		return number;
	}


	public int getX() {
		return this.number.getX();
	}

	public int getY() {
		return number.getY();
	}

	public boolean searchForSpot(double intensity) {
		if (spot == null) {
			return false;
		} else {
			return spot.found((int) intensity);

		}
	}

	public boolean searchForDoors(double intensity) {
		for (int i = 0; i < 4; i++) {
			if (doors[i] != null && doors[i].isHidden()) {
				return doors[i].found((int) intensity);
			}
		}
		return false;

	}


	public Room getKlon(Room r) {
		Room x = new Room(r.getPoint().getX(), r.getPoint().getY(), r.d);
		x.setDoors(r.getDoors());
		// x.setVisibilityStatus(r.getVisibilityStatus());
		x.setDieMonster(new LinkedList<Figure>(r.getRoomFigures()));
		x.setItems(new LinkedList<Item>(r.getItems()));
		x.setVisited(r.getVisited());
		x.floorIndex = r.getFloorIndex();
		if (r.getShrine() != null) {
			x.setShrine(r.getShrine(), false);
		}
		return x;
	}

	public boolean isWall() {
		return isWall;
	}

	/**
	 * Sets the isWall.
	 * 
	 * @param isWall
	 *            The isWall to set
	 * 
	 */
	public void setIsWall(boolean isWall) {
		this.isWall = isWall;
	}

	public Paragraph[] getParagraphs(RoomObservationStatus status) {
		String room = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FOUND
				|| d.getGame().getVisibility()) {

			room = "Raum: " + getNumber().toString();
			if (d.getGame().getVisibility()) {
				if (getHall() != null) {
					room += "\n Halle: " + getHall().getName();
				}
			}

		}
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(room);
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(Color.orange);
		p[0].setBold();

		String shrine = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_SHRINE) {
			if (getShrine() != null) {
				shrine = getShrine().toString();
			}
		}
		p[1] = new Paragraph(shrine);
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(Color.black);
		p[1].setBold();

		String monster = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FIGURES) {

			monster = "Figuren: " + roomFigures.size();

		}
		p[2] = new Paragraph(monster);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(Color.black);

		String itemS = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_ITEMS) {

			itemS = "Gegenst�nde: " + Items.size();

		}
		p[3] = new Paragraph(itemS);
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(Color.black);

		return p;
	}

	// deprecated --> vis-zeug
	// public Paragraph[] getParagraphs() {
	// String room = new String();
	// if (visibilityStatus >= RoomObservationStatus.VISIBILITY_FOUND ||
	// d.getGame().getVisibility()) {
	//
	// room = "Raum: " + getNumber().toString();
	// if (d.getGame().getVisibility()) {
	// if (getHall() != null) {
	// room += "\n Halle: " + getHall().getName();
	// }
	// }
	//
	// }
	// Paragraph[] p = new Paragraph[4];
	// p[0] = new Paragraph(room);
	// p[0].setSize(24);
	// p[0].setCentered();
	// p[0].setColor(Color.orange);
	// p[0].setBold();
	//
	// String shrine = new String();
	// if (visibilityStatus >= RoomObservationStatus.VISIBILITY_SHRINE) {
	// if (getShrine() != null) {
	// shrine = getShrine().toString();
	// }
	// }
	// p[1] = new Paragraph(shrine);
	// p[1].setSize(20);
	// p[1].setCentered();
	// p[1].setColor(Color.black);
	// p[1].setBold();
	//
	// String monster = new String();
	// if (visibilityStatus >= RoomObservationStatus.VISIBILITY_MONSTERS) {
	//
	// monster = "Figuren: " + roomFigures.size();
	//
	// }
	// p[2] = new Paragraph(monster);
	// p[2].setSize(14);
	// p[2].setCentered();
	// p[2].setColor(Color.black);
	//
	// String itemS = new String();
	// if (visibilityStatus >= RoomObservationStatus.VISIBILITY_ITEMS) {
	//
	// itemS = "Gegenst�nde: " + Items.size();
	//
	// }
	// p[3] = new Paragraph(itemS);
	// p[3].setSize(14);
	// p[3].setCentered();
	// p[3].setColor(Color.black);
	//
	// return p;
	// }

	/**
	 * Sets the doors.
	 * 
	 * @param doors
	 *            The doors to set
	 * 
	 * @uml.property name="doors"
	 */
	public void setDoors(Door[] doors) {
		this.doors = doors;
	}

	/**
	 * Returns the chest.
	 * 
	 * @return chest
	 * 
	 * @uml.property name="chest"
	 */
	public Chest getChest() {
		return Chest;
	}

	/**
	 * Sets the chest.
	 * 
	 * @param chest
	 *            The chest to set
	 * 
	 * @uml.property name="chest"
	 */
	public void setChest(Chest chest) {
		// //System.out.println("SETTING CHEST!" + toString());
		Chest = chest;
		Chest.setLocation(this.number);
		chest.setRoom(this);
	}

	/**
	 * Returns the hall.
	 * 
	 * @return hall
	 * 
	 * @uml.property name="hall"
	 */
	public Hall getHall() {
		return hall;
	}

	/**
	 * Returns the sec.
	 * 
	 * @return sector
	 * 
	 * @uml.property name="sec"
	 */
	public Sector getSec() {
		return sec;
	}

	/**
	 * Sets the hall.
	 * 
	 * @param hall
	 *            The hall to set
	 */
	public boolean setHall(Hall hall) {
		if (this.hall == null) {
			// //System.out.println("claiming: "+toString() +" by
			// "+hall.getName());
			this.hall = hall;
			return true;
		} else {
			// System.out.println(toString()+" schon besetzt:
			// "+this.hall.getName());
			return false;
			// System.exit(0);
			// for(int i = 0; i < 100; i++) {
			// //System.out.println("+");
			// }
		}
	}

	public String toString() {
		String s = " Raum Nr.: " + number.toString();
		if (getChest() != null) {
			s += " truhe: ja  ";
		}
		if (getShrine() != null) {
			s += " Schrein:  " + getShrine().toString();
		}
		return s;

	}

	/**
	 * Sets the sec.
	 * 
	 * @param sec
	 *            The sec to set
	 * 
	 * @uml.property name="sec"
	 */
	public void setSec(Sector sec) {
		this.sec = sec;
	}

	public boolean isClaimed() {
		return (hall != null) || (sec != null);
	}

	/**
	 * Returns the d.
	 * 
	 * @return dungeon
	 * 
	 * @uml.property name="d"
	 */
	public Dungeon getDungeon() {
		return d;
	}

	/**
	 * Returns the spot.
	 * 
	 * @return hidden_spot
	 * 
	 * @uml.property name="spot"
	 */
	public HiddenSpot getSpot() {
		return spot;
	}

	/**
	 * Sets the spot.
	 * 
	 * @param spot
	 *            The spot to set
	 * 
	 * @uml.property name="spot"
	 */
	public void setSpot(HiddenSpot spot) {
		this.spot = spot;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="part_scouted"
	 */
	public boolean isPart_scouted() {
		return part_scouted;
	}

	/**
	 * @param b
	 * 
	 * @uml.property name="part_scouted"
	 */
	public void setPart_scouted(boolean b) {
		part_scouted = b;
	}

	/**
	 * @return Returns the positions.
	 */
	public Position[] getPositions() {
		return positions;
	}

	/**
	 * @return Returns the kampf.
	 */
	public Fight getFight() {
		return fight;
	}

	public void endFight() {
		fight = null;
		for (Iterator<Figure> iter = roomFigures.iterator(); iter.hasNext();) {
			Figure element = (Figure) iter.next();
			boolean disappears = element.fightEnded();
			if(disappears) {
				// prevent concurrent modification
				prepareFigureLeaves(element);
				iter.remove();
			}
		}
	}

	class MyFightOrderComparator implements Comparator<Figure> {

		public int compare(Figure o1, Figure o2) {
			if (o1 instanceof Figure && o2 instanceof Figure) {
				Figure f1 = (Figure) o1;
				Figure f2 = (Figure) o2;
				double readines1 = f1.getReadines();
				double readines2 = f2.getReadines();
				if (f1.isRaiding()) {
					return -1;
				}
				if (f2.isRaiding()) {
					return 1;
				}

				if (readines1 > readines2) {
					return -1;
				} else if (readines1 < readines2)
					return 1;
			} else {
				return 0;
			}

			return 0;
		}
	}

}
