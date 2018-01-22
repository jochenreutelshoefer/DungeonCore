package level.stageone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ai.GuardPositionBehaviour;
import ai.PreGuardBehaviour;
import dungeon.Chest;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.Room;
import dungeon.generate.DungeonFiller;
import dungeon.quest.ReversibleRoomQuest;
import dungeon.quest.RoomQuest1x1;
import dungeon.quest.RoomQuest2x2;
import dungeon.util.RouteInstruction;
import figure.monster.Monster;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Wolf;
import item.Item;
import item.ItemPool;
import item.Key;
import item.VisibilityCheatBall;
import item.map.AncientMapFragmentUtils;
import item.paper.ScrollMagic;
import level.AbstractDungeonFactory;
//import org.apache.log4j.Logger;
import level.generation.SimpleDungeonFiller;
import shrine.HealthFountain;
import shrine.LevelExit;
import shrine.RevealMapShrine;
import shrine.Statue;
import spell.conjuration.FirConjuration;
import spell.conjuration.LionessConjuration;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.04.16.
 */
public class StartLevelOLD extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 6;
	public static final int DUNGEON_SIZE_X = 6;
	private static final int FLOOR_INDEX_EXIT = 5;

	@Override
	public Dungeon createDungeon() {
		return createStartDungeon();
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return new JDPoint(2, 5);
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Level 1";
	}

	@Override
	public String getDescription() {
		return "Einstiegslevel";
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 150;
	}

	private Dungeon createStartDungeon() {

		Dungeon dungeon = null;

		List<Key> allKeys = Key.generateKeylist();
		Key exitKey = allKeys.get(0);
		List<Key> remainingKeys = new ArrayList<Key>();

		DungeonFiller filler = null;

		Room entryRoom = null;
		JDPoint entryPoint = this.getHeroEntryPoint();

		// set exit including doors, keys and guards
		int limit = 100;
		int counter = 0;
		boolean accomplished = false;
		while (!accomplished && counter < limit) {
			counter++;

			// TODO: fix intersection with start room
			dungeon = new Dungeon(DUNGEON_SIZE_X, DUNGEON_SIZE_Y);
			createAllDoors(dungeon);
			filler = new SimpleDungeonFiller(dungeon, remainingKeys);
			filler.itemToDistribute(exitKey);

			JDPoint exitPoint = getExitPoint();
			Room exitRoom = dungeon.getRoom(exitPoint);
			exitRoom.removeAllDoors();
			exitRoom.setShrine(new LevelExit());
			filler.addAllocatedRoom(exitRoom);

			// show exit on a map in entry room
			entryRoom = dungeon.getRoom(this.getHeroEntryPoint());
			filler.addAllocatedRoom(entryRoom);
			entryRoom.setShrine(new RevealMapShrine(exitRoom));




			// CHEST TEST
			List<Item> itemsL = new LinkedList<Item>();
			Item it1 = ItemPool.getRandomItem(25, 1);
			Item it2 = ItemPool.getRandomItem(30, 1.5);
			if (it1 != null) {
				itemsL.add(it1);
			}
			if (it2 != null) {
				itemsL.add(it2);
			}
			Chest ch = new Chest(itemsL);
			entryRoom.setChest(ch);

			remainingKeys.addAll(allKeys);
			remainingKeys.remove(exitKey);
			Set<RouteInstruction.Direction> doorDirections = getExitDirections();

			boolean setDoorsAndKey = setDoorsWithKeys(exitRoom, exitKey, doorDirections, filler);
			if (!setDoorsAndKey) continue;

			boolean setGuards = setGuards(exitRoom, doorDirections);
			if (!setGuards) continue;

			accomplished = true;
		}

		// prevent removal of doors for statue room (make RQ?)
		// todo: statue should not be with guards
		filler.getUnallocatedRandomRoom().setShrine(new Statue());
		// situate HF more near entrance/exit
		filler.getUnallocatedRandomRoom().setShrine(new HealthFountain(10, 1));

		List<Item> items = new ArrayList<Item>();
		for (int i = 0; i < 5; i++) {
			items.add(ItemPool.getRandomItem((int) (20 + (Math.random() * 50)), Math.random() * 2));
		}

		// flush remaining items
		List<Item> itemList = new ArrayList<Item>();
		Item item = filler.getItemForDistribution();
		while (item != null) {
			itemList.add(item);
			item = filler.getItemForDistribution();
		}
		Chest chestWithRemainderItems = new Chest(itemList);
		Room chestRoom = filler.getUnallocatedRandomRoom();
		chestRoom.setChest(chestWithRemainderItems);

		// for testing only
		entryRoom.addItem(new VisibilityCheatBall());
		entryRoom.addItem(new ScrollMagic(new LionessConjuration(1)));
		entryRoom.addItem(new ScrollMagic(new FirConjuration(1)));
		// TODO: remove
		entryRoom.addItem(AncientMapFragmentUtils.createMap(dungeon, entryPoint, 7));

		List<ReversibleRoomQuest> roomQuests = new ArrayList<>();

		// configure RoomQuests to be inserted
		roomQuests.add(new RoomQuest2x2(filler, new ArrayList<Item>(), new HealthFountain(20, 1)));
		roomQuests.add(new RoomQuest1x1(filler, true, null));
		roomQuests.add(new RoomQuest1x1(filler, true, null));
		roomQuests.add(new RoomQuest1x1(filler, true, null));
		roomQuests.add(new RoomQuest1x1(filler, true, null));

		setupRoomQuests(dungeon, filler, entryRoom, entryPoint, roomQuests);


		return dungeon;
	}



	private boolean setGuards(Room exitRoom, Set<RouteInstruction.Direction> doorDirections) {
		exitRoom.setFloorIndex(FLOOR_INDEX_EXIT);
		List<Monster> gateKeepers = new ArrayList<Monster>();

		gateKeepers.add(new Orc(900));
		gateKeepers.add(new Wolf(900));
		gateKeepers.add(new Skeleton(900));


		Iterator<RouteInstruction.Direction> directionsIterator = doorDirections.iterator();
		for (int i = 0; i < 3; i++) {
			RouteInstruction.Direction dir = directionsIterator.next();
			Door door = exitRoom.getDoor(dir);
			Position positionToGuard = door.getPositionAtDoor(exitRoom, true);
			Monster monster = gateKeepers.get(i);
			// todo: use setControl!
			monster.setAI(new GuardPositionBehaviour(positionToGuard));
			Room guardRoom = positionToGuard.getRoom();
			if (guardRoom == null) return false;
			guardRoom.figureEnters(monster, RouteInstruction.turnOpp(dir).getValue());
			guardRoom.setFloorIndex(FLOOR_INDEX_EXIT);
		}
		return true;
	}

	private boolean setDoorsWithKeys(Room exitRoom, Key exitKey, Set<RouteInstruction.Direction> doorDirections, DungeonFiller filler) {

		for (RouteInstruction.Direction doorDirection : doorDirections) {
			Room neighbourRoom = exitRoom.getNeighbourRoom(doorDirection);
			filler.addAllocatedRoom(neighbourRoom);
			neighbourRoom.removeAllDoors();
			int preGuardDoorDirIndex = random(4) + 1;
			if (RouteInstruction.Direction.opposite(doorDirection).getValue() == preGuardDoorDirIndex) {
				preGuardDoorDirIndex = doorDirection.getValue();
			}
			RouteInstruction.Direction preGuardDirectionDoor = RouteInstruction.Direction.fromInteger(preGuardDoorDirIndex);
			Door preGuardDoor = new Door(neighbourRoom, preGuardDirectionDoor);
			neighbourRoom.setDoor(preGuardDoor, preGuardDirectionDoor, true);
			Room preGuardRoom = preGuardDoor.getOtherRoom(neighbourRoom);
			if (preGuardRoom == null) return false;
			filler.addAllocatedRoom(preGuardRoom);
			preGuardRoom.setFloorIndex(FLOOR_INDEX_EXIT);
			Monster preGuardMonster = filler.getSmallMonster(800);
			// TODO: use setControl!
			preGuardMonster.setAI(new PreGuardBehaviour(preGuardRoom));

			preGuardRoom.figureEnters(preGuardMonster, RouteInstruction.direction(random(4) + 1).getValue());
			exitRoom.setDoor(new Door(exitRoom, doorDirection, exitKey), doorDirection, true);
		}
		return true;
	}

	private Set<RouteInstruction.Direction> getExitDirections() {
		Set<RouteInstruction.Direction> doorDirections = new HashSet<RouteInstruction.Direction>();
		while (doorDirections.size() < 3) {
			doorDirections.add(RouteInstruction.Direction.fromInteger(random(4) + 1));
		}
		return doorDirections;
	}

	private JDPoint getExitPoint() {
		int exitX = random(DUNGEON_SIZE_X / 2) + 2;
		int exitY = random(DUNGEON_SIZE_Y / 2) + 2;
		return new JDPoint(exitX, exitY);
	}

	private int random(int max) {
		return (int) (Math.random() * max);
	}

}
