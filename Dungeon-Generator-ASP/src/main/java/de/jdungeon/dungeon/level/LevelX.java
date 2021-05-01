package de.jdungeon.dungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.builder.DoorMarker;
import de.jdungeon.dungeon.builder.DungeonBuilder;
import de.jdungeon.dungeon.builder.DungeonBuilderASP;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.DungeonGeneratorASPUtils;
import de.jdungeon.dungeon.builder.DungeonResult;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.location.RevealMapShrine;

public class LevelX extends AbstractASPDungeonFactory {

	public LevelX() {
		/*
		this.start = new JDPoint(4, 9);
		this.exit = new JDPoint(4, 1);
		this.gridWidth = 10;
		this.getHeight = 10;
		int hallUpperLeftCornerX = start.getX() - 1;
		int hallLowerLeftCornerY = start.getY() - 6;
		int hallWidth = 3;
		int hallHeight = 3;
		addHall(DungeonGeneratorASPUtils.createHall(hallUpperLeftCornerX, hallLowerLeftCornerY, hallWidth, hallHeight, true));

		// we need entrance and exit for our hall
		DoorMarker openNorth = new DoorMarker(hallUpperLeftCornerX + 1, hallLowerLeftCornerY, hallUpperLeftCornerX + 1, hallLowerLeftCornerY - 1);
		this.walls.remove(openNorth);
		DoorMarker openSouth = new DoorMarker(hallUpperLeftCornerX + 1, hallLowerLeftCornerY + hallHeight, hallUpperLeftCornerX + 1, hallLowerLeftCornerY + hallHeight - 1);
		this.walls.remove(openSouth);
		LocationBuilder startL = new LocationBuilder(RevealMapShrine.class).setRoom(start.getX(), start.getY());

		DungeonBuilder dungeonBuilder = new DungeonBuilderASP();
		dungeonBuilder.gridSize(gridWidth, getHeight)
				.setStartingPoint(startL)
				.setMinExitPathLength(minPathLength)
				.setMaxAmountOfDoors(37)
				.addPredefinedDoors(doors)
				.addPredefinedWalls(walls);

		DungeonResult build = null;
		try {
			this.dungeon = dungeonBuilder.build();
		}
		catch (DungeonGenerationException e) {
			e.printStackTrace();
		}

		 */
	}

	@Override
	public Dungeon createDungeon() throws DungeonGenerationException {
		return null;
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return null;
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
