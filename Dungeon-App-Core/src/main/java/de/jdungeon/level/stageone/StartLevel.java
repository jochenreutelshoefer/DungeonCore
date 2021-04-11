package de.jdungeon.level.stageone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DeadEndPath;
import de.jdungeon.dungeon.generate.DistanceAtLeastConstraint;
import de.jdungeon.dungeon.quest.ReversibleRoomQuest;
import de.jdungeon.dungeon.quest.RoomQuestWall;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.monster.Wolf;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.Item;
import de.jdungeon.item.Key;
import de.jdungeon.item.VisibilityCheatBall;
import de.jdungeon.item.paper.ScrollMagic;
import de.jdungeon.level.AbstractDungeonFactory;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.location.Corpse;
import de.jdungeon.location.HealthFountain;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.location.ScoutShrine;
import de.jdungeon.location.Statue;
import de.jdungeon.spell.KeyLocator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class StartLevel extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 5;
	public static final int DUNGEON_SIZE_X = 5;
	private static final int FLOOR_INDEX_EXIT = 5;
	private JDPoint entryPoint;

	@Override
	public Dungeon createDungeon() {
		return createStartDungeon();
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

		int limit = 20;
		int counter = 0;
		boolean accomplished = false;
		while (!accomplished && counter < limit) {
			counter++;

			dungeon = new Dungeon(DUNGEON_SIZE_X, DUNGEON_SIZE_Y);
			createAllDoors(dungeon);
			filler = new SimpleDungeonFiller(dungeon, new ArrayList<Key>());

			entryPoint = this.getHeroEntryPoint();
			Room entryRoom = dungeon.getRoom(entryPoint);
			filler.addAllocatedRoom(entryRoom);


			Room exitRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 3));
			if (exitRoom == null) continue;
			filler.addAllocatedRoom(exitRoom);
			exitRoom.setShrine(new LevelExit());
			exitRoom.removeAllDoors();
			entryRoom.setShrine(new RevealMapShrine(exitRoom));
			entryRoom.addItem(new VisibilityCheatBall());

			RouteInstruction.Direction exitNeighbour = filler.getUnallocatedRandomNeighbour(exitRoom);
			if(exitNeighbour == null) continue;
			exitRoom.setDoor( new Door(exitRoom, exitNeighbour, exitKey), exitNeighbour, true);


			Room wolfRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 2));
			if (wolfRoom == null) continue;
			wolfRoom.figureEnters(new Wolf(900), RouteInstruction.Direction.North.getValue(), -1);
			//wolfRoom.figureEnters(new Spider(900), RouteInstruction.Direction.West.getValue());
			//wolfRoom.figureEnters(new Ghul(900), RouteInstruction.Direction.West.getValue());
			filler.addAllocatedRoom(wolfRoom);

			Room statueRoom = filler.getUnallocatedRandomRoom();
			statueRoom.setShrine(new Statue());
			filler.addAllocatedRoom(statueRoom);




			List<Item> itemsL = new ArrayList<>();
			itemsL.add(new ScrollMagic(new KeyLocator(1, 0, 0, 0, 0, 0)));
			Chest entryChest = new Chest(itemsL);
			entryChest.takeItem(exitKey); // for reachability checking only, remove and set later
			entryRoom.setChest(entryChest);

			// remove some doors
			//filler.removeDoors(1, entryPoint);

			// some black areas
			List<ReversibleRoomQuest> roomQuests = new ArrayList<>();
			// configure RoomQuests to be inserted
			roomQuests.add(new RoomQuestWall(filler, 1, 2));
			roomQuests.add(new RoomQuestWall(filler, 1, 1));
			roomQuests.add(new RoomQuestWall(filler, 1, 1));
			roomQuests.add(new RoomQuestWall(filler, 1, 1));
			setupRoomQuests(dungeon, filler, entryRoom, entryPoint, roomQuests);

			// remove some more doors
			filler.removeDoors(2, entryPoint);

			Collection<DeadEndPath> deadEnds = filler.getDeadEndsUnallocated();

			// key room
			DeadEndPath shortestDeadEndPath = DeadEndPath.getDeadEndPathFarestTo(deadEnds, entryPoint);
			if (shortestDeadEndPath == null) continue;
			deadEnds.remove(shortestDeadEndPath);
			Room keyRoom = shortestDeadEndPath.getEndRoom();

			// this is just the trick to allow exit key setting after dungeon thinning out
			entryChest.removeItem(exitKey);

			Chest keyChest = new Chest(exitKey);
			keyRoom.setChest(keyChest);
			filler.addAllocatedRoom(keyRoom);

			// dead corpse
			Room corpseRoom;
			if(!deadEnds.isEmpty()) {
				DeadEndPath longestDeadEndPath = DeadEndPath.getLongestDeadEndPath(deadEnds);
				deadEnds.remove(longestDeadEndPath);
				if (longestDeadEndPath == null) {
					continue;
				}
				corpseRoom = longestDeadEndPath.getEndRoom();
			} else {
				corpseRoom = filler.getUnallocatedRandomRoom();
			}
			filler.addAllocatedRoom(corpseRoom);
			List<Item> list = new ArrayList<>();
			list.add(new HealPotion(15));
			assert corpseRoom != null;
			corpseRoom.setShrine(new Corpse(list, corpseRoom, 1));

			// set healing fountain
			Room fountainRoom = null;
			if(!deadEnds.isEmpty()) {
				DeadEndPath fountainDeadEndPath = DeadEndPath.getLongestDeadEndPath(deadEnds);
				fountainRoom = fountainDeadEndPath.getEndRoom();
			} else {
				fountainRoom = filler.getUnallocatedRandomRoom();
			}
			fountainRoom.setShrine(new HealthFountain(15, 0.1));
			filler.addAllocatedRoom(fountainRoom);

			// set scout shrine
			Room scoutShrineRoom = filler.getUnallocatedRoomNearCenter();
			scoutShrineRoom.setShrine(new ScoutShrine(scoutShrineRoom));
			filler.addAllocatedRoom(scoutShrineRoom);

			// for testing only
			//entryRoom.addItem(new VisibilityCheatBall());
			//entryRoom.addItem(new HealPotion(15));



			accomplished = true;
		}

		return dungeon;
	}

}
