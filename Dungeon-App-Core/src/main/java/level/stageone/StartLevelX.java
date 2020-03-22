package level.stageone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dungeon.Chest;
import dungeon.Door;
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
import shrine.ScoutShrine;
import shrine.Statue;
import spell.KeyLocator;

import static dungeon.util.RouteInstruction.Direction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 15.03.20.
 */
public class StartLevelX extends AbstractDungeonFactory {

	public static final int DUNGEON_SIZE_Y = 9;
	public static final int DUNGEON_SIZE_X = 9;

	@Override
	public JDPoint getHeroEntryPoint() {
		return new JDPoint(4, 8);
	}

	@Override
	public Dungeon createDungeon() {

		List<Key> allKeys = Key.generateKeylist();
		Key exitKey = allKeys.get(0);

		Dungeon dungeon = new Dungeon(DUNGEON_SIZE_X, DUNGEON_SIZE_Y);
		createAllDoors(dungeon);
		filler = new SimpleDungeonFiller(dungeon, new ArrayList<Key>());
		Room exitRoom = dungeon.getRoomNr(4,0);
		exitRoom.removeAllDoorsExcept(Direction.South);
		filler.addAllocatedRoom(exitRoom);
		exitRoom.setShrine(new LevelExit());

		entryPoint = this.getHeroEntryPoint();
		Room entryRoom = dungeon.getRoom(entryPoint);
		filler.addAllocatedRoom(entryRoom);
		entryRoom.removeAllDoorsExcept(Direction.North);
		entryRoom.setShrine(new RevealMapShrine(exitRoom));
		entryRoom.addItem(new VisibilityCheatBall());


		Room statueRoom = dungeon.getRoom(4, 7);
		statueRoom.removeAllDoorsExcept(Direction.North, Direction.South);
		statueRoom.setShrine(new Statue());
		filler.addAllocatedRoom(statueRoom);

		Room hall44 = dungeon.getRoomNr(4, 4);
		hall44.getDoor(Direction.North).setKey(exitKey);

		Room hall36 = dungeon.getRoomNr(3, 6);
		hall36.removeDoors(Direction.South, Direction.West);

		Room hall56 = dungeon.getRoom(5, 6);
		hall56.removeDoors(Direction.South, Direction.East);

		Room hall34 = dungeon.getRoomNr(3, 4);
		hall34.removeDoors(Direction.North, Direction.West);

		Room hall54 = dungeon.getRoom(5, 4);
		hall54.removeDoors(Direction.North, Direction.East);

		Room exitPath43 = dungeon.getRoomNr(4,3);
		exitPath43.removeAllDoorsExcept(Direction.North, Direction.South);

		Room exitPath42 = dungeon.getRoomNr(4,2);
		exitPath42.removeAllDoorsExcept(Direction.North, Direction.South);

		Room exitPath41 = dungeon.getRoomNr(4,1);
		exitPath41.removeAllDoorsExcept(Direction.North, Direction.South);

		if(Math.random() > 0.5) {
			// key to be found on the right
			dungeon.getRoomNr(3,5).removeDoors(Direction.West); // on the left goes nothing...

			dungeon.getRoom(6,5).removeAllDoorsExcept(Direction.West, Direction.East);
			dungeon.getRoom(7,5).removeAllDoorsExcept(Direction.West, Direction.East);

			Room keyRoom = dungeon.getRoom(8, 5);
			keyRoom.removeAllDoorsExcept(Direction.West);
			Chest keyChest = new Chest(exitKey);
			keyRoom.setChest(keyChest);
		}
		else {
			// key to be found on the left
			dungeon.getRoomNr(5,5).removeDoors(Direction.East); // on the right goes nothing...

			dungeon.getRoom(2,5).removeAllDoorsExcept(Direction.West, Direction.East);
			dungeon.getRoom(1,5).removeAllDoorsExcept(Direction.West, Direction.East);

			Room keyRoom = dungeon.getRoom(0, 5);
			keyRoom.removeAllDoorsExcept(Direction.East);
			Chest keyChest = new Chest(exitKey);
			keyRoom.setChest(keyChest);
		}

		filler.setToWallUnreachableRoom(getHeroEntryPoint());


		Room wolfRoom = dungeon.getRoom(4 ,5 );
		HadrianAI ai = new HadrianAI();
		Wolf hadrian = new Wolf( 1100, ai, "Hadrian" );
		wolfRoom.figureEnters(hadrian, RouteInstruction.Direction.North.getValue(),-1);
		ai.setFigure(FigureInfo.makeFigureInfo(hadrian, hadrian.getRoomVisibility()));
		filler.setAllFound(hadrian.getRoomVisibility());
		filler.addAllocatedRoom(wolfRoom);

		return dungeon;
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Hadrian";
	}

	@Override
	public String getDescription() {
		return "Hadrians Höhle";
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 100;
	}
}
