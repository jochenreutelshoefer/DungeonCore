package level.stagetwo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dungeon.Chest;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DeadEndPath;
import dungeon.generate.DistanceAtLeastConstraint;
import dungeon.quest.ReversibleRoomQuest;
import dungeon.quest.RoomQuestWall;
import dungeon.util.RouteInstruction;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Wolf;
import item.DustItem;
import item.HealPotion;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.VisibilityCheatBall;
import item.paper.ScrollMagic;
import item.quest.MoonRune;
import level.AbstractDungeonFactory;
import level.generation.SimpleDungeonFiller;
import shrine.Corpse;
import shrine.HealthFountain;
import shrine.LevelExit;
import shrine.MoonRuneFinderShrine;
import shrine.RevealMapShrine;
import shrine.Statue;
import spell.KeyLocator;
import spell.Steal;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class MoonRuneChase extends AbstractDungeonFactory {

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
	public Dungeon createDungeon() {

		Dungeon dungeon = null;

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
			exitRoom.setShrine(new LevelExit(rune));
			entryRoom.setShrine(new RevealMapShrine(exitRoom));

			Room druidRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(exitRoom.getPoint(), 2));
			druidRoom.setShrine(runeFinder);
			filler.addAllocatedRoom(druidRoom);

			// statue should be placed (and allocated) before monsters
			Room statueRoom = filler.getUnallocatedRandomRoom();
			statueRoom.setShrine(new Statue());
			filler.addAllocatedRoom(statueRoom);

			Room orcRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(new JDPoint((int)(dungeonSizeX/2), (int)(dungeonSizeY/2)), 2));
			if (orcRoom == null) continue;
			Orc runeRunner = new Orc(1500);
			// TODO: setAI must (!) be called before figure is set into room! fix this!
			DungeonVisibilityMap runeRunnerRoomVisibility = runeRunner.createVisibilityMap(dungeon);
			FigureInfo runnerInfo = FigureInfo.makeFigureInfo(runeRunner, runeRunnerRoomVisibility);
			runeRunner.setAI(new RuneRunnerAI(runnerInfo, ItemInfo.makeItemInfo(rune, runeRunnerRoomVisibility)));
			orcRoom.figureEnters(runeRunner, RouteInstruction.Direction.North.getValue());
			runeRunner.takeItem(rune);
			runeRunner.takeItem(new DustItem(8));
			filler.addAllocatedRoom(orcRoom);

			Room ogreRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(new JDPoint((int)(dungeonSizeX/2), (int)(dungeonSizeY/2)), 2));
			if (ogreRoom == null) continue;
			Ogre ogre = new Ogre(2000);
			ogreRoom.figureEnters(ogre, RouteInstruction.Direction.North.getValue());
			filler.addAllocatedRoom(ogreRoom);

			Room wolfRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(new JDPoint((int)(dungeonSizeX/2), (int)(dungeonSizeY/2)), 2));
			if (wolfRoom == null) continue;
			Wolf wolf = new Wolf(800);
			wolf.takeItem(new DustItem(4));
			wolfRoom.figureEnters(wolf, RouteInstruction.Direction.North.getValue());
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
			corpseRoom.setShrine(new Corpse(list, corpseRoom, 1));

			// set healing fountain
			Room fountainRoom = null;
			if(!deadEnds.isEmpty()) {
				DeadEndPath fountainDeadEndPath = DeadEndPath.getLongestDeadEndPath(deadEnds);
				fountainRoom = fountainDeadEndPath.getEndRoom();
			} else {
				fountainRoom = filler.getUnallocatedRandomRoom();
			}
			fountainRoom.setShrine(new HealthFountain(10, 1));

			// for testing only
			entryRoom.addItem(new VisibilityCheatBall());




			accomplished = true;
		}

		return dungeon;

	}


}
