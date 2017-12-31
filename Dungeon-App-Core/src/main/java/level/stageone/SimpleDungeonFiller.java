package level.stageone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DungeonFillUtils;
import dungeon.generate.DungeonFiller;
import dungeon.generate.ReachabilityChecker;
import dungeon.generate.RectArea;
import dungeon.generate.RoomPositionConstraint;
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

	private final Dungeon dungeon;
	private final List<Key> allKeys;
	private int keyIndex = 0;
	private final Collection<Item> itemsToBeDistributed = new HashSet<Item>();

	private final List<Room> allocatedRooms = new ArrayList<Room>();

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

			double scattered = Arith.gauss(value, ((double) value) / 5);
			double quotient = scattered / value;
			Item i = ItemPool.getRandomItem((int) scattered, quotient);
			if ((i != null) && (scattered > 0)) {
				m.takeItem(i);
			}
		}
		if (Math.random() * 3 < 1) {
			int value = (int) (0.2 * Math.sqrt(m.getWorth()));
			m.takeItem(new DustItem(value));
		}
		if (Math.random() * 3 < 0.7) {
			int value = (int) (0.3 * Math.sqrt(m.getWorth()));
			m.takeItem(new HealPotion(value));
		}
	}

	@Override
	public boolean isAllocated(Room room) {
		return allocatedRooms.contains(room);
	}

	@Override
	public Room getUnallocatedRandomRoom(RoomPositionConstraint... constraints) {
		RectArea unallocatedSpaceRandom = getUnallocatedSpaceRandom(1, 1,  constraints);
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
	public RectArea getUnallocatedSpaceRandom(int sizeX, int sizeY, RoomPositionConstraint... constraints) {
		int numberOfTrials = 100;
		int trial = 0;
		JDPoint result = null;
		while (trial < numberOfTrials && result == null) {

			JDPoint pointToCheck = getRandomPoint();
			boolean isOk = true;
			for (int x = pointToCheck.getX(); x < pointToCheck.getX() + sizeX; x++) {
				for (int y = pointToCheck.getY(); y < pointToCheck.getY() + sizeY; y++) {
					Room room = getDungeon().getRoom(new JDPoint(x, y));
					if (room == null || allocatedRooms.contains(room)  || !constraintsOk(room, constraints)) {
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

	private boolean constraintsOk(Room room, RoomPositionConstraint[] constraints) {
		for (RoomPositionConstraint constraint : constraints) {
			if(!constraint.isValid(room)) {
				return false;
			}
		}
		return true;
	}

	public JDPoint getRandomPoint() {
		return new JDPoint(Math.random() * getDungeon().getSize().getX(),
				Math.random() * getDungeon().getSize().getY());

	}

	@Override
	public RectArea getValidArea(Room entryRoom, int sizeX, int sizeY) {
		int maxTrials = 100;
		int trial = 0;
		RectArea unallocatedSpace = getUnallocatedSpaceRandom(sizeX, sizeY);

		// we need to exclude the rooms of the tested area and those which are already declared as unreachable (isWall)
		Collection<Room> ignoredRooms = new HashSet<>();
		ignoredRooms.addAll(unallocatedSpace.getRooms());
		Collection<Room> wallRooms = dungeon.getWallRooms();
		ignoredRooms.addAll(wallRooms);

		boolean valid = DungeonFillUtils.validateNet(dungeon.getAllRooms(), entryRoom, ignoredRooms );
		while(!valid) {
			unallocatedSpace = getUnallocatedSpaceRandom(sizeX, sizeY);
			ignoredRooms = new HashSet<>();
			ignoredRooms.addAll(unallocatedSpace.getRooms());
			ignoredRooms.addAll(wallRooms);
			valid = DungeonFillUtils.validateNet(dungeon.getAllRooms(), entryRoom, ignoredRooms);
			if(trial > maxTrials) {
				break;
			}
			trial++;
		}
		return unallocatedSpace;
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
		if(itemsToBeDistributed.isEmpty()) return null;
		Iterator<Item> iterator = itemsToBeDistributed.iterator();
		Item item = iterator.next();
		iterator.remove();
		return item;
	}

	@Override
	public int removeDoors(int number, JDPoint entryPoint) {
		ReachabilityChecker checker = new ReachabilityChecker(dungeon, entryPoint);
		int counterTries = 0;
		int counterDoors = 0;
		int max = 200;
		while(counterDoors < number) {
			counterTries++;
			JDPoint randomPoint = getRandomPoint();
			Room room = dungeon.getRoom(randomPoint);
			if(room == null) continue;
			List<Door> doors = getDoorList(room);
			if(doors.isEmpty()) continue;
			int doorIndex = (int) (Math.random() * doors.size());
			Door door = doors.get(doorIndex);
			int direction = door.getDir(room.getPoint());
			room.removeDoor(door, true);
			boolean allRoomsStillReachable = checker.check();
			if (allRoomsStillReachable) {
				counterDoors ++;
			}
			else {
				// we re-insert the door as it is required
				room.addDoor(door, direction, true);
			}

			if(counterTries > max) {
				break;
			}

		}
		return counterDoors;
	}

	private List<Door> getDoorList(Room room) {
		List<Door> result = new ArrayList<>();
		for(int i = 0; i < room.getDoors().length; i++) {
			Door door = room.getDoors()[i];
			if(door != null && !door.hasLock()) {
				result.add(door);
			}
		}
		return result;
	}

}
