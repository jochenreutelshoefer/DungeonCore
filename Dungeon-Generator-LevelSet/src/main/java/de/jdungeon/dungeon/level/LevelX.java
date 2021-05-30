package de.jdungeon.dungeon.level;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.DungeonBuilderFactory;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.dungeon.builder.StartLocationBuilder;
import de.jdungeon.location.LevelExit;

public class LevelX extends AbstractLevel {

	public LevelX(DungeonBuilderFactory builderFactory) {
		super(builderFactory);
	}

	@Override
	protected void doGenerate() throws DungeonGenerationException {
		JDPoint start = new JDPoint(4, 5);
		JDPoint exitP = new JDPoint(5, 0);
		LocationBuilder exit = new LocationBuilder(LevelExit.class, 5, 0);
		StartLocationBuilder startL = new StartLocationBuilder(start, exitP);
		dungeonBuild = createBuilder()
				.gridSize(10, 6)
				.setStartingPoint(startL)
				.setMinAmountOfDoors(60)
				.setMaxDeadEnds(3)
				.addLocation(exit)
				.addLocationsShortestDistanceExactlyConstraint(startL, exit, 20)
				.build();
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Level X";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 200;
	}
}
