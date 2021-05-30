package de.jdungeon.dungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.DoorMarker;
import de.jdungeon.dungeon.builder.DoorSpecification;
import de.jdungeon.dungeon.builder.DungeonBuilderFactory;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.HallBuilder;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.dungeon.builder.StartLocationBuilder;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.ScoutShrine;

public class Level16x16 extends AbstractLevel {


	public Level16x16(DungeonBuilderFactory builderFactory) {
		super(builderFactory);
	}

	@Override
	protected void doGenerate() throws DungeonGenerationException {
		JDPoint start = new JDPoint(4, 15);

		int hallUpperLeftCornerX = start.getX() - 1;
		int hallLowerLeftCornerY = start.getY() - 4;
		int hallWidth = 5;
		int hallHeight = 5;
		DoorSpecification centerHall = new HallBuilder(hallUpperLeftCornerX, hallLowerLeftCornerY, hallWidth, hallHeight)
				.createAllInternalDoors()
				// we need entrance and exit for our hall
				.removeWall(new DoorMarker(hallUpperLeftCornerX + 1, hallLowerLeftCornerY, hallUpperLeftCornerX + 1, hallLowerLeftCornerY - 1))
				.removeWall(new DoorMarker(hallUpperLeftCornerX + 1, hallLowerLeftCornerY + hallHeight, hallUpperLeftCornerX + 1, hallLowerLeftCornerY + hallHeight - 1))
				.build();

		JDPoint exitP = new JDPoint(5, 0);
		LocationBuilder exit = new LocationBuilder(LevelExit.class, exitP);
		StartLocationBuilder startL = new StartLocationBuilder(start, exitP);
		LocationBuilder scoutTower = new LocationBuilder(ScoutShrine.class, 7, 3);
		dungeonBuild = createBuilder()
				.gridSize(16, 16)
				.setStartingPoint(startL)
				.setMinAmountOfDoors(501)
				.addDoorSpecification(centerHall)
				.addLocation(exit)
				.addLocation(scoutTower)
				.addLocationsShortestDistanceExactlyConstraint(startL, exit, 40)
				.build();
	}

	@Override
	public Dungeon getDungeon() {
		return dungeonBuild.getDungeon();
	}



	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Level Y";
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
