package level.stageone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dungeon.Chest;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DeadEndPath;
import dungeon.generate.DistanceAtLeastConstraint;
import dungeon.generate.DungeonFiller;
import dungeon.quest.ReversibleRoomQuest;
import dungeon.quest.RoomQuestWall;
import dungeon.util.RouteInstruction;
import figure.monster.Wolf;
import item.HealPotion;
import item.Item;
import item.Key;
import item.VisibilityCheatBall;
import item.paper.ScrollMagic;
import level.AbstractDungeonFactory;
import level.generation.SimpleDungeonFiller;
import shrine.Corpse;
import shrine.HealthFountain;
import shrine.LevelExit;
import shrine.RevealMapShrine;
import shrine.Statue;
import spell.KeyLocator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class StartLevel extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 5;
	public static final int DUNGEON_SIZE_X = 5;
	private static final int FLOOR_INDEX_EXIT = 5;

	@Override
	public Dungeon createDungeon() {
		return createStartDungeon();
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return new JDPoint(2, 4);
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
		return 75;
	}

	private Dungeon createStartDungeon() {

		Dungeon dungeon = null;

		List<Key> allKeys = Key.generateKeylist();
		Key exitKey = allKeys.get(0);


		// set exit including doors, keys and guards
		int limit = 20;
		int counter = 0;
		boolean accomplished = false;
		while (!accomplished && counter < limit) {
			counter++;

			dungeon = new Dungeon(DUNGEON_SIZE_X, DUNGEON_SIZE_Y);
			createAllDoors(dungeon);
			DungeonFiller filler = new SimpleDungeonFiller(dungeon, new ArrayList<Key>());


			JDPoint entryPoint = this.getHeroEntryPoint();
			Room entryRoom = dungeon.getRoom(this.getHeroEntryPoint());
			filler.addAllocatedRoom(entryRoom);

			Room statueRoom = filler.getUnallocatedRandomRoom();
			statueRoom.setShrine(new Statue());
			filler.addAllocatedRoom(statueRoom);

			Room exitRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 3));;
			if(exitRoom == null) continue;
			JDPoint exitPoint = exitRoom.getPoint();
			filler.addAllocatedRoom(exitRoom);
			exitRoom.setShrine(new LevelExit());
			exitRoom.removeAllDoors();
			entryRoom.setShrine(new RevealMapShrine(exitRoom));

			exitRoom.setDoor(new Door(exitRoom, RouteInstruction.Direction.South, exitKey), RouteInstruction.Direction.South, true);

			Room keyRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(exitPoint, 3), new DistanceAtLeastConstraint(entryPoint, 3));
			if(keyRoom == null) continue;

			Chest keyChest = new Chest(exitKey);
			keyRoom.setChest(keyChest);
			filler.addAllocatedRoom(keyRoom);

			Room wolfRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 2));
			if(wolfRoom == null) continue;
			wolfRoom.figureEnters(new Wolf(900), RouteInstruction.Direction.North.getValue());

			List<Item> itemsL = new LinkedList<>();
			itemsL.add(new ScrollMagic(new KeyLocator(1, 0, 0, 0, 0, 0)));
			Chest entryChest = new Chest(itemsL);
			entryChest.takeItem(new HealPotion(10));
			entryRoom.setChest(entryChest);


			// remove some doors
			filler.removeDoors(1, entryPoint);

			// some black areas
			List<ReversibleRoomQuest> roomQuests = new ArrayList<>();
			// configure RoomQuests to be inserted
			roomQuests.add(new RoomQuestWall(filler, 1 ,2));
			roomQuests.add(new RoomQuestWall(filler, 1 ,1));
			roomQuests.add(new RoomQuestWall(filler, 1 ,1));
			roomQuests.add(new RoomQuestWall(filler, 1 ,1));
			setupRoomQuests(dungeon, filler, entryRoom, entryPoint, roomQuests);


			// set healing fountain
			filler.getUnallocatedRandomRoom().setShrine(new HealthFountain(10, 1));

			// remove some more doors
			filler.removeDoors(3, entryPoint);

			// dead corpse
			Collection<DeadEndPath> deadEnds = filler.getDeadEndsUnallocated();
			DeadEndPath longestDeadEndPath = DeadEndPath.getLongestDeadEndPath(deadEnds);
			if(longestDeadEndPath == null) {
				continue;
			}
			Room endRoom = longestDeadEndPath.getEndRoom();
			filler.addAllocatedRoom(endRoom);
			List<Item> list = new ArrayList<>();
			list.add(new HealPotion(15));
			endRoom.setShrine(new Corpse(list, endRoom, 1));


			entryRoom.addItem(new VisibilityCheatBall());

			accomplished = true;
		}


		return dungeon;
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
