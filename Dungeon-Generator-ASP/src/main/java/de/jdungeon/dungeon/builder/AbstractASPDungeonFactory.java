package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.log.Log;

public abstract class AbstractASPDungeonFactory implements DungeonFactory {

	protected JDPoint start;
	protected JDPoint exit;
	protected int gridWidth = -1;
	protected int getHeight = -1;
	protected int minPathLength = -1;
	protected int minDoors = -1;
	protected Collection<DoorMarker> walls = new HashSet<>();
	protected Collection<DoorMarker> doors = new HashSet<>();
	protected DungeonResult dungeon;
	DungeonBuilder dungeonBuilder = new DungeonBuilderASP();

	@Override
	public Dungeon createDungeon() {
		return dungeon.getDungeon();
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return dungeon.getStartPosition();
	}

	protected void addHall(DoorSpecification hall) {
		this.doors.addAll(hall.getDoors());
		this.walls.addAll(hall.getWalls());
	}

}
