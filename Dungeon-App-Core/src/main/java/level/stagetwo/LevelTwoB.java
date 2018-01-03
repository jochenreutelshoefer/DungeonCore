package level.stagetwo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ai.AbstractAI;
import ai.ChaserAI;
import dungeon.Chest;
import dungeon.Dir;
import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.Room;
import dungeon.generate.DistanceAtLeastConstraint;
import dungeon.generate.DungeonFiller;
import dungeon.quest.ReversibleRoomQuest;
import dungeon.quest.RoomQuestWall;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.monster.Dwarf;
import figure.monster.MonsterInfo;
import figure.monster.Ogre;
import figure.monster.Orc;
import game.ControlUnit;
import item.Item;
import item.Key;
import item.VisibilityCheatBall;
import item.paper.ScrollMagic;
import level.AbstractDungeonFactory;
import level.stageone.SimpleDungeonFiller;
import shrine.HealthFountain;
import shrine.LevelExit;
import shrine.RevealMapShrine;
import shrine.Statue;
import spell.KeyLocator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.18.
 */
public class LevelTwoB extends AbstractDungeonFactory {

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
		return "Level 2 B";
	}

	@Override
	public String getDescription() {
		return "Level 2 B";
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
			Orc orcGuard = new Orc(1000);
			orcGuard.setAI(new ChaserAI());
			keyRoom.figureEnters(orcGuard, 1);
			filler.addAllocatedRoom(keyRoom);

			Room dwarfRoom = filler.getUnallocatedRandomRoom(new DistanceAtLeastConstraint(entryPoint, 2));
			if(dwarfRoom == null) continue;
			Figure dwarf = new Dwarf();
			dwarfRoom.figureEntersAtPosition(dwarf, 0, Position.Pos.SE.getValue());
			MonsterInfo dwarfInfo = (MonsterInfo) FigureInfo.makeFigureInfo(dwarf,
					dwarf.getRoomVisibility());

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
			roomQuests.add(new RoomQuestWall(filler, 1 ,2));
			roomQuests.add(new RoomQuestWall(filler, 1 ,1));
			roomQuests.add(new RoomQuestWall(filler, 1 ,1));
			roomQuests.add(new RoomQuestWall(filler, 1 ,1));
			setupRoomQuests(dungeon, filler, entryRoom, entryPoint, roomQuests);


			filler.getUnallocatedRandomRoom().setShrine(new HealthFountain(10, 1));

			filler.removeDoors(3, entryPoint);

			entryRoom.addItem(new VisibilityCheatBall());
			entryRoom.figureEnters(new Ogre(500), Dir.NORTH);

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
