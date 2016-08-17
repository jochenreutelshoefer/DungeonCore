/**
 * Abstrakte Oberklasse zum Fuellen eines Dungeons.
 * Sie stellt eine Reihe von Werkzeugen zum gestalten des Dungeons zur
 * Verfuegung, wie Erstellung von Monstern, Schluesseln und Schreinen,
 * Methoden zum setzen und entfernen von Tueren und Monstern und
 * einen Algorithmus der die ein Netzwerk von Raeumen auf Zusammenhaengigkeit testet.
 */

package dungeon.generate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import game.DungeonGame;
import item.DustItem;
import item.HealPotion;
import item.Item;
import item.ItemPool;
import item.Key;
import item.quest.Rune;
import shrine.Statue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Arith;

public abstract class AbstractDungeonFiller implements DungeonFiller {

	protected final Dungeon d;

	private final Rune[] runen;

	private final int[][] map;

	private final List<Key> keyList;

	List<Room> allocatedRooms = new ArrayList<Room>();

	public AbstractDungeonFiller(Dungeon d, Rune[] runen) {
		this.d = d;
		this.runen = runen;
		this.map = getMap();
		keyList = Key.generateKeylist();

	}

	public void addAllocatedRoom(Room room) {
		this.allocatedRooms.add(room);
	}

	public Key getNextKey() {
		if (keyList.size() > 0) {
			int i = (int) (Math.random() * keyList.size());
			Key k = keyList.get(i);
			keyList.remove(i);
			return k;
		}
		else {
			return null;
		}
	}

	protected abstract int[][] getMap();

	/**
	 *
	 * @uml.property name="d"
	 */
	public Dungeon getDungeon() {
		return d;
	}

	protected int validateNetCnt(Room startRoom, boolean print) {
		List<Room> hash = new LinkedList<Room>();
		Room first = startRoom;
		hash.add(first);
		int k = 0;
		while (k < hash.size()) {

			Room r = hash.get(k);
			if (r.getShrine() != null && (r.getShrine() instanceof Statue)) {
				// System.out.println("ValidateCnt: StatueRoom wird nicht expandiert!");

			}
			else {
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
		return l;

	}

	public abstract void fillDungeon() throws DungeonGenerationFailedException;

	// Gibt zuf�llig einen B�ren, Ghul oder Oger zur�ck
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

	public static Monster getBigMonster(int value, DungeonGame game) {
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

	// gibt zuf�llig einen wolf, orc oder skellet zur�ck
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
			if (i != null) {
				m.takeItem(i);
			}
		}
		if (Math.random() * 2 < 1) {
			int value = (int) (0.3 * Math.sqrt(m.getWorth()));
			Item i = new HealPotion(value);
			if (i != null) {
				m.takeItem(i);
			}
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
		if ((r == null) || (r.getRoomFigures().size() == 0)) {
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
