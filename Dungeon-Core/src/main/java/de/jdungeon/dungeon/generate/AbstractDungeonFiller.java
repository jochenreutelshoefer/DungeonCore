/**
 * Abstrakte Oberklasse zum Fuellen eines Dungeons.
 * Sie stellt eine Reihe von Werkzeugen zum gestalten des Dungeons zur
 * Verfuegung, wie Erstellung von Monstern, Schluesseln und Schreinen,
 * Methoden zum setzen und entfernen von Tueren und Monstern und
 * einen Algorithmus der die ein Netzwerk von Raeumen auf Zusammenhaengigkeit testet.
 */

package de.jdungeon.dungeon.generate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Path;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.DungeonUtils;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.monster.Ghul;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.Ogre;
import de.jdungeon.figure.monster.Orc;
import de.jdungeon.figure.monster.Skeleton;
import de.jdungeon.figure.monster.Spider;
import de.jdungeon.figure.monster.Wolf;
import de.jdungeon.game.loop.DungeonGameLoop;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemPool;
import de.jdungeon.item.Key;
import de.jdungeon.item.quest.Rune;
import de.jdungeon.util.Arith;

public abstract class AbstractDungeonFiller implements DungeonFiller {

	protected final Dungeon d;

	private final Rune[] runen;

	private final int[][] map;

	private final List<Key> keyList;

	private List<Room> allocatedRooms = new ArrayList<Room>();

	public AbstractDungeonFiller(Dungeon d, Rune[] runen) {
		this.d = d;
		this.runen = runen;
		this.map = getMap();
		keyList = Key.generateKeylist();

	}

	@Override
	public Room getUnallocatedRimRoom(boolean cornerAllowed) {
		List<Room> candidates = new ArrayList<>();
		JDPoint size = d.getSize();
		int firstCol = 0;
		int lastCol = size.getX() - 1;
		int firstRow = 0;
		int lastRow = size.getY() - 1;
		for (int x = 0; x < size.getX(); x++) {
			for (int y = 0; y < size.getY(); y++) {
				if(x == firstCol || x == lastCol || y == firstRow || y == lastRow ) {
					if(!cornerAllowed && ((x == firstCol && (y == 0 || y == lastRow))|| (x == lastCol && (y == 0 || y == lastRow)))){
						continue;
					}
					candidates.add(d.getRoomNr(x, y));
				}
			}
		}

		return candidates.get((int)(Math.random()* candidates.size()));
	}

	@Override
	public void addAllocatedRoom(Room room) {
		this.allocatedRooms.add(room);
	}

	@Override
	public Key getNextKey() {
		if (!keyList.isEmpty()) {
			int i = (int) (Math.random() * keyList.size());
			Key k = keyList.get(i);
			keyList.remove(i);
			return k;
		}
		else {
			return null;
		}
	}

	@Override
	public void setToWallUnreachableRoom(JDPoint heroEntryPoint) {
		for (int x = 0; x < d.getSize().getX(); x++) {
			for (int y = 0; y < d.getSize().getY(); y++) {
				Path path = DungeonUtils.findShortestPath(heroEntryPoint, new JDPoint(x, y), DungeonVisibilityMap.getAllVisMap(d), true);
				if(path == null) {
					d.getRoom(x, y).setIsWall(true);
				}
			}
		}
	}

	protected abstract int[][] getMap();

	@Override
	public Dungeon getDungeon() {
		return d;
	}


	public abstract void fillDungeon() throws DungeonGenerationFailedException;

	@Override
	public Monster getBigMonster(int value) {
		int k = (int) (Math.random() * 3);
		if (k == 0) {
			return new Spider(value);
		}
		if (k == 1) {
			return new Ogre(value);
		}
		else {
			return new Ghul(value);
		}

	}

	public static Monster getBigMonster(int value, DungeonGameLoop game) {
		int k = (int) (Math.random() * 3);
		if (k == 0) {
			return new Spider(value);
		}
		if (k == 1) {
			return new Ogre(value);
		}
		else {
			return new Ghul(value);
		}

	}

	@Override
	public Monster getSmallMonster(int value) {
		int k = (int) (Math.random() * 3);
		if (k == 0) {
			return new Wolf(value);
		}
		if (k == 1) {
			return new Orc(value);
		}
		else {
			return new Skeleton(value);
		}
	}

	/**
	 * Ruestet ein Monster eventuell mit zuf�lligen Gegenst�nden aus. Die
	 * wahrscheinlchkeit, dass ein Monster etwas bekommt ist 1/rate. rate = 1 =>
	 * monster bekommt immer was rate = 3 => jedes dritte bekommt was
	 *
	 * @param m
	 * @param rate
	 */
	@Override
	public void equipMonster(Monster m, int rate) {
		// itemwert = Wurzel(monsterwert) mal einhalb

		if (Math.random() * rate < 1) { // jedes dritte Monster hat was
			int value = (int) (0.32 * Math.sqrt(m.getWorth()));
			// Wert des Monsters hoch 3/8
			// int value = (int) (1
			// *(Math.sqrt(Math.sqrt(Math.sqrt(m.getWorth()*m.getWorth()*m.getWorth())))));

			double scattered = Arith.gauss(value, ((double) value) / 5);
			double quotient = scattered / value;
			Item i = ItemPool.getRandomItem((int) scattered, quotient);
			if ((i != null) && (scattered > 0)) {
				// //System.out.println(i.toString());
				m.takeItem(i);
			}
		}
		if (Math.random() * 3 < 1) {

			int value = (int) (0.2 * Math.sqrt(m.getWorth()));
			Item i = new DustItem(value);
			if (i != null) {
				// //System.out.println(i.toString());
				m.takeItem(i);
			}
		}
		if (Math.random() * 3 < 0.7) {
			int value = (int) (0.3 * Math.sqrt(m.getWorth()));
			Item i = new HealPotion(value);
			if (i != null) {
				// //System.out.println(i.toString());
				m.takeItem(i);
			}
		}
	}

	public static void equipAMonster(Monster m) {
		// itemwert = Wurzel(monsterwert) mal einhalb
		if (Math.random() * 3 < 1) { // jedes zweite Monster hat was
			int value = (int) (0.35 * Math.sqrt(m.getWorth()));
			double scattered = Arith.gauss(value, ((double) value) / 5);
			double quotient = scattered / value;
			Item i = ItemPool.getRandomItem((int) scattered, quotient);
			if ((i != null) && (scattered > 0)) {
				m.takeItem(i);
			}
		}
		if (Math.random() * 3 < 1) {
			int value = (int) (0.2 * Math.sqrt(m.getWorth()));
			Item i = new DustItem(value);
			m.takeItem(i);
		}
		if (Math.random() * 2 < 1) {
			int value = (int) (0.3 * Math.sqrt(m.getWorth()));
			Item i = new HealPotion(value);
			m.takeItem(i);
		}
	}

	protected JDPoint getPoint(int sector) {
		List<JDPoint> points = new LinkedList<JDPoint>();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				int sec = map[i][j] / 10;
				if (sec == sector) {
					points.add(d.getPoint(j, i));
				}
			}
		}
		int k = (int) (Math.random() * points.size());
		JDPoint p = points.get(k);

		if ((p.getX() == d.getHeroPosition().getX())
				&& (p.getY() == d.getHeroPosition().getY())) {
			p = getPoint(sector);
		}
		return p;
	}

	protected Monster getMonsterIn(int sector, int cnt) {
		if (cnt > 100) {
			// System.out.println(
			// "Kein Monster in Sektor "
			// + sector
			// + " in 1000000 Versuchen gefunden!");
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
		if ((r == null) || (r.getRoomFigures().isEmpty())) {
			return getMonsterIn(sector, cnt);
		}
		else {
			return (Monster) r.getRoomFigures().get(0);
		}

	}

	public Rune[] getRunes() {
		return runen;
	}

}
