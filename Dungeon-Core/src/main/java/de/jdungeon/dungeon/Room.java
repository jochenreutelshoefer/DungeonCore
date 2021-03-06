package de.jdungeon.dungeon;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import de.jdungeon.dungeon.quest.RoomQuest;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.event.EventManager;
import de.jdungeon.event.FightEndedEvent;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.memory.MemoryObject;
import de.jdungeon.figure.memory.RoomMemory;
import de.jdungeon.figure.percept.InfoPercept;
import de.jdungeon.figure.percept.OpticalPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.interfaces.ItemOwner;
import de.jdungeon.location.Location;
import de.jdungeon.location.Statue;
import de.jdungeon.log.Log;
import de.jdungeon.util.CopyOnWriteArrayList;
import de.jdungeon.util.JDColor;

public class Room extends DungeonWorldObject implements ItemOwner, RoomEntity {

	private boolean isWall = false;

	// TODO: is this the right place to store every de.jdungeon.figure that can observe this room?
	private final Map<Figure, Integer> observer = new ConcurrentHashMap<>();

	private final Position[] positions = new Position[8];

	private Location s;

	private Chest chest;

	private final Dungeon dungeon;

	//private final List<Figure> roomFigures;

	private final Map<Position, Set<Figure>> deadFigures = new HashMap<>();

	private final List<Item> items;

	private final JDPoint number;

	private int floorIndex;

	private RoomQuest rquest;

	private Door[] doors = new Door[4];

	private final Item[] itemArray = new Item[4];

	private boolean fightRunning = false;

	public void checkFight(Figure movedIn, int round) {
		boolean fight = false;
		ControlUnit movedInControl = movedIn.getControl();
		if (movedInControl == null) {
			Log.warning("de.jdungeon.figure without control moves into room: " + movedIn.getName());
		}
		Iterator<Figure> iterator = getRoomFigures().iterator();
		for (Iterator<Figure> iter = iterator; iter.hasNext(); ) {
			Figure element = iter.next();
			if (element.equals(movedIn)) {
				// should not start de.jdungeon.fight with himself
				continue;
			}
			ControlUnit currentControl = element.getControl();
			if (currentControl != null) {
				boolean currentIsHostileToMovedIn = currentControl.isHostileTo(FigureInfo.makeFigureInfo(movedIn, element
						.getViwMap()));
				boolean movedInIsHostileToCurrent = movedInControl.isHostileTo(FigureInfo.makeFigureInfo(element, movedIn
						.getViwMap()));
				if ((currentIsHostileToMovedIn || movedInIsHostileToCurrent)) {
					// if one of them wants to start a de.jdungeon.fight, we start a de.jdungeon.fight
					fight = true;
				}
			}
		}
		if (fight) {
			startFight(round);
		}
	}

	private void startFight(int round) {
		Log.info("START FIGHT");
		this.fightRunning = true;
	}

	public void endFight(int round) {
		Log.info("END FIGHT");
		fightRunning = false;

		// remove magic conjured creature that disappear at the end of a de.jdungeon.fight
		Iterator<Figure> iterator = getRoomFigures().iterator();
		for (Iterator<Figure> iter = iterator; iter.hasNext(); ) {
			Figure element = iter.next();
			boolean disappears = element.fightEnded(getRoomFigures(), round);
			if (disappears) {
				element.getViwMap().getStatusObject(getNumber()).removeVisibilityModifier(element);
				element.getViwMap().resetVisibilityStatus(this.number);
				element.getPos().figureLeaves();
			}
		}
		EventManager.getInstance().fireEvent(new FightEndedEvent(this));
	}

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new RoomInfo(this, map);
	}

	public void setObserverStatus(Figure f, int vis) {
		synchronized (observer) {
			if (vis < RoomObservationStatus.VISIBILITY_FIGURES) {
				observer.remove(f);
			}
			else {
				observer.put(f, vis);
			}
		}
	}

	public double calcFleeDiff() {
		double diff = 0;
		List<Figure> l = getRoomFigures();
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

	public void setFloorIndex(int floorIndex) {
		this.floorIndex = floorIndex;
	}

	public Collection<ItemOwner> getAllItemOwners() {
		Collection<ItemOwner> result = new HashSet<>();
		result.addAll(getRoomFigures());
		result.add(this);
		if (this.chest != null) {
			result.add(chest);
		}
		return result;
	}

	public Room(int x, int y) {
		this(x, y, null);
	}

	public Room(int x, int y, Dungeon dungeon) {
		this.dungeon = dungeon;
		items = new CopyOnWriteArrayList<>();
		number = new JDPoint(x, y);
		for (int i = 0; i < positions.length; i++) {
			positions[i] = new Position(this, i);
		}
		for (int i = 0; i < positions.length; i++) {
			int last = (i - 1) % 8;
			if (i == 0) {
				last = 7;
			}
			positions[last].setPrevious(positions[i]);
			positions[(i + 1) % 8].setNext(positions[i]);
		}
	}

	public boolean fightRunning() {
		return fightRunning;
	}

	public boolean turn(int round, DungeonWorldUpdater worldUpdater) {
		for (Figure roomFigure : getRoomFiguresArray()) {
			if (roomFigure != null && roomFigure.getLastRoundTurnCompleted() < round) {
				if (!dungeon.isGameOver()) {
					// figure does its turn
					roomFigure.turn(round, worldUpdater);

					if (worldUpdater.getGameLoopMode() == GameLoopMode.RenderThreadWorldUpdate) {
						// if a figure is current idle, we break and try again on next render loop call
						boolean figureCompletedRound = roomFigure.getLastRoundTurnCompleted() == round;
						if (!figureCompletedRound) {
							return false;
						}
					}
				}
			}
		}
		// all figures in this room have their round completed
		return true;
	}

	public boolean checkFightOn() {

		List<Figure> roomFigures = getRoomFigures();
		if (roomFigures.size() <= 1) {
			return false;
		}

		boolean fightOn = false;
		for (Figure element : roomFigures) {
			ControlUnit c = element.getControl();
			if (c == null) {
				return false;
			}
			for (Figure element2 : roomFigures) {
				if (element != element2) {
					boolean hostileTo = element.getControl()
							.isHostileTo(FigureInfo.makeFigureInfo(element2, element.getViwMap()));
					if (hostileTo) {
						fightOn = true;
						break;
					}
				}
				if (fightOn) {
					break;
				}
			}
		}
		return fightOn;
	}

	public void resetShrine(Location shrine) {
		if (this.s != shrine) {
			Log.warning("Trying to reset a location that was not set in before! Weird!");
			return;
		}
		this.getDungeon().removeShrine(s);
		setLocation(null, false);
	}

	public void setLocation(Location newShrine) {
		if (this.s != null) {
			throw new IllegalStateException("check for shrine before setting one!");
		}
		if (newShrine.getRoom() != null && !newShrine.getRoom().equals(this)) {
			throw new IllegalStateException("de.jdungeon.location room not matching");
		}
		this.getDungeon().addShrine(newShrine);
		setLocation(newShrine, true);
	}

	public void setLocation(Location s, boolean setShrineLocation) {
		this.s = s;
		if (s != null && setShrineLocation) {
			s.setLocation(this);
		}
	}

	public int getFloorIndex() {
		return floorIndex;
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
	public Item unwrapItem(ItemInfo wrapped) {
		List<Item> items = getItems();
		for (Item item : items) {
			if (ItemInfo.makeItemInfo(item, null).equals(wrapped)) {
				return item;
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

	@Override
	public boolean hasItem(Item i) {
		return items.contains(i);
	}

	public Room getNeighbourRoom(RouteInstruction.Direction direction) {
		return getNeighbourRoom(direction.getValue());
	}

	public Room getNeighbourRoom(int dir) {
		if (dir == 1) {
			return dungeon.getRoom(dungeon.getPoint(number.getX(), number.getY() - 1));
		}
		else if (dir == 2) {
			return dungeon.getRoom(dungeon.getPoint(number.getX() + 1, number.getY()));
		}
		else if (dir == 3) {
			return dungeon.getRoom(dungeon.getPoint(number.getX(), number.getY() + 1));
		}
		else if (dir == 4) {
			return dungeon.getRoom(dungeon.getPoint(number.getX() - 1, number.getY()));
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
	public JDPoint getRoomNumber() {
		return number;
	}

	@Override
	public Collection<Position> getInteractionPositions() {
		return Arrays.asList(getPositions());
	}

	public void setRoomQuest(RoomQuest q) {
		rquest = q;
	}

	public RoomQuest getRoomQuest() {
		return rquest;
	}

	@Override
	public boolean addItems(List<Item> l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (l.get(i));
			this.takeItem(it);
		}
		return true;
	}

    /*
    public String getDirectionString(Room other) {
        JDPoint b = other.getRoomNumber();
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

     */

	@Override
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

	public void distributePercept(Percept p) {
		synchronized (observer) {
			if (p instanceof OpticalPercept || p instanceof TextPercept || p instanceof InfoPercept) {
				for (Figure element : observer.keySet()) {
					Integer visStat = observer.get(element);
					if (visStat >= RoomObservationStatus.VISIBILITY_FIGURES) {
						element.tellPercept(p);
					}
				}
			}
		}
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

	public void removeDoors(RouteInstruction.Direction... directions) {
		for (int i = 0; i < doors.length; i++) {
			int doorDirection = i + 1; // direction index starts with 1
			RouteInstruction.Direction dir = RouteInstruction.Direction.fromInteger(doorDirection);
			if (contains(dir, directions)) {
				setDoor(null, dir, true);
			}
		}
	}

	public void removeAllDoorsExcept(RouteInstruction.Direction... directions) {
		for (int i = 0; i < doors.length; i++) {
			int doorDirection = i + 1; // direction index starts with 1
			RouteInstruction.Direction dir = RouteInstruction.Direction.fromInteger(doorDirection);
			if (!contains(dir, directions)) {
				setDoor(null, dir, true);
			}
		}
	}

	private boolean contains(RouteInstruction.Direction dir, RouteInstruction.Direction... directions) {
		for (RouteInstruction.Direction direction : directions) {
			if (direction == dir) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Room room = (Room) o;
		if (this.dungeon == null && room.dungeon == null) return number.equals(room.number);
		return dungeon.equals(room.dungeon) && number.equals(room.number);
	}

	@Override
	public int hashCode() {
		int result = dungeon.hashCode();
		result = 31 * result + number.hashCode();
		return result;
	}

	public void setDoorTo(Room room2) {
		setDoor(new Door(this, room2), this.getDirection(room2), true);
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

	public RouteInstruction.Direction getDirection(Room neighbour) {
		return RouteInstruction.Direction.fromPoints(this.number, neighbour.number);
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
		doors[dir - 1] = d;
		Room neighbourRoom = this.getNeighbourRoom(direction);
		if (otherRoom) {
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
		List<Room> l = new LinkedList<>();
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
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				if (doors[i].getOtherRoom(this) == r) {
					return i + 1;
				}
			}
		}
		return -1;
	}

	public int getConnectionDirectionTo(JDPoint p) {
		return getConnectionDirectionTo(this.dungeon.getRoom(p));
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
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				if (doors[i].getOtherRoom(this) == r) {
					return doors[i];
				}
			}
		}
		return null;
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

	public Location getLocation() {
		return s;
	}

	public JDPoint getPoint() {
		return number;
	}

	List<Figure> roomFigures = new ArrayList<>(8);
	Figure[] roomFiguresArray = new Figure[8];

	private void updateRoomFiguresList() {
		roomFigures.clear();
		for (int i = 0; i < positions.length; i++) {
			Figure posFigure = positions[i].getFigure();
			if (posFigure != null) {
				roomFigures.add(posFigure);
			}
		}
	}

	public List<Figure> getRoomFigures() {
		return roomFigures;
	}

	public Figure[] getRoomFiguresArray() {
		return roomFigures.toArray(roomFiguresArray);
	}

	public int figureEntersDungeonHere(Figure figure, int moveDir, int round) {
		this.getDungeon().insertFigure(figure);
		return figureEnters(figure, moveDir, round);
	}

	public int figureEnters(Figure figure, int moveDir, int round) {

		int inRoomIndex = -1;

		if (moveDir == 0) {
			inRoomIndex = (int) (Math.random() * 8);
			while (positions[inRoomIndex].getFigure() != null) {
				inRoomIndex = (int) (Math.random() * 8);
			}
		}
		// TODO: check whether the pos is free!
		// edit: is automatically shifted to next position if not free
		if (moveDir == Dir.EAST) {
			inRoomIndex = 7;
		}
		if (moveDir == Dir.WEST) {
			inRoomIndex = 3;
		}
		if (moveDir == Dir.NORTH) {
			inRoomIndex = 5;
		}
		if (moveDir == Dir.SOUTH) {
			inRoomIndex = 1;
		}

		figureEntersAtPosition(figure, moveDir, inRoomIndex, round);
		return inRoomIndex;
	}

	/**
	 * This method is only used if figures come from nowhere that is
	 * - during de.jdungeon.level generation process
	 * - conjured figures being added during the de.jdungeon.game
	 *
	 * @param figure
	 * @param moveDir
	 * @param inRoomIndex
	 */
	public void figureEntersAtPosition(Figure figure, int moveDir, int inRoomIndex, int round) {
		Position position = positions[inRoomIndex];
		if (!this.getDungeon().equals(figure.getDungeon())) {
			figure.setActualDungeon(this.getDungeon());
		}

		if (figure.getPos() != null) {
			figure.getPos().figureLeaves();
		}
		if (moveDir != 0) {
			figure.setLookDir(moveDir);
		}

		// we 'discover' also all neighbour rooms of the entered room
		final List<Room> neighboursWithDoor = getNeighboursWithDoor();
		for (Room neighbourRoom : neighboursWithDoor) {
			figure.getViwMap()
					.setVisibilityStatus(neighbourRoom.number, RoomObservationStatus.VISIBILITY_SHRINE);
		}

		position.figureEntersHere(figure);
		figure.setLocation(this);

		if (figure.getViwMap() == null) {
			figure.createVisibilityMap(dungeon);
		}
		figure.getViwMap().setVisibilityStatus(getNumber(),
				RoomObservationStatus.VISIBILITY_ITEMS);
		figure.getViwMap().addVisibilityModifier(getNumber(), figure);

		this.checkFight(figure, round);
		updateRoomFiguresList();
	}

	public int getDeadFigurePos(Figure figure) {
		// TODO: refactor data structure for storing dead figures, not efficient for required access
		final Set<Position> positions = deadFigures.keySet();
		for (Position position : positions) {
			if (deadFigures.get(position).contains(figure)) {
				return position.getIndex();
			}
		}
		return 0;
	}

	Collection<Figure> getDeadFigures() {
		// TODO: refactor data structure for storing dead figures, not efficient for required access
		Collection<Figure> result = new HashSet<>();
		final Collection<Set<Figure>> collection = deadFigures.values();
		for (Set<Figure> figures : collection) {
			result.addAll(figures);
		}
		return result;
	}

	public void figureEntersAtPosition(Figure m, int position, int round) {
		figureEntersAtPosition(m, 0, position, round);
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

	public void figureDies(Figure figure) {
		if (figure.getRoom() != this) {
			throw new IllegalArgumentException();
		}

		// add de.jdungeon.figure to collection of dead figures in this room
		final Position currentPosition = figure.getPos();
		Set<Figure> deadFiguresOnPos = this.deadFigures.get(currentPosition);
		if (deadFiguresOnPos == null) {
			// TODO: use multi map
			deadFiguresOnPos = new HashSet<>();
			deadFigures.put(currentPosition, deadFiguresOnPos);
		}
		deadFiguresOnPos.add(figure);

		// finally leave position
		figureLeaves(figure);
	}

	public boolean figureLeaves(Figure m) {

		final Position pos = m.getPos();
		// might already be a new position in other room after fleeing
		if (pos != null && pos.getRoom().equals(this)) {
			pos.figureLeaves();
		}

		if (!getDeadFigures().contains(m)) {
			// dead figures may retain their vis status (for GUI's sake)
			final DungeonVisibilityMap roomVisibility = m.getViwMap();
			if (roomVisibility != null) {
				final RoomObservationStatus statusObject = roomVisibility.getStatusObject(getNumber());
				if (statusObject != null) {
					statusObject.removeVisibilityModifier(m);
				}
				roomVisibility.resetVisibilityStatus(this.number);
			}
		}
		updateRoomFiguresList();
		return true;
	}

	private void remItemFromArray(Item i) {
		for (int j = 0; j < itemArray.length; j++) {
			if (itemArray[j] != null && itemArray[j].equals(i)) {
				itemArray[j] = null;
				break;
			}
		}
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

	/**
	 * A room marked as wall is no room actually,
	 * its just an empty placehodler within the grid
	 * <p>
	 * A room cannot change its change-state during the de.jdungeon.game
	 *
	 * @return true if this room is marked as wall, false otherwise
	 */
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
			if (getLocation() != null) {
				shrine = getLocation().toString();
			}
		}
		p[1] = new Paragraph(shrine);
		p[1].setSize(20);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();

		String monster = new String();
		if (status.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FIGURES) {
			monster = "Figuren: " + getRoomFigures().size();
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

	@Override
	public String toString() {
		String s = " Raum Nr.: " + number;
		if (getChest() != null) {
			s += " truhe: ja  ";
		}
		if (getLocation() != null) {
			s += " Schrein:  " + getLocation();
		}
		return s;
	}

	@Deprecated
	public boolean isClaimed() {
		return false;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public Position[] getPositions() {
		return positions;
	}
}
