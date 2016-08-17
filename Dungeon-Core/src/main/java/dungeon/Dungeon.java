package dungeon;

import dungeon.util.InfoUnitUnwrapper;
import event.Event;
import event.EventManager;
import event.GameOverEvent;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.monster.Monster;
import game.DungeonGame;
import game.Turnable;
import event.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import shrine.Shrine;
import dungeon.util.RouteInstruction;

/**
 * Diese Klasse bildet den Dungeon, welcher aus einem 2D-Array von Raeumen
 * besteht Enthaelt einen Backtracking-Algorithmus zum Suchen von Wegen.
 * 
 * 
 */
public class Dungeon implements Turnable, EventListener {

	public Room[][] theDungeon;

	private final JDPoint[][] points;

	private final JDPoint size;

	// TODO: remove this field
	@Deprecated
	private JDPoint heroPosition;

	private final List<Shrine> shrines = new LinkedList<Shrine>();

	private boolean gameOver = false;

	private InfoUnitUnwrapper unwrapper;

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
		JDPoint.setPoints(points);
	}

	public Collection<Room> getAllRooms() {
		Set<Room> result = new HashSet<Room>();
		for(int i = 0; i < getRooms().length; i++) {
			result.addAll(Arrays.asList(getRooms()[i]));
		}
		return result;
	}

	public Dungeon(int x, int y, int xh, int yh) {
		this(x, y);
		heroPosition = JDPoint.getPoint(xh, yh);
	}

	public JDPoint getPoint(int x, int y) {
		if (x >= 0 && y >= 0 && x < points.length && y < points[0].length) {
			return points[x][y];
		}

		return null;
	}

	public RoomObservationStatus[][] getNewRoomVisibilityMap(
			DungeonVisibilityMap map) {
		RoomObservationStatus[][] stats = new RoomObservationStatus[theDungeon.length][theDungeon[0].length];
		for (int i = 0; i < stats.length; i++) {
			for (int j = 0; j < stats[0].length; j++) {

				stats[i][j] = new RoomObservationStatus(map, getPoint(i, j));

			}
		}

		return stats;
	}

	@Override
	public void turn(int round) {
		shrinesTurn(round);
		roomsTurn(round);
	}

	public List<Shrine> getShrines() {
		return shrines;
	}

	public void addShrine(Shrine s) {
		shrines.add(s);
	}

	public JDPoint getRandomPoint() {
		int x = (int) (Math.random() * size.getX());
		int y = (int) (Math.random() * size.getY());
		return getPoint(x, y);
	}

	private void shrinesTurn(int round) {
		int j = shrines.size();
		for (int i = 0; i < j; i++) {
			Shrine s = (shrines.get(i));
			// doPrint("turning: "+s.toString());
			s.turn(round);
		}
	}

	public JDPoint getSize() {
		return size;
	}

	

	

	

	// public LinkedList searchWayGreedy(Room r1, Room r2, LinkedList bisWay) {
	//
	// bisWay.add(r1);
	// if (bisWay.size() > 50) {
	// doPrint("Kein Weg zu finden in 100 Zuegen!");
	// // throw new NullPointerException();
	//
	// return null;
	// }
	// if (r1 == r2) {
	// doPrint("FERTIG GEFUNDEN!");
	// return bisWay;
	// }
	//
	// int dx = r1.getNumber().getX() - r2.getNumber().getX();
	// int dy = r1.getNumber().getY() - r2.getNumber().getY();
	//
	// int distance = dx + dy;
	//
	// boolean[] dirs = getPossibleDirection(r1);
	// int dir = decideDir(dirs, dx, dy, bisWay);
	//
	// doPrintoz("Richtung: " + dir);
	//
	// Room next = this.getRoomAt(r1, dir);
	// getGame().getTracker().setLocation(next);
	//
	// // getGame().getGui().repaintSpielfeldBild();
	//
	// // try {Thread.sleep(1000);}catch(Exception e){}
	// doPrint(" zu Raum: " + next.toString());
	// doPrintBisWay(bisWay);
	// return searchWayGreedy(next, r2, bisWay);
	//
	// }

	public Room getRoom(JDPoint p) {
		if (p == null) {
			return null;
		}
		if(p.getY() < 0 || p.getY() >= this.getSize().getY()
				|| p.getX() < 0 || p.getX() >= this.getSize().getX()) {
			// invalid coordinates for this dungeon
			return null;
		}
		Room r = theDungeon[p.getX()][p.getY()];
		if (r.isWall()) {
			return null;
		} else {
			return r;
		}
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
			Room r = theDungeon[x][y];
			// if(r.isWall()) {
			// return null;
			// }
			// else {
			return r;
			// }
		} else {
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
		} else if (dir == RouteInstruction.Direction.East) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX() + 1, p.getY());
		} else if (dir == RouteInstruction.Direction.West) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX() - 1, p.getY());
		} else if (dir == RouteInstruction.Direction.South) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX(), p.getY() + 1);
		} else {
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

	// public void monsterTurn() {
	// LinkedList alle = getAlleMonster();
	// List fightingMonsters = null;
	// if(game.getFight()!= null) {
	// fightingMonsters = game.getFight().getMonstersL();
	// }
	// int length = alle.size();
	// // System.out.println("Anzahl Monster: " + length);
	// if (length > 0) {
	// int k = 0;
	// while (k < length) {
	// Monster m = ((Monster) alle.get(k));
	// if (fightingMonsters == null || !fightingMonsters.contains(m)) {
	// m.turn(0);
	// }
	//
	// k++;
	//
	// }
	// } else { // //System.out.println("Keine Monster mehr vorhanden");
	// }
	// }

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

	public List<Figure> getAlleMonster() {
		List<Figure> alle = new LinkedList<Figure>();
		Room[][] field = getRooms();
		for (int i = 0; i < this.size.getX(); i++) {
			for (int j = 0; j < this.size.getY(); j++) {
				addMonsters(field[i][j], alle);

			}
		}
		return alle;
	}

	// fügt monster aus ruam der liste alle hinzu
	private void addMonsters(Room raum, List<Figure> alle) {
		List<Figure> liste = raum.getRoomFigures();
		int length = liste.size();
		if (length > 0) {
			int i = 0;
			while (i < length) {
				Monster m = (Monster) liste.get(i);
				alle.add(m);
				// //System.out.println(m.toString() + " hinzugefuegt");
				i++;
			}
		} else {
			/** System.out.println("Keine Monster");* */
		}

	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		List<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>(1);
		events.add(GameOverEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if(event instanceof GameOverEvent) {
			this.gameOver = true;
		}
	}

	public InfoUnitUnwrapper getUnwrapper() {
		return unwrapper;
	}

	// public LinkedList searchWayGreedy(Room r1, Room r2, LinkedList bisWay) {
	//
	// bisWay.add(r1);
	// if (bisWay.size() > 50) {
	// doPrint("Kein Weg zu finden in 100 Zuegen!");
	// // throw new NullPointerException();
	//
	// return null;
	// }
	// if (r1 == r2) {
	// doPrint("FERTIG GEFUNDEN!");
	// return bisWay;
	// }
	//
	// int dx = r1.getNumber().getX() - r2.getNumber().getX();
	// int dy = r1.getNumber().getY() - r2.getNumber().getY();
	//
	// int distance = dx + dy;
	//
	// boolean[] dirs = getPossibleDirection(r1);
	// int dir = decideDir(dirs, dx, dy, bisWay);
	//
	// doPrintoz("Richtung: " + dir);
	//
	// Room next = this.getRoomAt(r1, dir);
	// getGame().getTracker().setLocation(next);
	//
	// // getGame().getGui().repaintSpielfeldBild();
	//
	// // try {Thread.sleep(1000);}catch(Exception e){}
	// doPrint(" zu Raum: " + next.toString());
	// doPrintBisWay(bisWay);
	// return searchWayGreedy(next, r2, bisWay);
	//
	// }


}

