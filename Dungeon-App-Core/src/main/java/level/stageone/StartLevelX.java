package level.stageone;

import java.util.ArrayList;
import java.util.List;

import dungeon.Chest;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DistanceAtMostConstraint;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.monster.Wolf;
import item.Key;
import item.OxygenPotion;
import item.VisibilityCheatBall;
import level.AbstractDungeonFactory;
import level.generation.SimpleDungeonFiller;
import location.HealthFountain;
import location.LevelExit;
import location.RevealMapShrine;
import location.ScoutShrine;
import location.Statue;

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
		filler = new SimpleDungeonFiller(dungeon, new ArrayList<>());
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

		// we set a spy tower in the middle
		Room hall45 = dungeon.getRoom(4,5);
		hall45.setShrine(new ScoutShrine(hall45, 1));
		filler.addAllocatedRoom(hall45);

		// we set a health fountain
		Room fountainRoom = filler.getUnallocatedRandomRoom(new DistanceAtMostConstraint(hall45.getPoint(), 2));
		fountainRoom.setShrine(new HealthFountain(30, 1));
		filler.isAllocated(fountainRoom);

		// we set a gimmick chest room
		Room gimmickRoom = filler.getUnallocatedRandomRoom(new DistanceAtMostConstraint(hall45.getPoint(), 2));
		Chest gimmickChest = new Chest(new OxygenPotion());
		gimmickRoom.setChest(gimmickChest);
		filler.isAllocated(gimmickRoom);

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
