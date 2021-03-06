package de.jdungeon.level.stagethree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.jdungeon.ai.GuardPositionBehaviour;
import de.jdungeon.ai.PreGuardBehaviour;
import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.quest.ReversibleRoomQuest;
import de.jdungeon.dungeon.quest.RoomQuest1x1;
import de.jdungeon.dungeon.quest.RoomQuest2x2;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.Orc;
import de.jdungeon.figure.monster.Skeleton;
import de.jdungeon.figure.monster.Wolf;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemPool;
import de.jdungeon.item.Key;
import de.jdungeon.item.map.AncientMapFragment;
import de.jdungeon.item.map.AncientMapFragmentUtils;
import de.jdungeon.item.paper.ScrollMagic;
import de.jdungeon.level.AbstractDungeonFactory;
//import org.apache.log4j.Logger;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.location.HealthFountain;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.location.ScoutShrine;
import de.jdungeon.location.Statue;
import de.jdungeon.spell.conjuration.FirConjuration;
import de.jdungeon.spell.conjuration.LionessConjuration;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.04.16.
 */
public class StartLevelOLD extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 6;
	public static final int DUNGEON_SIZE_X = 6;
	private static final int FLOOR_INDEX_EXIT = 5;
	private Dungeon dungeon;

	@Override
	public void create() {
		createStartDungeon();
	}

	@Override
	public Dungeon getDungeon() {
		return dungeon;
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
		return "Todo: Name";
	}

	@Override
	public String getDescription() {
		return "Todo: Description";
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 400;
	}

	private void createStartDungeon() {

		dungeon = null;

		List<Key> allKeys = Key.generateKeylist();
		Key exitKey = allKeys.remove(0);
		List<Key> remainingKeys = new ArrayList<>(allKeys);

		SimpleDungeonFiller filler = null;

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
			exitRoom.setLocation(new LevelExit());
			filler.addAllocatedRoom(exitRoom);

			// show exit on a map in entry room
			entryRoom = dungeon.getRoom(this.getHeroEntryPoint());
			entryRoom.setLocation(new RevealMapShrine(exitRoom));
			filler.addAllocatedRoom(entryRoom);

			// CHEST TEST
			List<Item> itemsL = new ArrayList<Item>();
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


			Set<RouteInstruction.Direction> doorDirections = getExitDirections();

			boolean setDoorsAndKey = setDoorsWithKeys(exitRoom, exitKey, doorDirections, filler);
			if (!setDoorsAndKey) continue;

			boolean setGuards = setGuards(exitRoom, doorDirections);
			if (!setGuards) continue;

			accomplished = true;
		}

		// prevent removal of doors for statue room (make RQ?)
		// todo: statue should not be with guards
		final Room statueRoom = filler.getUnallocatedRandomRoom();
		statueRoom.setLocation(new Statue());
		filler.addAllocatedRoom(statueRoom);

		// situate HF more near entrance/exit
		final Room fountainRoom = filler.getUnallocatedRandomRoom();
		fountainRoom.setLocation(new HealthFountain(30, 1));
		filler.addAllocatedRoom(fountainRoom);

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
		//entryRoom.addItem(new VisibilityCheatBall());
		entryRoom.addItem(new ScrollMagic(new LionessConjuration(1)));
		entryRoom.addItem(new ScrollMagic(new FirConjuration(1)));
		// TODO: remove
		AncientMapFragment map = AncientMapFragmentUtils.createMap(dungeon, entryPoint, 7);
		if(map != null) {
			entryRoom.addItem(map);
		}

		List<ReversibleRoomQuest> roomQuests = new ArrayList<>();

		// configure RoomQuests to be inserted
		roomQuests.add(new RoomQuest2x2(filler, new ArrayList<Item>(), new HealthFountain(20, 1)));
		roomQuests.add(new RoomQuest1x1(filler, true, null));
		roomQuests.add(new RoomQuest1x1(filler, true, null));
		roomQuests.add(new RoomQuest1x1(filler, true, null));
		roomQuests.add(new RoomQuest1x1(filler, true, null));

		setupRoomQuests(dungeon, filler, entryRoom, entryPoint, roomQuests);



		// set scout shrine
		Room scoutShrineRoom = filler.getUnallocatedRoomNearCenter();
		scoutShrineRoom.setLocation(new ScoutShrine(scoutShrineRoom));
		filler.addAllocatedRoom(scoutShrineRoom);

	}



	private boolean setGuards(Room exitRoom, Set<RouteInstruction.Direction> doorDirections) {
		exitRoom.setFloorIndex(FLOOR_INDEX_EXIT);
		List<Monster> gateKeepers = new ArrayList<Monster>();

		gateKeepers.add(new Orc(15000));
		gateKeepers.add(new Wolf(15000));
		gateKeepers.add(new Skeleton(15000));


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
			guardRoom.figureEnters(monster, RouteInstruction.turnOpp(dir).getValue(), -1);
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
			Monster preGuardMonster = filler.getSmallMonster(10000);
			// TODO: use setControl!
			preGuardMonster.setAI(new PreGuardBehaviour(preGuardRoom));

			preGuardRoom.figureEnters(preGuardMonster, RouteInstruction.direction(random(4) + 1).getValue(), -1);
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
