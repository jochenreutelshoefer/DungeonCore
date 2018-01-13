package dungeon;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dungeon.generate.DefaultHall;
import dungeon.generate.Sector;
import dungeon.quest.Quest;
import dungeon.quest.RoomQuest;
import dungeon.util.RouteInstruction;
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
import game.JDEnv;
import gui.Paragraph;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.interfaces.ItemOwner;
import shrine.Shrine;
import shrine.Statue;
import util.JDColor;

/**
 * Ein Raum des Dungeons. Kann dynamisch enthalten: Gegenstaende, Monster, Held ;
 * Kann statisch enthalten: Truhe, Schrein, Versteck, 4 Tueren;
 * <p>
 * Ein Raum weis zu welchen Quest, Halle, Sektor, Dungeon er gehoert Der
 * aktuelle Sichtbarkeitsstatus des Helden auf diesen Raum wird hier
 * gespeichert.
 */
public class Room extends DungeonWorldObject implements
		ItemOwner {

	public static final int NO = 0;

	public static final int LITTLE = 1;

	public static final int ALL = 2;

	boolean isWall = false;

	// TODO: is this the right place to store every figure that can observe this room?
	private final Map<Figure, Integer> observer = new HashMap<>();

	private final Position[] positions = new Position[8];

	private HiddenSpot spot;

	private Fight fight = null;

	private Shrine s;

	private Chest chest;

	private final Dungeon d;

	private final List<Quest> quests = new LinkedList<Quest>();

	private List<Figure> roomFigures;

	private List<Item> items;

	private final JDPoint number;

	public void checkFight(Figure movedIn) {

		boolean fight = false;
		for (Iterator<Figure> iter = roomFigures.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			if(element.equals(movedIn)) {
				// should not start fight with himself
				continue;
			}
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

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new RoomInfo(this, map);
	}

	public void setObserverStatus(Figure f, int vis) {
		if (vis < RoomObservationStatus.VISIBILITY_FIGURES) {
			observer.remove(f);
		}
		else {
			observer.put(f, vis);
		}
	}

	public double calcFleeDiff() {
		double diff = 0;
		List<Figure> l = getFight().getFightFigures();
		for (int i = 0; i < l.size(); i++) {
			diff += l.get(i).getAntiFleeValue();
		}
		if (l.size() == 2) {
			diff = diff * 4 / 5;
		}
		else if (l.size() == 3) {
			diff = diff * 3 / 4;
		}
		else if (l.size() == 4) {
			diff = diff * 1 / 2;
		}
		return diff;
	}

	@Override
	public MemoryObject getMemoryObject(FigureInfo info) {
		return new RoomMemory(this, info);
	}

	public boolean monstersThere() {
		for (Iterator<Figure> iter = this.roomFigures.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			if (element instanceof Monster) {
				return true;
			}
		}
		return false;
	}

	private int visited;

	private boolean start = false;

	private boolean part_scouted = false;

	private final Item[] itemArray = new Item[4];

	public void setFloorIndex(int floorIndex) {
		this.floorIndex = floorIndex;
	}

	private int floorIndex;

	public String oldInfos;

	private RoomQuest rquest;

	private DefaultHall hall = null;

	private Sector sec = null;

	private Door[] doors = new Door[4];

	public Room(int x, int y, Dungeon d) {
		this.d = d;
		roomFigures = new LinkedList<Figure>();
		items = new LinkedList<Item>();
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
		for (Iterator<Figure> i = roomFigures.iterator(); i.hasNext(); ) {
			Figure element = i.next();
			element.timeTick(round);
		}
	}

	public void turn(int round) {
		tickFigures(round);

		if (fight == null) {
			turnNormal(round);
		}
		else {
			fight.doFight();
		}
	}

	private void turnNormal(int round) {
		List<Figure> figures = new LinkedList<Figure>();
		figures.addAll(roomFigures);
		for (Iterator<Figure> iter = figures.iterator(); iter.hasNext(); ) {
			if (this.d.isGameOver()) {
				break;
			}
			Figure element = iter.next();
			element.turn(round);
		}
	}

	public void setShrine(Shrine s) {
		this.getDungeon().addShrine(s);
		setShrine(s, true);

	}

	public void setShrine(Shrine s, boolean setShrineLocation) {

		this.s = s;
		if (s != null && setShrineLocation) {
			s.setLocation(this);
		}
	}

	public int getFloorIndex() {
		if (hall == null) {
			return floorIndex;
		}
		return hall.getFloorIndex();
	}

	public int[] makeDoorInfo() {
		int[] infos = new int[4];
		for (int i = 0; i < 4; i++) {
			infos[i] = Door.NO_DOOR;
			if (doors[i] != null) {
				if (doors[i].hasLock()) {
					if (doors[i].getLocked()) {
						infos[i] = Door.DOOR_LOCK_LOCKED;
					}
					else {
						infos[i] = Door.DOOR_LOCK_OPEN;
					}
				}
				else {
					infos[i] = Door.DOOR;
				}
			}
		}
		return infos;
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {
		ItemInfo[] array = new ItemInfo[items.size()];
		for (int i = 0; i < items.size(); i++) {
			array[i] = ItemInfo.makeItemInfo(items.get(i), map);
		}
		return array;
	}

	@Override
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
		return (s instanceof Statue);
	}

	public boolean directionPassable(int dir) {
		return doors[dir - 1] != null && doors[dir - 1].isPassable(null);
	}

	@Override
	public boolean removeItem(Item i) {
		boolean b = false;
		if (items.remove(i)) {
			this.remItemFromArray(i);
			b = true;
		}
		return b;
	}

	public boolean hasItem(Item i) {
		return items.contains(i);
	}

	public Room getNeighbourRoom(RouteInstruction.Direction direction) {
		return getNeighbourRoom(direction.getValue());
	}

	public Room getNeighbourRoom(int dir) {
		if (dir == 1) {
			return d.getRoom(d.getPoint(number.getX(), number.getY() - 1));
		}
		else if (dir == 2) {
			return d.getRoom(d.getPoint(number.getX() + 1, number.getY()));
		}
		else if (dir == 3) {
			return d.getRoom(d.getPoint(number.getX(), number.getY() + 1));
		}
		else if (dir == 4) {
			return d.getRoom(d.getPoint(number.getX() - 1, number.getY()));
		}
		else {
			return null;
		}

	}

	@Override
	public Room getRoom() {
		return this;
	}

	@Override
	public JDPoint getLocation() {
		return number;
	}

	public void setRoomQuest(RoomQuest q) {
		rquest = q;
	}

	public RoomQuest getRoomQuest() {
		return rquest;
	}

	public void setStart() {
		start = true;
	}

	public boolean isStart() {
		return start;
	}

	@Override
	public boolean addItems(List<Item> l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (l.get(i));
			this.takeItem(it);
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
			}
			else {
				s += JDEnv.getResourceBundle().getString("northern");
			}
		}
		else if (Math.abs(dy) <= 1) {
			if (dx < 0) {
				s += JDEnv.getResourceBundle().getString("eastern");
			}
			else {
				s += JDEnv.getResourceBundle().getString("western");
			}
		}
		else {
			if (dy > 0) {
				if (dx > 0) {
					s += JDEnv.getResourceBundle().getString("northwestern");
				}
				else {
					s += JDEnv.getResourceBundle().getString("northeastern");
				}
			}
			else {
				if (dx > 0) {
					s += JDEnv.getResourceBundle().getString("southwestern");
				}
				else {
					s += JDEnv.getResourceBundle().getString("southeastern");
				}
			}
		}
		return s + " ";
	}

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	@Deprecated
	public Item[] getItemArray() {
		LinkedList<Item> workList = new LinkedList<Item>(items);
		for (int i = 0; i < 4; i++) {
			boolean b;
			if (itemArray[i] != null) {
				b = items.contains(itemArray[i]);

				if (b) {
					workList.remove(itemArray[i]);
				}
				else {
					itemArray[i] = null;
				}
			}
			else {
				boolean done = false;
				if (workList.isEmpty()) {
					done = true;
					break;
				}
				Item it = workList.getFirst();
				while (itemArrayContains(it)) {
					workList.removeFirst();
					if (workList.isEmpty()) {
						done = true;
						break;
					}
					it = workList.getFirst();
				}
				if (done) {
					itemArray[i] = null;
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
			mons[i] = MonsterInfo.makeFigureInfo((roomFigures.get(i)),
					map);
		}
		return mons;
	}

	public void distributePercept(Percept p) {

		if (p instanceof OpticalPercept || p instanceof TextPercept || p instanceof InfoPercept) {
			for (Iterator<Figure> iter = observer.keySet().iterator(); iter.hasNext(); ) {
				Figure element = iter.next();
				Integer visStat = observer.get(element);
				if (visStat >= RoomObservationStatus.VISIBILITY_FIGURES) {
					element.tellPercept(p);
				}

			}
		}
	}

	public boolean hasHero() {
		for (Iterator<Figure> iter = this.roomFigures.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			if (element instanceof Hero) {
				return true;
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

	public void removeAllDoors() {
		for (int i = 0; i < doors.length; i++) {
			int doorDirection = i + 1; // direction index starts with 1
			setDoor(null, RouteInstruction.Direction.fromInteger(doorDirection), true);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Room room = (Room) o;

		return d.equals(room.d) && number.equals(room.number);

	}

	@Override
	public int hashCode() {
		int result = d.hashCode();
		result = 31 * result + number.hashCode();
		return result;
	}

	public void setDoor(Door d, RouteInstruction.Direction direction, boolean otherRoom) {
		if (direction == RouteInstruction.Direction.North) {
			doors[0] = d;
		}
		if (direction == RouteInstruction.Direction.East) {
			doors[1] = d;
		}
		if (direction == RouteInstruction.Direction.South) {
			doors[2] = d;
		}
		if (direction == RouteInstruction.Direction.West) {
			doors[3] = d;
		}
		if (otherRoom) {
			Room other = this.getNeighbourRoom(direction);
			if (other != null) {
				other.setDoor(d, RouteInstruction.Direction.opposite(direction), false);
			}
		}
	}

	@Deprecated
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
		Door d = new Door(this, dun.getRoomAt(this,
				RouteInstruction.direction(dir)));
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

	public Door getDoor(RouteInstruction.Direction direction) {
		Door d = null;
		if (direction == RouteInstruction.Direction.North) {
			d = doors[0];
		}
		if (direction == RouteInstruction.Direction.East) {
			d = doors[1];
		}
		if (direction == RouteInstruction.Direction.South) {
			d = doors[2];
		}
		if (direction == RouteInstruction.Direction.West) {
			d = doors[3];
		}
		return d;
	}

	@Deprecated
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

	public RouteInstruction.Direction getDirection(Door d) {
		RouteInstruction.Direction dir = null;
		if (d != null) {
			if (d == doors[0]) {
				dir = RouteInstruction.Direction.North;
			}
			if (d == doors[1]) {

				dir = RouteInstruction.Direction.East;
			}

			if (d == doors[2]) {

				dir = RouteInstruction.Direction.South;
			}
			if (d == doors[3]) {

				dir = RouteInstruction.Direction.West;
			}
		}
		return dir;
	}

	public Door[] getDoors() {
		return doors;
	}

	public boolean removeDoor(Door d, boolean otherRoom) {
		if (d == null) {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			if (doors[i] == d) {
				doors[i] = null;
				if (otherRoom) {
					d.getOtherRoom(this).removeDoor(d, false);
				}
				return true;
			}
		}
		return false;
	}

	public void addDoor(Door d, int dir, boolean otherRoom) {
		RouteInstruction.Direction direction = RouteInstruction.Direction.fromInteger(dir);
		doors[dir-1] = d;
		Room neighbourRoom = this.getNeighbourRoom(direction);
		if(otherRoom) {
			neighbourRoom.addDoor(d, RouteInstruction.turnOpp(direction).getValue(), false);
		}
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

	public boolean hasConnectionTo(Room r) {
		for (int i = 0; i < 4; i++) {
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
		for (int i = 0; i < 4; i++) {
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
		return Room.getDoorPosIndex(dir);
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
				return true;
			}
		}
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
		Door d = null;
		if ((dir == RouteInstruction.NORTH)) {
			d = doors[0];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom) {
					doors[0].getOtherRoom(this).removeDoor(doors[0], false);
				}
				doors[0] = null;
			}
			else {
				return d;
			}
		}
		if ((dir == RouteInstruction.EAST)) {
			d = doors[1];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom) {
					doors[1].getOtherRoom(this).removeDoor(doors[1], false);
				}
				doors[1] = null;
			}
			else {
				return d;
			}
		}
		if ((dir == RouteInstruction.SOUTH)) {
			d = doors[2];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom) {
					doors[2].getOtherRoom(this).removeDoor(doors[2], false);
				}
				doors[2] = null;
			}
			else {
				return d;
			}
		}
		if ((dir == RouteInstruction.WEST)) {
			d = doors[3];
			if ((d != null) && (!d.isHallDoor())) {
				if (otherRoom) {
					doors[3].getOtherRoom(this).removeDoor(doors[3], false);
				}
				doors[3] = null;
			}
			else {
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

	private void setItems(List<Item> l) {
		items = l;
	}

	public void addQuest(Quest q) {
		quests.add(q);
	}

	public boolean partOfQuest(Quest q) {
		if (quests.contains(q)) {
			return true;
		}
		else {
			return false;
		}
	}

	public void figureEnters(Figure figure, int fromDir) {



		int inRoomIndex = -1;

		if (fromDir == 0) {
			inRoomIndex = (int) (Math.random() * 8);
			while (positions[inRoomIndex].getFigure() != null) {
				inRoomIndex = (int) (Math.random() * 8);
			}
		}
		// TODO: check whether the pos is free!
		// edit: is automatically shifted to next position if not free
		if (fromDir == Dir.EAST) {
			inRoomIndex = 7;
		}
		if (fromDir == Dir.WEST) {
			inRoomIndex = 3;
		}
		if (fromDir == Dir.NORTH) {
			inRoomIndex = 5;
		}
		if (fromDir == Dir.SOUTH) {
			inRoomIndex = 1;
		}

		figureEntersAtPosition(figure, fromDir, inRoomIndex);
	}

	public void figureEntersAtPosition(Figure figure, int fromDir, int inRoomIndex) {
		Position position = positions[inRoomIndex];
		if (!this.getDungeon().equals(figure.getActualDungeon())) {
			figure.setActualDungeon(this.getDungeon());
		}

		if (figure.getPos() != null) {
			figure.getPos().figureLeaves();
		}
		if (fromDir != 0) {
			figure.setLookDir(fromDir);
		}
		if (figure.getRoomVisibility() == null) {
			figure.createVisibilityMap(d);
		}
		figure.getRoomVisibility().setVisibilityStatus(getNumber(),
				RoomObservationStatus.VISIBILITY_ITEMS);
		figure.getRoomVisibility().getStatusObject(getNumber())
				.addVisibilityModifier(figure);

		position.figureEntersHere(figure);
		figure.setLocation(this);
		roomFigures.add(figure);

		if (this.fightRunning()) {
			getFight().figureJoins(figure);
		}
		else {
			this.checkFight(figure);
		}
	}

	public void figureEntersAtPosition(Figure m, int position) {
		figureEntersAtPosition(m, 0, position);
	}

	public boolean addItem(Item i) {
		return takeItem(i);
	}

	@Override
	public boolean takeItem(Item i) {

		if (i instanceof DustItem) {
			boolean foundOther = false;
			for (int k = 0; k < items.size(); k++) {
				Item a = (items.get(k));
				if (a instanceof DustItem) {
					this.removeItem(a);
					DustItem newDust = new DustItem(((DustItem) a).getCount()
							+ ((DustItem) i).getCount());
					items.add(newDust);
					Item.notifyItem(newDust, this);

					foundOther = true;
					break;
				}
			}
			if (!foundOther) {
				items.add(i);
				Item.notifyItem(i, this);
			}
		}
		else {

			items.add(i);
			Item.notifyItem(i, this);
		}
		return true;
	}

	public int getVisited() {
		return visited;
	}

	public void setVisited(int i) {
		oldInfos = ("\n\nLetzter Stand\n" + getInfoText(ALL, true));
	}

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
			info += ("Raum " + number + ":\n" + "Besucht: ");
		}
		else {
			info += ("Stelle: " + number + ":\n" + ": ");

		}
		if (visited == 0) {
			info += ("nein" + "\n");
		}
		else {
			info += (Integer.toString(visited) + "\n");
		}

		if (c >= LITTLE) {
			int i = roomFigures.size();
			if (i != 0) {
				info += ("Monster: ");
				for (int j = 0; j < i; j++) {
					info += ("  " + roomFigures.get(j) + ", ");
				}
			}
			else {
				info += ("Keine Monster.\n");
			}

			if (s != null) {
				info += ("\n" + s) + "\n ";
			}
		}
		if (c == ALL) {
			int k = items.size();
			if (k != 0) {
				info += ("\nGegenstande: " + "\n");
				for (int j = 0; j < k; j++) {
					info += ("  " + items.get(j) + "\n");
				}
			}
			else {
				info += ("Keine Gegenstaende.");
			}

		}
		if (!scouted) {
			info += oldInfos;
		}
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
		return spot != null && spot.found((int) intensity);
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
		x.setDieMonster(new LinkedList<>(r.getRoomFigures()));
		x.setItems(new LinkedList<>(r.getItems()));
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

	public void setIsWall(boolean isWall) {
		this.isWall = isWall;
	}

	public Paragraph[] getParagraphs(RoomObservationStatus status) {
		String room = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FOUND) {
			room = "Raum: " + getNumber();
		}
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(room);
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
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
		p[1].setColor(JDColor.black);
		p[1].setBold();

		String monster = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FIGURES) {
			monster = "Figuren: " + roomFigures.size();
		}
		p[2] = new Paragraph(monster);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);

		String itemS = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_ITEMS) {
			itemS = "Gegenstände: " + items.size();
		}
		p[3] = new Paragraph(itemS);
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(JDColor.black);
		return p;
	}

	public void setDoors(Door[] doors) {
		this.doors = doors;
	}

	public Chest getChest() {
		return chest;
	}

	public void setChest(Chest c) {
		chest = c;
		if (chest != null) {
			chest.setLocation(this.number);
			chest.setRoom(this);
		}
	}

	public DefaultHall getHall() {
		return hall;
	}

	public Sector getSec() {
		return sec;
	}

	public boolean setHall(DefaultHall hall) {
		if (this.hall == null) {
			this.hall = hall;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		String s = " Raum Nr.: " + number;
		if (getChest() != null) {
			s += " truhe: ja  ";
		}
		if (getShrine() != null) {
			s += " Schrein:  " + getShrine();
		}
		return s;

	}

	public void setSec(Sector sec) {
		this.sec = sec;
	}

	public boolean isClaimed() {
		return (hall != null) || (sec != null);
	}

	public Dungeon getDungeon() {
		return d;
	}

	public HiddenSpot getSpot() {
		return spot;
	}

	public void setSpot(HiddenSpot spot) {
		this.spot = spot;
	}

	public boolean isPart_scouted() {
		return part_scouted;
	}

	public void setPart_scouted(boolean b) {
		part_scouted = b;
	}

	public Position[] getPositions() {
		return positions;
	}

	public Fight getFight() {
		return fight;
	}

	public void endFight() {
		fight = null;
		for (Iterator<Figure> iter = roomFigures.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			boolean disappears = element.fightEnded(roomFigures);
			if (disappears) {
				// prevent concurrent modification
				prepareFigureLeaves(element);
				iter.remove();
			}
		}
	}

	static class MyFightOrderComparator implements Comparator<Figure> {

		@Override
		public int compare(Figure o1, Figure o2) {
			if (o1 != null && o2 != null) {
				double readiness1 = o1.getReadiness();
				double readiness2 = o2.getReadiness();
				if (o1.isRaiding()) {
					return -1;
				}
				if (o2.isRaiding()) {
					return 1;
				}
				if (readiness1 > readiness2) {
					return -1;
				}
				else if (readiness1 < readiness2) {
					return 1;
				}
			}
			else {
				return 0;
			}
			return 0;
		}
	}

}
