package de.jdungeon.level.stageone;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.level.AbstractDungeonFactory;
import de.jdungeon.location.LevelExit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class PreliminaryStartLevelFactory extends AbstractDungeonFactory {

	private Dungeon dungeon;

	@Override
	public void create() {
		makeDungeon();
	}

	@Override
	public Dungeon getDungeon() {
		return dungeon;
	}

	@Override
	public LevelDTO getDTO() {
		throw new IllegalStateException("This DungeonFactory does not provide a DTO");
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

	@Override
	public int getRoundScoringBaseValue() {
		return 50;
	}

	private void makeDungeon() {
		dungeon = new Dungeon(3, 3, 1, 2);
		createAllDoors(dungeon);
		Room room = dungeon.getRoom(new JDPoint(1, 0));
		room.setLocation(new LevelExit());
	}

}
