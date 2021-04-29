package de.jdungeon.dungeon.builder;

public class DungeonASPConfiguration {

	protected int gridWidth = 7;
	protected int gridHeight = 7;

	// starting point
	protected int startX = 1;
	protected int startY = 1;

	// level exit point
	protected int exitX = 4;
	protected int exitY = 5;

	// path length from start to exit
	protected int minExitPathLength = 13;

	// number of doors
	protected int totalAmountOfDoors = 130;

	public DungeonASPConfiguration() {
	}

	public DungeonASPConfiguration(int gridWidth, int gridHeight, int startX, int startY, int exitX, int exitY, int minExitPathLength, int totalAmountOfDoors) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.startX = startX;
		this.startY = startY;
		this.exitX = exitX;
		this.exitY = exitY;
		this.minExitPathLength = minExitPathLength;
		this.totalAmountOfDoors = totalAmountOfDoors;
	}
}
