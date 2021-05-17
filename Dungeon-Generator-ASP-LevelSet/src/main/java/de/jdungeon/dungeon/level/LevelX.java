package de.jdungeon.dungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.builder.DungeonBuilderASP;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.DungeonResult;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.dungeon.builder.StartLocationBuilder;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;

public class LevelX extends AbstractASPDungeonFactory {



	@Override
	public void create() throws DungeonGenerationException {
		JDPoint start = new JDPoint(4, 5);
		JDPoint exitP = new JDPoint(5, 0);
		LocationBuilder exit = new LocationBuilder(LevelExit.class, 5, 0);
		StartLocationBuilder startL = new StartLocationBuilder( start, exitP );
		dungeonBuild = new DungeonBuilderASP()
				.gridSize(10, 6)
				.setStartingPoint(startL)
				.setMinAmountOfDoors(60)
				.setMaxDeadEnds(3)
				.addLocation(exit)
				.addLocationsShortestDistanceExactlyConstraint(startL, exit, 20)
				.build();


	}



	@Override
	public LevelDTO getDTO() {
		return null;
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
