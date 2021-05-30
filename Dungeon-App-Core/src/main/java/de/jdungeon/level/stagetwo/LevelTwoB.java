package de.jdungeon.level.stagetwo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.ai.AbstractAI;
import de.jdungeon.ai.ChaserAI;
import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DistanceAtLeastConstraint;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.quest.ReversibleRoomQuest;
import de.jdungeon.dungeon.quest.RoomQuestWall;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Dwarf;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.monster.Orc;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.item.Item;
import de.jdungeon.item.Key;
import de.jdungeon.item.paper.ScrollMagic;
import de.jdungeon.level.AbstractDungeonFactory;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.location.Statue;
import de.jdungeon.spell.KeyLocator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.18.
 */
public class LevelTwoB extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 5;
	public static final int DUNGEON_SIZE_X = 5;
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
		return new JDPoint(2, 4);
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Level 2 B";
	}

	@Override
	public String getDescription() {
		return "Level 2 B";
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 100;
	}

	private Dungeon createStartDungeon() {

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
			Room entryRoom = dungeon.getRoom(entryPoint);
			filler.addAllocatedRoom(entryRoom);

			Room statueRoom = filler.getUnallocatedRandomRoom();
			statueRoom.setLocation(new Statue());
			filler.addAllocatedRoom(statueRoom);

			Room exitRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 3));
			;
			if (exitRoom == null) continue;
			JDPoint exitPoint = exitRoom.getPoint();
			filler.addAllocatedRoom(exitRoom);
			exitRoom.setLocation(new LevelExit());
			exitRoom.removeAllDoors();
			entryRoom.setLocation(new RevealMapShrine(exitRoom));

			exitRoom.setDoor(new Door(exitRoom, RouteInstruction.Direction.South, exitKey), RouteInstruction.Direction.South, true);

			Room keyRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(exitPoint, 3), new DistanceAtLeastConstraint(entryPoint, 3));
			if (keyRoom == null) continue;

			Chest keyChest = new Chest(exitKey);
			keyRoom.setChest(keyChest);
			Orc orcGuard = new Orc(1000 * 10);
			orcGuard.setAI(new ChaserAI());
			keyRoom.figureEnters(orcGuard, 1, -1);
			filler.addAllocatedRoom(keyRoom);

			Room dwarfRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 2));
			if (dwarfRoom == null) continue;
			Figure dwarf = new Dwarf();
			dwarfRoom.figureEntersAtPosition(dwarf, 0, Position.Pos.SE.getValue());
			MonsterInfo dwarfInfo = (MonsterInfo) FigureInfo.makeFigureInfo(dwarf,
					dwarf.getViwMap());

			AbstractAI ai = new OrcHaterAI();
			ai.setFigure(dwarfInfo);
			ControlUnit control = new FigureControl(dwarfInfo, ai);

			dwarf.setControl(control);

			List<Item> itemsL = new LinkedList<>();
			itemsL.add(new ScrollMagic(new KeyLocator(1, 0, 0, 0, 0, 0)));
			Chest entryChest = new Chest(itemsL);
			entryRoom.setChest(entryChest);

			filler.removeDoors(2, entryPoint);

			List<ReversibleRoomQuest> roomQuests = new ArrayList<>();

			// configure RoomQuests to be inserted
			roomQuests.add(new RoomQuestWall(filler, 1, 2));
			roomQuests.add(new RoomQuestWall(filler, 1, 1));
			roomQuests.add(new RoomQuestWall(filler, 1, 1));
			roomQuests.add(new RoomQuestWall(filler, 1, 1));
			setupRoomQuests(dungeon, filler, entryRoom, entryPoint, roomQuests);

			filler.removeDoors(3, entryPoint);

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
