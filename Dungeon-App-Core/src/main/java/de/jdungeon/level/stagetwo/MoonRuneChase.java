package de.jdungeon.level.stagetwo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DeadEndPath;
import de.jdungeon.dungeon.generate.DistanceAtLeastConstraint;
import de.jdungeon.dungeon.quest.ReversibleRoomQuest;
import de.jdungeon.dungeon.quest.RoomQuestWall;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Ogre;
import de.jdungeon.figure.monster.Orc;
import de.jdungeon.figure.monster.Wolf;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.Key;
import de.jdungeon.item.paper.ScrollMagic;
import de.jdungeon.item.quest.MoonRune;
import de.jdungeon.level.AbstractDungeonFactory;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.location.Corpse;
import de.jdungeon.location.HealthFountain;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.MoonRuneFinderShrine;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.location.ScoutShrine;
import de.jdungeon.location.Statue;
import de.jdungeon.spell.Steal;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class MoonRuneChase extends AbstractDungeonFactory {

	private Dungeon dungeon;

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Moon Rune Chase";
	}

	@Override
	public String getDescription() {
		return "Find the Moon Rune";
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 100;
	}

	@Override
	public void create() {

		List<Key> allKeys = Key.generateKeylist();
		Key exitKey = allKeys.get(0);

		int limit = 20;
		int counter = 0;
		boolean accomplished = false;
		while (!accomplished && counter < limit) {
			counter++;

			int dungeonSizeX = 6;
			int dungeonSizeY = 5;
			dungeon = new Dungeon(dungeonSizeX, dungeonSizeY);
			createAllDoors(dungeon);
			filler = new SimpleDungeonFiller(dungeon, new ArrayList<Key>());

			entryPoint = this.getHeroEntryPoint();
			Room entryRoom = dungeon.getRoom(entryPoint);
			filler.addAllocatedRoom(entryRoom);

			MoonRune rune = new MoonRune();
			MoonRuneFinderShrine runeFinder = new MoonRuneFinderShrine(rune);

			Room exitRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 3));
			if (exitRoom == null) continue;
			filler.addAllocatedRoom(exitRoom);
			exitRoom.setLocation(new LevelExit(rune));
			entryRoom.setLocation(new RevealMapShrine(exitRoom));

			Room druidRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(exitRoom.getPoint(), 2));
			druidRoom.setLocation(runeFinder);
			filler.addAllocatedRoom(druidRoom);

			// statue should be placed (and allocated) before monsters
			Room statueRoom = filler.getUnallocatedRandomRoom();
			statueRoom.setLocation(new Statue());
			filler.addAllocatedRoom(statueRoom);

			Room orcRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(new JDPoint((int)(dungeonSizeX/2), (int)(dungeonSizeY/2)), 2));
			if (orcRoom == null) continue;
			Orc runeRunner = new Orc(15000);
			// TODO: setAI must (!) be called before de.jdungeon.figure is set into room! fix this!
			DungeonVisibilityMap runeRunnerRoomVisibility = runeRunner.createVisibilityMap(dungeon);
			FigureInfo runnerInfo = FigureInfo.makeFigureInfo(runeRunner, runeRunnerRoomVisibility);
			runeRunner.setAI(new RuneRunnerAI(runnerInfo, ItemInfo.makeItemInfo(rune, runeRunnerRoomVisibility)));
			orcRoom.figureEnters(runeRunner, RouteInstruction.Direction.North.getValue(), -1);
			runeRunner.takeItem(rune);
			runeRunner.takeItem(new DustItem(8));
			filler.addAllocatedRoom(orcRoom);

			Room ogreRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(new JDPoint((int)(dungeonSizeX/2), (int)(dungeonSizeY/2)), 2));
			if (ogreRoom == null) continue;
			Ogre ogre = new Ogre(20000);
			ogreRoom.figureEnters(ogre, RouteInstruction.Direction.North.getValue(), -1);
			filler.addAllocatedRoom(ogreRoom);

			Room wolfRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(new JDPoint((int)(dungeonSizeX/2), (int)(dungeonSizeY/2)), 2));
			if (wolfRoom == null) continue;
			Wolf wolf = new Wolf(8000);
			wolf.takeItem(new DustItem(4));
			wolfRoom.figureEnters(wolf, RouteInstruction.Direction.North.getValue(), -1);
			filler.addAllocatedRoom(wolfRoom);

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
			filler.removeDoors(6, entryPoint);

			Collection<DeadEndPath> deadEnds = filler.getDeadEndsUnallocated();

			// key room
			DeadEndPath shortestDeadEndPath = DeadEndPath.getShortestDeadEndPath(deadEnds);
			if (shortestDeadEndPath == null) continue;
			deadEnds.remove(shortestDeadEndPath);
			Room keyRoom = shortestDeadEndPath.getEndRoom();

			// this is just the trick to allow exit key setting after dungeon thinning out
			Chest keyChest = new Chest(new ScrollMagic(new Steal()));
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
			corpseRoom.setLocation(new Corpse(list, corpseRoom, 1));

			// set healing fountain
			Room fountainRoom = null;
			if(!deadEnds.isEmpty()) {
				DeadEndPath fountainDeadEndPath = DeadEndPath.getLongestDeadEndPath(deadEnds);
				fountainRoom = fountainDeadEndPath.getEndRoom();
			} else {
				fountainRoom = filler.getUnallocatedRandomRoom();
			}
			fountainRoom.setLocation(new HealthFountain(30, 1));
			filler.addAllocatedRoom(fountainRoom);

			// set scout shrine
			Room scoutShrineRoom = filler.getUnallocatedRoomNearCenter();
			scoutShrineRoom.setLocation(new ScoutShrine(scoutShrineRoom));
			filler.addAllocatedRoom(scoutShrineRoom);




			accomplished = true;
		}

	}

	@Override
	public Dungeon getDungeon() {
		return dungeon;
	}

}
