package level.stageone;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import level.AbstractDungeonFactory;
import level.DungeonFactory;
import shrine.LevelExit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class PreliminaryStartLevelFactory extends AbstractDungeonFactory {
	@Override
	public Dungeon createDungeon() {
		return makeDungeon();
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return new JDPoint(1,2);
	}

	@Override
	public String icon() {
		// tODO:
		return null;
	}

	@Override
	public String getName() {
		return "Stage 1 (Preliminary)";
	}

	@Override
	public String getDescription() {
		return "Einstiegslevel";
	}

	private Dungeon makeDungeon() {
		Dungeon dungeon = new Dungeon(3, 3, 1, 2);
		createAllDoors(dungeon);
		Room room = dungeon.getRoom(new JDPoint(1, 0));
		room.setShrine(new LevelExit());
		return dungeon;
	}

}
