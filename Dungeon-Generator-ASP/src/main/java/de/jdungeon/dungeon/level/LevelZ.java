package de.jdungeon.dungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.builder.DoorMarker;
import de.jdungeon.dungeon.builder.DoorSpecification;
import de.jdungeon.dungeon.builder.DungeonBuilderASP;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.DungeonResult;
import de.jdungeon.dungeon.builder.HallBuilder;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.location.ScoutShrine;

public class LevelZ extends AbstractASPDungeonFactory {

	private DungeonResult dungeonBuild;

	@Override
	public Dungeon createDungeon() throws DungeonGenerationException {
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

		LocationBuilder exit = new LocationBuilder(LevelExit.class).setRoom(5,0);
		LocationBuilder startL = new LocationBuilder(RevealMapShrine.class).setRoom(start.getX(), start.getY());
		//LocationBuilder scoutTower = new LocationBuilder(ScoutShrine.class).setRoom(7,3);
		dungeonBuild = new DungeonBuilderASP()
				.gridSize(16, 16)
				.setStartingPoint(startL)
				.setMinAmountOfDoors(200)
				.addDoorSpecification(centerHall)
				.addLocation(exit)
				//.addLocation(scoutTower)
				.addLocationsLeastDistanceConstraint(startL, exit, 40)
				//.addLocationsLeastDistanceConstraint(startL, scoutTower, 11)
				//.addLocationsLeastDistanceConstraint(exit, scoutTower, 11)
				.build();

		return dungeonBuild.getDungeon();
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return dungeonBuild.getStartPosition();
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
