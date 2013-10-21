package dungeon;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.monster.Monster;
import game.DungeonGame;
import game.Turnable;

import java.util.LinkedList;
import java.util.List;

import shrine.Shrine;
import test.TestTracker;

/**
 * Diese Klasse bildet den Dungeon, welcher aus einem 2D-Array von Raeumen
 * besteht Enthaelt einen Backtracking-Algorithmus zum Suchen von Wegen.
 * 
 * 
 */
public class Dungeon implements Turnable {

	public Room[][] theDungeon;

	private final JDPoint[][] points;

	private final JDPoint size;

	private JDPoint heroPosition;

	private final List<Shrine> shrines = new LinkedList<Shrine>();

	private final DungeonGame game;

	public JDPoint nature_brood_point;

	public Dungeon(int x, int y, int xh, int yh, DungeonGame g) {

		this.game = g;

		theDungeon = new Room[x][y];
		size = new JDPoint(x, y);
		points = new JDPoint[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				points[i][j] = new JDPoint(i, j);
				theDungeon[i][j] = new Room(i, j, this);
			}
		}
		JDPoint.setPoints(points);
		heroPosition = JDPoint.getPoint(xh, yh);
	}

	public JDPoint getPoint(int x, int y) {
		if (x < points.length && y < points[0].length) {
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

	public List<Room> findShortestWayFromTo(JDPoint p1, JDPoint p2,
			DungeonVisibilityMap map) {
		return findShortestWayFromTo(getRoom(p1), getRoom(p2), map);
	}

	private void removeCycles(List<Room> l) {
		if (l == null) {
		} else {
			// System.out.println("Removing Cycles!");
			int index = 0;
			// System.out.println("Liste: " + l.size());
			while (index < l.size() - 1) {
				// System.out.print("Pruefe Raum mit index: " + index + " :");
				Room r = l.get(index);
				// System.out.println(r.toString());
				int lastAppearence = -1;
				for (int i = index + 1; i < l.size(); i++) {

					Room next = l.get(i);
					if (next == r) {
						// System.out.println("Wiedergefunden! an Stelle: " +
						// i);
						lastAppearence = i;
					}
				}
				if (lastAppearence > -1) {
					//doPrint(r.toString() + " nochmal an Stelle: "
					//+ lastAppearence + " : "
					//		+ ((Room) l.get(lastAppearence)).toString());
					for (int i = index; i < lastAppearence; i++) {
						Room rx = l.remove(index);
						//doPrint("loesche aus Liste: " + rx.toString());
					}
				}
				index++;

			}
		}
	}

	private void removeCycles2(Way l) {
		if (l == null) {
		} else {
			// System.out.println("Removing Cycles!");
			int index = 0;
			// System.out.println("Liste: " + l.size());
			while (index < l.size() - 1) {
				// System.out.print("Pruefe Raum mit index: " + index + " :");
				Room r = l.getRooms().get(index);
				// System.out.println(r.toString());
				int lastAppearence = -1;
				for (int i = index + 1; i < l.getRooms().size(); i++) {

					Room next = l.getRooms().get(i);
					if (next == r) {
						// System.out.println("Wiedergefunden! an Stelle: " +
						// i);
						lastAppearence = i;
					}
				}
				if (lastAppearence > -1) {
					doPrint(r.toString()
							+ " nochmal an Stelle: "
							+ lastAppearence
							+ " : "
							+ l.getRooms().get(lastAppearence)
									.toString());
					for (int i = index; i < lastAppearence; i++) {
						Room rx = l.getRooms().remove(index);
						doPrint("loesche aus Liste: " + rx.toString());
					}
				}
				index++;

			}
		}
	}

	public int getFirstStepFromTo(Room r1, Room r2, DungeonVisibilityMap map) {
		if (r1 == r2) {
			System.out.println("angekommen!");
			return 0;
		}
		List way = findShortestWayFromTo(r1, r2, map);
		if (way == null) {
			System.out.println("way ist null!");
			return 0;
		}
		// System.out.println("Von " + r1.toString() + "nach " + r2.toString());
		boolean noWay = false;
		Room next = null;
		if (way.size() == 1) {
			System.out.println("no way");
			noWay = true;
		} else {

			next = ((Room) way.get(1));

			// System.out.println("gehe erstmal nach: " + next.toString());
		}
		int dir = 0;
		if (next != null && next.hasConnectionTo(r1)) {

			dir = r1.getConnectionDirectionTo(next);
		} else {
			System.out.println("Erster Schritt passt nicht!");

		}
		return dir;
	}

	public int getFirstStepFromTo2(Room r1, Room r2) {
		if (r1 == r2) {
			System.out.println("angekommen!");
			return 0;
		}
		Way way = findShortestWayFromTo2(r1, r2, false);
		if (way == null) {
			System.out.println("way unblocked ist null!");
			way = findShortestWayFromTo2(r1, r2, true);
			if (way == null) {
				System.out.println("way blocked auch null!!!!!");
			} else {
				System.out.println("blocked way gefunden!");
			}
			// return 0;
		}
		// System.out.println("Von " + r1.toString() + "nach " + r2.toString());
		boolean noWay = false;
		Room next = null;
		if (way.size() == 1) {
			System.out.println("no way");
			noWay = true;
		} else {

			next = way.get(1).getRoom();

			// System.out.println("gehe erstmal nach: " + next.toString());
		}
		int dir = 0;
		if (next != null && next.hasConnectionTo(r1)) {

			dir = r1.getConnectionDirectionTo(next);
		} else {
			System.out.println("Erster Schritt passt nicht!");

		}
		return dir;
	}

	private void builtShortCuts(List<Room> way) {
		if (way == null) {
			// System.out.println("Weg ist null!");
			return;
		}
		int i = 0;
		// System.out.println();
		// System.out.println("SHORTCUTTING!!!!");
		while (i < way.size()) {
			Room r1 = ( way.get(i));
			for (int j = way.size() - 1; j > 0; j--) {
				Room r2 = (way.get(j));
				if (r2.hasConnectionTo(r1)) {
					doPrint(r2.getNumber().toString()
							+ " has direct Connection to: "
							+ r1.getNumber().toString());
					for (int k = i + 1; k < j - i; k++) {
						// System.out.println(
						// "Removing:"
						// + ((room) way.get(i + 1)).getNumber().toString());
						way.remove(i + 1);
					}
					break;
				}
			}
			i++;

		}
		// System.out.println("SHORTCUTTING BEENDET");
		// System.out.println();
	}

	private void builtShortCuts2(Way way) {
		if (way == null) {
			// System.out.println("Weg ist null!");
			return;
		}
		int i = 0;
		// System.out.println();
		// System.out.println("SHORTCUTTING!!!!");
		while (i < way.getRooms().size()) {
			Room r1 = (way.getRooms().get(i));
			for (int j = way.getRooms().size() - 1; j > 0; j--) {
				Room r2 = (way.getRooms().get(j));
				if (r2.hasConnectionTo(r1)) {
					doPrint(r2.getNumber().toString()
							+ " has direct Connection to: "
							+ r1.getNumber().toString());
					for (int k = i + 1; k < j - i; k++) {
						// System.out.println(
						// "Removing:"
						// + ((room) way.get(i + 1)).getNumber().toString());
						way.getRooms().remove(i + 1);
					}
					break;
				}
			}
			i++;

		}
		// System.out.println("SHORTCUTTING BEENDET");
		// System.out.println();
	}

	public List<Room> findShortestWayFromTo(Room room1, Room room2,
			DungeonVisibilityMap map) {

		RoomInfo r1 = RoomInfo.makeRoomInfo(room1, map);
		RoomInfo r2 = RoomInfo.makeRoomInfo(room2, map);
		//doPrint();
		//doPrint();
		//doPrint("Beginne Wegsuche: " + r1.toString() + " - " + r2.toString());
		getGame().setTestTracker(new TestTracker());
		// LinkedList way = searchWayGreedy(r1, r2, new LinkedList());
		List<Room> way = searchWayBackTrack(r1, r2);
		removeCycles(way);
		builtShortCuts(way);
		// System.out.println("GEFUNDNENER WEG !!!!");
		if (way != null) {

			for (int i = 0; i < way.size(); i++) {
				doPrint(way.get(i).getNumber().toString());

			}
		}
		return way;
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

	public Way findShortestWayFromTo2(Room r1, Room r2, boolean blocked) {

		// doPrint();
		// doPrint();
		// doPrint("Beginne Wegsuche: " + r1.toString() + " - " +
		// r2.toString());
		getGame().setTestTracker(new TestTracker());
		// LinkedList way = searchWayGreedy(r1, r2, new LinkedList());
		Way way = searchWayBackTrack2(r1, r2, blocked);
		removeCycles2(way);
		builtShortCuts2(way);
		// System.out.println("GEFUNDNENER WEG !!!!");
		// if (way != null) {
		//
		// for (int i = 0; i < way.size(); i++) {
		// doPrint(((room) way.get(i)).getNumber().toString());
		//
		// }
		// }
		return way;
	}

	private Way searchWayBackTrack2(Room fromR, Room toR, boolean blocked) {

		DungeonVisibilityMap map = DungeonVisibilityMap.getAllVisMap(this);
		RoomInfo from = RoomInfo.makeRoomInfo(fromR, map);
		RoomInfo to = RoomInfo.makeRoomInfo(toR, map);

		if (from == to) {
			System.out.println("Start gleich Ziel!");
			LinkedList<RoomInfo> l = new LinkedList<RoomInfo>();
			l.add(from);

			return new Way(l, false);
		}
		LinkedList<Tupel> list = new LinkedList<Tupel>();
		list.add(new Tupel(from, new explorer(from, blocked)));
		list = explore2(list, to, blocked, 0);
		if (list == null) {
			return null;
		}
		LinkedList<RoomInfo> erg = new LinkedList<RoomInfo>();

		for (int i = 0; i < list.size(); i++) {
			erg.add(list.get(i).r);
		}
		// doPrint("ERGEBNISS!!!");
		// doPrintBisWay(erg);
		return new Way(erg, false);
	}

	private List<Room> searchWayBackTrack(RoomInfo from, RoomInfo to) {
		doPrint();
		doPrint();
		doPrint();
		doPrint();

		doPrint("VON: " + from.getNumber().toString() + " NACH: "
				+ to.getNumber().toString());
		if (from.equals(to)) {
			doPrint("Start gleich Ziel!");
			return new LinkedList<Room>();
		}
		LinkedList<Tupel> list = new LinkedList<Tupel>();
		list.add(new Tupel(from, new explorer(from, false)));
		explore(list, to);
		LinkedList<Room> erg = new LinkedList<Room>();
		for (int i = 0; i < list.size(); i++) {
			erg.add(list.get(i).r.getRoom());
		}
		doPrint("ERGEBNISS!!!");
		doPrintBisWay(erg);
		return erg;
	}

	private void explore(LinkedList<Tupel> list, RoomInfo to) {

		boolean more = walkBackToLastUnexploredPoint(list);
		if (more) {

			Tupel t = (list.getLast());
			doPrint("Bin jetzt bei Raum: " + t.r.getNumber().toString());
			explorer ex = t.exp;
			int dir = ex.getOpenDir(to);
			ex.setExplored(dir);
			RoomInfo next = t.r.getDoor(dir + 1).getOtherRoom(t.r);

			doPrint("und exploriere nach :"
					+ RouteInstruction.dirToString(dir + 1) + " in Raum: "
					+ next.getNumber().toString());
			explorer newExp = new explorer(next, false);
			configExp(newExp, list);
			Tupel tup = new Tupel(next, newExp);
			if(!list.contains(tup)) {
				list.add(tup);
			}
			if (next.equals(to)) {
				doPrint("BiN DA !");
				return;
			}
			explore(list, to);
		}
	}

	private LinkedList<Tupel> explore2(LinkedList<Tupel> list, RoomInfo to, boolean blocked,
			int cnt) {
		cnt++;
		if (cnt > 2000) {
			System.out.println("Endlosschleife in dungeon.explore2()");
			System.exit(0);
		}

		boolean more = walkBackToLastUnexploredPoint(list);
		if (more) {

			Tupel t = (list.getLast());
			// System.out.println("Bin jetzt bei Raum: " +
			// t.r.getNumber().toString());
			explorer ex = t.exp;
			int dir = ex.getOpenDir(to);
			ex.setExplored(dir);
			RoomInfo next = t.r.getDoor(dir + 1).getOtherRoom(t.r);

			// System.out.println(
			// "und exploriere nach :"
			// + routeInstruction.dirToString(dir + 1)
			// + " in Raum: "
			// + next.getNumber().toString());
			explorer newExp = new explorer(next, blocked);
			configExp(newExp, list);
			list.add(new Tupel(next, newExp));
			if (next.equals(to)) {
				System.out.println("BiN DA !");
				return list;
			}
			return explore2(list, to, blocked, cnt);
		} else {
			System.out.println("nimmer-more!returning null");
			return null;
		}
	}

	private void configExp(explorer exp, List<Tupel> list) {
		for (int i = 0; i < list.size(); i++) {
			RoomInfo r1 = list.get(i).r;
			if (exp.r.hasConnectionTo(r1)) {
				int dir = exp.r.getConnectionDirectionTo(r1);

				exp.setExplored2(dir - 1);
			}
		}
	}

	private boolean walkBackToLastUnexploredPoint(LinkedList<Tupel> list) {
		doPrint("Laufe zur�ck zum n�chsten offenen Punkt!");
		explorer ex = list.getLast().exp;
		configExp(ex, list);
		if (ex.stillOpen()) {
			//doPrint("Das Ende ist noch offen!");
			return true;
		} else {
			//doPrint("Muss zurücklaufen: List.size(): " + list.size());
			int k = list.size() - 2;
			while (!ex.stillOpen()) {
				if (k < 0) {
					//doPrint("Zurück bis an den Anfang, KEIN WEG GEFUNDEN!!!");
					return false;
				}
				Tupel t = (list.get(k));
				ex = t.exp;
				if (!ex.stillOpen()) {
					//doPrint(t.r.getNumber().toString()
					//		+ " ist nicht mehr offen --> weiter zurück");
				} else {
					//doPrint(t.r.getNumber().toString()
					//		+ " ist noch offen --> hier weitermachen");
				}
				list.add(t);
				k--;
				//doPrint("k: " + k);

			}
		}
		return true;
	}

	private int getLastDir(List<Room> way) {
		if (way.size() < 2) {
			//doPrint("letzte Richtung noch nicht verfuegbar");
			return 0;
		}
		Room r1 = way.get(way.size() - 2);
		Room r2 = way.get(way.size() - 1);
		int dir = r1.getConnectionDirectionTo(r2);
		if (dir == 0) {
			//doPrint("Error bei lastDir() --> Weg gar nicht moeglich");
		}
		return dir;

	}



	private void doPrintBisWay(List<Room> l) {

		for (int i = 0; i < l.size(); i++) {
			if (printing)
				System.out.println(i + ": "
						+ l.get(i).getNumber().toString());

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

	public Room getRoom(JDPoint p) {
		if (p == null) {
			return null;
		}
		Room r = null;
		try {
			r = theDungeon[p.getX()][p.getY()];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
		if (r.isWall()) {
			return null;
		} else {
			return r;
		}
	}

	public Room getRoomAt(Room r, int dir) {
		if (r == null) {
			return null;
		}
		if (dir == RouteInstruction.NORTH) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX(), p.getY() - 1);
		} else if (dir == RouteInstruction.EAST) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX() + 1, p.getY());
		} else if (dir == RouteInstruction.WEST) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX() - 1, p.getY());
		} else if (dir == RouteInstruction.SOUTH) {
			JDPoint p = r.getNumber();
			return getRoomNr(p.getX(), p.getY() + 1);
		} else {
			doPrint("ungueltiges getRoomAt in Dungeon:" + dir);
			return null;

		}
	}

	public JDPoint getHeroPosition() {
		return heroPosition;
	}

	public void setHeroPosition(JDPoint p) {
		heroPosition = p;
	}

	public static int getNeighbourDirectionFromTo(JDPoint fromP, JDPoint toP) {
		if (fromP.getX() == toP.getX()) {
			if (fromP.getY() == toP.getY() - 1) {
				return RouteInstruction.SOUTH;
			} else if (fromP.getY() == toP.getY() + 1) {
				return RouteInstruction.NORTH;

			} else {
				doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
				doPrint(fromP.toString() + " - " + toP.toString());
				return 0;
			}
		} else if (fromP.getY() == toP.getY()) {
			if (fromP.getX() == toP.getX() - 1) {
				// doPrint("Gefundene Richtung: EAST!");
				return RouteInstruction.EAST;
			} else if (fromP.getX() == toP.getX() + 1) {
				// doPrint("Gefundene Richtung: WEST!");
				return RouteInstruction.WEST;

			} else {
				doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
				doPrint(fromP.toString() + " - " + toP.toString());

				return 0;
			}
		} else {
			doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
			doPrint(fromP.toString() + " - " + toP.toString());

			return 0;
		}
	}

	public static int getNeighbourDirectionFromTo(Room from, Room to) {
		JDPoint fromP = from.getNumber();
		JDPoint toP = to.getNumber();
		return getNeighbourDirectionFromTo(fromP, toP);

	}

	public DungeonGame getGame() {
		return game;
	}

	private static boolean printing = false;

	private static void doPrint(String s) {
		if (printing) {
			System.out.println(s);
		}
	}

	private static void doPrintoz(String s) {
		if (printing) {
			System.out.print(s);
		}
	}

	private static void doPrint() {
		if (printing) {

			System.out.println();
		}
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
				if (game.isGameOver()) {
					return;
				}
				theDungeon[i][j].turn(round);
			}
		}
	}

	public LinkedList getAlleMonster() {
		LinkedList alle = new LinkedList();
		Room[][] field = getRooms();
		for (int i = 0; i < game.DungeonSizeX; i++) {
			for (int j = 0; j < game.DungeonSizeY; j++) {
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

}

class explorer {
	int[] directions;

	RoomInfo r;

	boolean blocked = false;

	public explorer(RoomInfo r, boolean blocked) {

		this.blocked = blocked;
		directions = new int[4];
		this.r = r;

		if (blocked) {
			for (int i = 0; i < 4; i++) {

				if (r.getDoors()[i] != null) {
					directions[i] = 2;
				} else {
					directions[i] = 0;
				}
			}
		} else {

			for (int i = 0; i < 4; i++) {
				DoorInfo[] doors = r.getDoors();
				if (doors == null) {

				} else {
					if (doors[i] != null && doors[i].isPassable()) {
						directions[i] = 2;
					} else {
						directions[i] = 0;
					}
				}
			}
		}
	}

	public void setExplored(int k) {
		if (directions[k] == 2) {
			directions[k] = 1;
		} else if (directions[k] == 0) {
			// System.out.println("SetExplored, wo gar nichts ist!");
		} else {
			// System.out.println("SetExplored, obwohls schon war!");
		}
	}

	public void setExplored2(int k) {
		if (directions[k] == 2) {
			directions[k] = 1;
		}
	}

	public int getTotalDirections() {
		int cnt = 0;
		for (int i = 0; i < 4; i++) {
			if (directions[i] != 0) {
				cnt++;
			}
		}
		return cnt;
	}

	public int getFreeDirections() {
		int cnt = 0;
		for (int i = 0; i < 4; i++) {
			if (directions[i] == 2) {
				cnt++;
			}
		}
		return cnt;
	}

	public int getOpenDir(RoomInfo target) {
		// Room target= this.

		boolean[] openDirs = new boolean[4];
		for (int i = 0; i < 4; i++) {
			if (directions[i] == 2) {
				openDirs[i] = true;
				;
			} else {
				openDirs[i] = false;
			}
		}
		int dx = target.getNumber().getX() - r.getNumber().getX();
		int dy = target.getNumber().getY() - r.getNumber().getY();

		int dirx = 0;
		int diry = 0;
		if (dx > 0) {
			dirx = 1;
		} else {
			dirx = 3;
		}

		if (dy > 0) {
			diry = 2;
		} else {
			diry = 0;
		}

		int dirOrder[] = new int[4];
		if (Math.abs(dx) > Math.abs(dy)) {
			dirOrder[0] = dirx;
			dirOrder[1] = diry;
			dirOrder[2] = (diry + 2) % 4;
			dirOrder[3] = (dirx + 2) % 4;
		} else {
			dirOrder[0] = diry;
			dirOrder[1] = dirx;
			dirOrder[2] = (dirx + 2) % 4;
			dirOrder[3] = (diry + 2) % 4;
		}
		for (int i = 0; i < 4; i++) {
			if (openDirs[dirOrder[i]] == true) {
				// System.out.println("Returning best Dir: "+dirOrder[i]);
				return dirOrder[i];
			}
		}
		// System.out.println("getOpenDir ung�ltiges!");
		return -1;
	}

	public boolean stillOpen() {
		return getFreeDirections() > 0;
	}
}

class Tupel {

	public RoomInfo r;

	public explorer exp;

	public Tupel(RoomInfo r, explorer e) {
		this.r = r;
		exp = e;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Tupel) {
			if(r.equals(((Tupel)o).r)) {
				return true;
			}
		}
		return false;
	}

}
