/**
 * Realisierung eines DungeonFillers - Sektorenweise
 * Diese Implementierung generiert den kompletten Dungeon indem am Startpunkt
 * ein Sektor 1 generiert und ein Sektor 2 dahinter geschaltet wird.
 *
 */
package dungeon.generate;

import dungeon.JDPoint;
import figure.Figure;
import figure.monster.Monster;
import game.DungeonGame;
import game.JDEnv;
import item.Item;
import item.quest.Rune;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dungeon.Dungeon;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SectorDungeonFiller1 extends AbstractDungeonFiller {

	public boolean succes = true;
	private final DungeonGame game;

	private static final String[] word = { "JAVA", "CLUB", "BEAR" };

	public SectorDungeonFiller1(Dungeon d, int value, DungeonGame game,
			int level) {
		super(d, runeCreater(level));
		this.game = game;
	}

	private static Rune[] runeCreater(int k) {
		Rune[] runen = new Rune[word[k].length()];
		for (int i = 0; i < word[k].length(); i++) {
			runen[i] = new Rune(word[k].charAt(i));
		}
		return runen;
	}

	public static int getValueForDungeon(int level) {
		if (level == 1) {
			return 40000;
		} else if (level == 2) {
			return 200000;
		} else if (level == 3) {
			return 1000000;
		} else {

			return -1;
		}
	}

	/**
	 * @see AbstractDungeonFiller#getMap()
	 */
	@Override
	protected int[][] getMap() {
		return null;
	}

	/**
	 * @see AbstractDungeonFiller#fillDungeon()
	 */
	@Override
	public void fillDungeon() throws DungeonGenerationFailedException {

		Sector s1 = new Sector1(d, d.getPoint(18, 39), 1, 800, 24, game, this);

		if (!JDEnv.isBeginnerGame()) {
			Sector s2 = new Sector2(d, (d.getRoomAt(s1.getConnectionRoom(),
							RouteInstruction.direction(RouteInstruction.NORTH))
							.getNumber()), 2, 1400, 40, game,
					this);
		}

	}

	@Override
	protected Monster getMonsterIn(int sector, int nope) {
		for (int i = 0; i < 1000; i++) {
			Room r = d.getRoom(d.getRandomPoint());
			// Hier muss was rein, damit nicht Monster innen den Schl�ssel
			// kriegt
			if ((r.getSec() != null) && (r.getSec().number == sector)) {
				List<Figure> monsters = r.getRoomFigures();
				if (!monsters.isEmpty()) {
					return (Monster) monsters
							.get((int) (Math.random() * monsters.size()));
				}
			}
		}
		return null;

	}

	@Override
	public boolean isAllocated(Room room) {
		throw new NotImplementedException();
	}

	@Override
	public Room getUnallocatedRandomRoom(RoomPositionConstraint... constraints) {
		throw new NotImplementedException();
	}

	@Override
	public Room getUnallocatedRandomRoom(JDPoint near) {
		throw new NotImplementedException();
	}

	@Override
	public void addAllocatedRooms(Collection<Room> rooms) {

	}

	@Override
	public Collection<DeadEndPath> getDeadEndsUnallocated() {
		return Collections.emptyList();
	}

	@Override
	public RectArea getUnallocatedSpace(int sizeX, int sizeY, JDPoint area) {
		return null;
	}

	@Override
	public RectArea getUnallocatedSpaceRandom(int sizeX, int sizeY, RoomPositionConstraint... constraints) {
		return null;
	}

	@Override
	public RectArea getValidArea(Room entryRoom, int sizeX, int sizeY) {
		return null;
	}

	@Override
	public void itemToDistribute(Item item) {

	}

	@Override
	public void itemsToDistribute(Collection<Item> items) {
		for (Item item : items) {
			itemToDistribute(item);
		}
	}

	@Override
	public Item getItemForDistribution() {
		return null;
	}

	@Override
	public int removeDoors(int number, JDPoint entryPoint) {
		throw new NotImplementedException();
	}
}
