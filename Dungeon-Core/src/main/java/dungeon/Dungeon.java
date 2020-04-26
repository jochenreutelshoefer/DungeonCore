package dungeon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ai.AbstractAI;
import ai.DefaultMonsterIntelligence;
import dungeon.util.InfoUnitUnwrapper;
import dungeon.util.RouteInstruction;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.PlayerDiedEvent;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import game.ControlUnit;
import game.Turnable;
import location.Location;

/**
 * Diese Klasse bildet den Dungeon, welcher aus einem 2D-Array von Raeumen
 * besteht Enthaelt einen Backtracking-Algorithmus zum Suchen von Wegen.
 */
public class Dungeon implements Turnable, EventListener {

	public Room[][] theDungeon;

	private final JDPoint[][] points;

	private final JDPoint size;

	// TODO: remove this field
	@Deprecated
	private JDPoint heroPosition;

	private final Set<Location> shrines = new HashSet<Location>();

	private boolean gameOver = false;

	private final InfoUnitUnwrapper unwrapper;

	public Map<Integer, Figure> getFigureIndex() {
		return Collections.unmodifiableMap(figureIndex);
	}

	Map<Integer, Figure> figureIndex = new HashMap<>();

	public Dungeon(int sizeX, int sizeY) {
		EventManager.getInstance().registerListener(this);
		unwrapper = new InfoUnitUnwrapper(this);
		theDungeon = new Room[sizeX][sizeY];
		size = new JDPoint(sizeX, sizeY);
		points = new JDPoint[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				points[i][j] = new JDPoint(i, j);
				theDungeon[i][j] = new Room(i, j, this);
			}
		}
	}

	public void insertFigure(Figure figure) {
		figureIndex.put(figure.getFigureID(), figure);
	}

	public void prepare() {
		for (int i = 0; i < this.getSize().getX(); i++) {
			for (int j = 0; j < this.getSize().getY(); j++) {
				List<Figure> roomFigures = theDungeon[i][j].getRoomFigures();
				for (Figure roomFigure : roomFigures) {
					figureIndex.put(roomFigure.getFigureID(), roomFigure);
				}
			}
		}

		createVisibilityMaps();
		setMonsterControls();
	}

	private void createVisibilityMaps() {
		Set<Integer> s = figureIndex.keySet();
		for (Iterator<Integer> iter = s.iterator(); iter.hasNext(); ) {
			Integer element = iter.next();
			Figure f = figureIndex.get(element);
			f.createVisibilityMap(this);
		}
	}

	private void setMonsterControls() {
		Set<Integer> s = figureIndex.keySet();
		for (Iterator<Integer> iter = s.iterator(); iter.hasNext(); ) {
			Integer element = iter.next();
			Figure f = figureIndex.get(element);
			if (f instanceof Monster) {
				MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(f,
						f.getRoomVisibility());
				AbstractAI ai = new DefaultMonsterIntelligence();
				ai.setFigure(info);
				ControlUnit control = new FigureControl(info, ai);
				if (f.getControl() == null) {
					f.setControl(control);
				}
			}
		}
	}

	public Collection<Room> getAllRooms() {
		Set<Room> result = new HashSet<Room>();
		for (int i = 0; i < getRooms().length; i++) {
			result.addAll(Arrays.asList(getRooms()[i]));
		}
		return result;
	}

	public Collection<Room> getWallRooms() {
		Set<Room> result = new HashSet<Room>();
		Collection<Room> allRooms = getAllRooms();
		for (Room room : allRooms) {
			if (room.isWall()) {
				result.add(room);
			}
		}
		return result;
	}

	public Dungeon(int x, int y, int xh, int yh) {
		this(x, y);
		heroPosition = new JDPoint(xh, yh);
	}

	public JDPoint getPoint(int x, int y) {
		if (x >= 0 && y >= 0 && x < points.length && y < points[0].length) {
			return points[x][y];
		}

		return null;
	}

	public RoomObservationStatus[][] getNewRoomVisibilityMap(DungeonVisibilityMap map) {
		RoomObservationStatus[][] stats = new RoomObservationStatus[theDungeon.length][theDungeon[0].length];
		for (int i = 0; i < stats.length; i++) {
			for (int j = 0; j < stats[0].length; j++) {
				stats[i][j] = new RoomObservationStatus(map.getFigure(), getPoint(i, j));
			}
		}
		return stats;
	}

	@Override
	public void turn(int round) {
		shrinesTurn(round);
		roomsTurn(round);
	}

	public void addShrine(Location s) {
		if (s != null) {
			shrines.add(s);
		}
	}

	public JDPoint getRandomPoint() {
		int x = (int) (Math.random() * size.getX());
		int y = (int) (Math.random() * size.getY());
		return getPoint(x, y);
	}

	private void shrinesTurn(int round) {
		for (Location shrine : shrines) {
			shrine.turn(round);
		}
	}

	public JDPoint getSize() {
		return size;
	}

	public Room getRoom(int x, int y) {
		return getRoom(new JDPoint(x, y));
	}

	public Room getRoom(JDPoint p) {
		if (p == null) {
			return null;
		}
		if (p.getY() < 0 || p.getY() >= this.getSize().getY()
				|| p.getX() < 0 || p.getX() >= this.getSize().getX()) {
			// invalid coordinates for this dungeon
			return null;
		}
		return theDungeon[p.getX()][p.getY()];
	}

	public Room[][] getRooms() {
		return theDungeon;
	}

	public List<Room> getNeighbourRooms(Room r) {
		List<Room> l = new LinkedList<Room>();
		JDPoint p = r.getPoint();
		int x = p.getX();
		int y = p.getY();
		Room neighbours[] = new Room[4];
		neighbours[0] = getRoomNr(x - 1, y);
		neighbours[1] = getRoomNr(x + 1, y);
		neighbours[2] = getRoomNr(x, y - 1);
		neighbours[3] = getRoomNr(x, y + 1);
		for (int i = 0; i < 4; i++) {
			if (neighbours[i] != null) {
				l.add(neighbours[i]);
			}
		}
		return l;
	}

	public Room getRoomNr(int x, int y) {
		if ((x >= 0) && (x < theDungeon.length) && (y >= 0)
				&& (y < theDungeon[0].length)) {
			return theDungeon[x][y];
			// }
		}
		else {
			return null;
		}
	}

	public RoomInfo getRoomInfoNr(int x, int y, DungeonVisibilityMap status) {
		return RoomInfo.makeRoomInfo(getRoomNr(x, y), status);
	}

	public Room getRoomAt(Room r, RouteInstruction.Direction dir) {
		if (r == null) {
			return null;
		}
		if (dir == RouteInstruction.Direction.North) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX(), p.getY() - 1);
		}
		else if (dir == RouteInstruction.Direction.East) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX() + 1, p.getY());
		}
		else if (dir == RouteInstruction.Direction.West) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX() - 1, p.getY());
		}
		else if (dir == RouteInstruction.Direction.South) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX(), p.getY() + 1);
		}
		else {
			return null;
		}
	}

	@Deprecated
	public JDPoint getHeroPosition() {
		return heroPosition;
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	private void roomsTurn(int round) {
		for (int i = 0; i < theDungeon.length; i++) {
			for (int j = 0; j < theDungeon[0].length; j++) {
				if (this.gameOver) {
					return;
				}
				theDungeon[i][j].turn(round);
			}
		}
	}



	@Override
	public Collection<Class<? extends Event>> getEvents() {
		return Collections.singletonList(PlayerDiedEvent.class);
	}

	@Override
	public void notify(Event event) {
		if (event instanceof PlayerDiedEvent) {
			this.gameOver = true;
		}
	}

	public InfoUnitUnwrapper getUnwrapper() {
		return unwrapper;
	}

	public void removeFigureFromIndex(Figure figure) {
		figureIndex.remove(figure.getFigureID());
	}

	public Collection<Figure> collectAllFigures() {
		List<Figure> result = new ArrayList<>();
		for (int i = 0; i < theDungeon.length; i++) {
			for (int j = 0; j < theDungeon[0].length; j++) {
				result.addAll(theDungeon[i][j].getRoomFigures());
			}
		}
		return result;
	}
}


