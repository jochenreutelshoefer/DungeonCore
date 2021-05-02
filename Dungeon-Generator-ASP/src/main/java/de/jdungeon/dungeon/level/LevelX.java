package de.jdungeon.dungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.builder.DungeonBuilderASP;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.DungeonResult;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;

public class LevelX extends AbstractASPDungeonFactory {

	private DungeonResult dungeonBuild;

	@Override
	public Dungeon createDungeon() throws DungeonGenerationException {
		JDPoint start = new JDPoint(4, 5);

		LocationBuilder exit = new LocationBuilder(LevelExit.class).setRoom(5,0);
		LocationBuilder startL = new LocationBuilder(RevealMapShrine.class).setRoom(start.getX(), start.getY());
		dungeonBuild = new DungeonBuilderASP()
				.gridSize(10, 6)
				.setStartingPoint(startL)
				.setMinAmountOfDoors(60)
				.addLocation(exit)
				.addLocationsLeastDistanceConstraint(startL, exit, 20)
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
