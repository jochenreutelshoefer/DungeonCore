package dungeon.util;

import java.util.LinkedList;
import java.util.List;

import test.TestTracker;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.Way;
import figure.DungeonVisibilityMap;
import game.DungeonGame;

public class DungeonUtils {

	public static List<Room> findShortestWayFromTo(Dungeon d, JDPoint p1,
			JDPoint p2, DungeonVisibilityMap map) {
		return findShortestWayFromTo(d.getRoom(p1), d.getRoom(p2), map);
	}
	


	public static List<Room> findShortestWayFromTo(Room room1, Room room2,
			DungeonVisibilityMap map) {

		RoomInfo r1 = RoomInfo.makeRoomInfo(room1, map);
		RoomInfo r2 = RoomInfo.makeRoomInfo(room2, map);
		// doPrint();
		// doPrint();
		// doPrint("Beginne Wegsuche: " + r1.toString() + " - " +
		// r2.toString());
		return findShortestWayFromTo(r1, r2);
	}

	public static List<Room> findShortestWayFromTo(RoomInfo r1, RoomInfo r2) {
		DungeonGame.getInstance().setTestTracker(new TestTracker());
		// LinkedList way = searchWayGreedy(r1, r2, new LinkedList());
		List<Room> way = searchWayBackTrack(r1, r2);
		removeCycles(way);
		builtShortCuts(way);

		return way;
	}

	public static int stepRight(int pos) {
		if (pos == 7) {
			return 0;
		} else {
			return pos + 1;
		}
	}

	public static int stepLeft(int pos) {
		if (pos == 0) {
			return 7;
		} else {
			return pos - 1;
		}
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

	private static void builtShortCuts(List<Room> way) {
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

	private static void builtShortCuts2(Way way) {
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

	private static void explore(List<Tupel> list, RoomInfo to) {
	
		boolean more = walkBackToLastUnexploredPoint(list);
		if (more) {
	
			Tupel t = (list.get(list.size() - 1));
			Explorer ex = t.exp;
			int dir = ex.getOpenDir(to);
			ex.setExplored(dir);
			RoomInfo next = t.r.getDoor(dir + 1).getOtherRoom(t.r);
	
			Explorer newExp = new Explorer(next, false);
			configExp(newExp, list);
			Tupel tup = new Tupel(next, newExp);
			if(!list.contains(tup)) {
				list.add(tup);
			}
			if (next.equals(to)) {
				return;
			}
			explore(list, to);
		}
	}

	private static List<Tupel> explore2(List<Tupel> list, RoomInfo to,
			boolean blocked,
			int cnt) {
		cnt++;
		if (cnt > 2000) {
			System.out.println("Endlosschleife in dungeon.explore2()");
			System.exit(0);
		}
	
		boolean more = walkBackToLastUnexploredPoint(list);
		if (more) {
	
			Tupel t = (list.get(list.size() - 1));
			// System.out.println("Bin jetzt bei Raum: " +
			// t.r.getNumber().toString());
			Explorer ex = t.exp;
			int dir = ex.getOpenDir(to);
			ex.setExplored(dir);
			RoomInfo next = t.r.getDoor(dir + 1).getOtherRoom(t.r);
	
			// System.out.println(
			// "und exploriere nach :"
			// + routeInstruction.dirToString(dir + 1)
			// + " in Raum: "
			// + next.getNumber().toString());
			Explorer newExp = new Explorer(next, blocked);
			configExp(newExp, list);
			list.add(new Tupel(next, newExp));
			if (next.equals(to)) {
				return list;
			}
			return explore2(list, to, blocked, cnt);
		} else {
			return null;
		}
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

	private static void configExp(Explorer exp, List<Tupel> list) {
		for (int i = 0; i < list.size(); i++) {
			RoomInfo r1 = list.get(i).r;
			if (exp.r.hasConnectionTo(r1)) {
				int dir = exp.r.getConnectionDirectionTo(r1);
	
				exp.setExplored2(dir - 1);
			}
		}
	}

	public static RouteInstruction.Direction getNeighbourDirectionFromTo(
			Room from, Room to) {
		JDPoint fromP = from.getNumber();
		JDPoint toP = to.getNumber();
		return getNeighbourDirectionFromTo(fromP, toP);
	
	}

	public static RouteInstruction.Direction getNeighbourDirectionFromTo(
			JDPoint fromP, JDPoint toP) {
		if (fromP.getX() == toP.getX()) {
			if (fromP.getY() == toP.getY() - 1) {
				return RouteInstruction.Direction.South;
			} else if (fromP.getY() == toP.getY() + 1) {
				return RouteInstruction.Direction.North;
	
			} else {
				// doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
				return null;
			}
		} else if (fromP.getY() == toP.getY()) {
			if (fromP.getX() == toP.getX() - 1) {
				// doPrint("Gefundene Richtung: EAST!");
				return RouteInstruction.Direction.East;
			} else if (fromP.getX() == toP.getX() + 1) {
				// doPrint("Gefundene Richtung: WEST!");
				return RouteInstruction.Direction.West;
	
			} else {
				// doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
	
				return null;
			}
		} else {
			// doPrint("Sind keine Nachbarn: dungeon.setNeighbourDirectionFromTo()");
	
			return null;
		}
	}

	private static void removeCycles(List<Room> l) {
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
						l.remove(index);
						//doPrint("loesche aus Liste: " + rx.toString());
					}
				}
				index++;
	
			}
		}
	}

	private static void removeCycles2(Way l) {
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
					/*
					 * 
					 * doPrint(r.toString() + " nochmal an Stelle: " +
					 * lastAppearence + " : " + l.getRooms().get(lastAppearence)
					 * .toString());
					 */

					for (int i = index; i < lastAppearence; i++) {
						l.getRooms().remove(index);
					}
				}
				index++;
	
			}
		}
	}

	private static boolean walkBackToLastUnexploredPoint(List<Tupel> list) {
		Explorer ex = list.get(list.size() - 1).exp;
		configExp(ex, list);
		if (ex.stillOpen()) {
			// doPrint("Das Ende ist noch offen!");
			return true;
		} else {
			// doPrint("Muss zurücklaufen: List.size(): " + list.size());
			int k = list.size() - 2;
			while (!ex.stillOpen()) {
				if (k < 0) {
					// doPrint("Zurück bis an den Anfang, KEIN WEG GEFUNDEN!!!");
					return false;
				}
				Tupel t = (list.get(k));
				ex = t.exp;
				if (!ex.stillOpen()) {
					// doPrint(t.r.getNumber().toString()
					// + " ist nicht mehr offen --> weiter zurück");
				} else {
					// doPrint(t.r.getNumber().toString()
					// + " ist noch offen --> hier weitermachen");
				}
				list.add(t);
				k--;
				// doPrint("k: " + k);

			}
		}
		return true;
	}

	private static Way searchWayBackTrack2(Dungeon d, Room fromR, Room toR,
			boolean blocked) {
	
		DungeonVisibilityMap map = DungeonVisibilityMap.getAllVisMap(d);
		RoomInfo from = RoomInfo.makeRoomInfo(fromR, map);
		RoomInfo to = RoomInfo.makeRoomInfo(toR, map);
	
		if (from == to) {
			// System.out.println("Start gleich Ziel!");
			LinkedList<RoomInfo> l = new LinkedList<RoomInfo>();
			l.add(from);
			return new Way(l, false);
		}
		List<Tupel> list = new LinkedList<Tupel>();
		list.add(new Tupel(from, new Explorer(from, blocked)));
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

	public static Way findShortestWayFromTo2(Dungeon d, Room r1, Room r2,
			boolean blocked) {

		// doPrint();
		// doPrint();
		// doPrint("Beginne Wegsuche: " + r1.toString() + " - " +
		// r2.toString());
		DungeonGame.getInstance().setTestTracker(new TestTracker());
		// LinkedList way = searchWayGreedy(r1, r2, new LinkedList());
		Way way = searchWayBackTrack2(d, r1, r2, blocked);
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

	public static int getFirstStepFromTo2(Dungeon d, Room r1, Room r2) {
		if (r1 == r2) {
			System.out.println("angekommen!");
			return 0;
		}
		Way way = findShortestWayFromTo2(d, r1, r2, false);
		if (way == null) {
			System.out.println("way unblocked ist null!");
			way = findShortestWayFromTo2(d, r1, r2, true);
			if (way == null) {
				System.out.println("way blocked auch null!!!!!");
			} else {
				System.out.println("blocked way gefunden!");
			}
			// return 0;
		}
		// System.out.println("Von " + r1.toString() + "nach " + r2.toString());
		Room next = null;
		if (way.size() == 1) {
			System.out.println("no way");
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

	public static int getFirstStepFromTo(Room r1, Room r2,
			DungeonVisibilityMap map) {
		if (r1 == r2) {
			System.out.println("angekommen!");
			return 0;
		}
		List<Room> way = findShortestWayFromTo(r1, r2, map);
		if (way == null) {
			System.out.println("way ist null!");
			return 0;
		}
		// System.out.println("Von " + r1.toString() + "nach " + r2.toString());
		Room next = null;
		if (way.size() == 1) {
			System.out.println("no way");
		} else {

			next = (way.get(1));

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

	private static List<Room> searchWayBackTrack(RoomInfo from, RoomInfo to) {
		if (from.equals(to)) {
			return new LinkedList<Room>();
		}
		LinkedList<Tupel> list = new LinkedList<Tupel>();
		list.add(new Tupel(from, new Explorer(from, false)));
		explore(list, to);
		List<Room> erg = new LinkedList<Room>();
		for (int i = 0; i < list.size(); i++) {
			erg.add(list.get(i).r.getRoom());
		}

		/*
		 * boolean targetFound = false; for (Room room : erg) {
		 * if(room.getNumber().equals(to.getNumber())) { targetFound = true; } }
		 * if(!targetFound) { return null; }
		 */
		return erg;
	}

}

class Tupel {

	public RoomInfo r;

	public Explorer exp;

	public Tupel(RoomInfo r, Explorer e) {
		this.r = r;
		exp = e;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Tupel) {
			if (r.equals(((Tupel) o).r)) {
				return true;
			}
		}
		return false;
	}

}

class Explorer {
	int[] directions;

	RoomInfo r;

	boolean blocked = false;

	public Explorer(RoomInfo r, boolean blocked) {

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
