package level.stageone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DungeonFillUtils;
import dungeon.generate.DungeonFiller;
import dungeon.generate.RectArea;
import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import item.DustItem;
import item.HealPotion;
import item.Item;
import item.ItemPool;
import item.Key;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.Arith;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 18.04.16.
 */
public class SimpleDungeonFiller implements DungeonFiller {

	private Dungeon dungeon;
	private List<Key> allKeys;
	private int keyIndex = 0;
	private Collection<Item> itemsToBeDistributed = new HashSet<Item>();

	private List<Room> allocatedRooms = new ArrayList<Room>();

	public SimpleDungeonFiller(Dungeon dungeon, List<Key> allKeys) {
		this.dungeon = dungeon;
		this.allKeys = allKeys;
	}



	@Override
	public Dungeon getDungeon() {
		return dungeon;
	}

	@Override
	public Monster getSmallMonster(int value) {
		int k = (int) (Math.random() * 3);
		if (k == 0)
			return new Wolf(value);
		if (k == 1)
			return new Orc(value);
		else
			return new Skeleton(value);
	}

	@Override
	public Monster getBigMonster(int value) {
		int k = (int) (Math.random() * 3);
		if (k == 0)
			return new Spider(value);
		if (k == 1)
			return new Ogre(value);
		else
			return new Ghul(value);
	}

	@Override
	public void equipMonster(Monster m, int rate) {
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

	@Override
	public boolean isAllocated(Room room) {
		return allocatedRooms.contains(room);
	}

	@Override
	public Room getUnallocatedRandomRoom() {
		RectArea unallocatedSpaceRandom = getUnallocatedSpaceRandom(1, 1);
		if(unallocatedSpaceRandom != null) {
			return dungeon.getRoom(unallocatedSpaceRandom.getPosition());
		}
		return null;
	}

	@Override
	public Room getUnallocatedRandomRoom(JDPoint near) {
		int count = 0;
		int limit = 100;
		int x = near.getX();
		int y = near.getY();
		while(count < limit) {
			int xNew = x - 3 + ((int)(Math.random() * 6));
			int yNew = y - 3 + ((int)(Math.random() * 6));
			Room foundRoom = dungeon.getRoom(new JDPoint(xNew, yNew));
			if(foundRoom != null) {
				if(!isAllocated(foundRoom)) {
					return foundRoom;
				}
			}
			count++;
		}
		return null;
	}

	@Override
	public void addAllocatedRoom(Room room) {
		this.allocatedRooms.add(room);
	}

	@Override
	public void addAllocatedRooms(Collection<Room> rooms) {
		this.allocatedRooms.addAll(rooms);

	}

	@Override
	public Key getNextKey() {
		if(allKeys.size() <= keyIndex) {
			return null;
		}
		Key key = allKeys.get(keyIndex);
		keyIndex++;
		return key;
	}


	@Override
	public RectArea getUnallocatedSpace(int sizeX, int sizeY, JDPoint area) {
		// TODO: implement
		throw new NotImplementedException();
	}

	@Override
	public RectArea getUnallocatedSpaceRandom(int sizeX, int sizeY) {
		int numberOfTrials = 100;
		int trial = 0;
		JDPoint result = null;
		while (trial < numberOfTrials && result == null) {

			JDPoint pointToCheck = getRandomPoint();
			boolean isOk = true;
			for (int x = pointToCheck.getX(); x < pointToCheck.getX() + sizeX; x++) {
				for (int y = pointToCheck.getY(); y < pointToCheck.getY() + sizeY; y++) {
					Room room = getDungeon().getRoom(new JDPoint(x, y));
					if (room == null || allocatedRooms.contains(room)) {
						isOk = false;
						break;
					}
				}
			}
			if(isOk) {
				result = pointToCheck;
			}
			trial++;
		}
		return new RectArea(result, sizeX, sizeY, getDungeon());
	}

	public JDPoint getRandomPoint() {
		return new JDPoint(Math.random() * getDungeon().getSize().getX(),
				Math.random() * getDungeon().getSize().getY());

	}

	public RectArea getValidArea(Room entryRoom, int sizeX, int sizeY) {
		int maxTrials = 100;
		int trial = 0;
		RectArea unallocatedSpace1 = getUnallocatedSpaceRandom(sizeX, sizeY);
		boolean valid = DungeonFillUtils.validateNet(dungeon.getAllRooms(), entryRoom, unallocatedSpace1.getRooms());
		while(!valid) {
			unallocatedSpace1 = getUnallocatedSpaceRandom(sizeX, sizeY);
			valid = DungeonFillUtils.validateNet(dungeon.getAllRooms(), entryRoom, unallocatedSpace1.getRooms());
			if(trial > maxTrials) {
				break;
			}
			trial++;
		}
		return unallocatedSpace1;
	}

	@Override
	public void itemToDistribute(Item item) {
		itemsToBeDistributed.add(item);
	}

	@Override
	public void itemsToDistribute(Collection<Item> items) {
		for (Item item : items) {
			itemToDistribute(item);
		}
	}

	@Override
	public Item getItemForDistribution() {
		if(itemsToBeDistributed.size() == 0) return null;
		Iterator<Item> iterator = itemsToBeDistributed.iterator();
		Item item = iterator.next();
		iterator.remove();
		return item;
	}

}
