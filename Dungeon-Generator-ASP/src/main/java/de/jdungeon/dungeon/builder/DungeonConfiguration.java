package de.jdungeon.dungeon.builder;

public class DungeonConfiguration {

    private int gridWidth = 6;
    private int gridHeight = 6;

    private int startX = 2;
    private int startY = 2;

    private int exitX = 5;
    private int exitY = 5;

    private int minExitPathLength = 8;
    private int totalAmountOfDoors = 30;

    public DungeonConfiguration(int gridWidth, int gridHeight, int startX, int startY, int exitX, int exitY, int minExitPathLength, int totalAmountOfDoors) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.startX = startX;
        this.startY = startY;
        this.exitX = exitX;
        this.exitY = exitY;
        this.minExitPathLength = minExitPathLength;
        this.totalAmountOfDoors = totalAmountOfDoors;
    }

    public DungeonConfiguration(DungeonBuilderASP builder) {
        this(builder.gridWidth, builder.gridHeight, builder.startX, builder.startY, builder.exitX, builder.exitY, builder.minExitPathLength, builder.totalAmountOfDoorsMin);
    }

    @Override
    public String toString() {
        return "DungeonASPConfiguration{" +
                "gridWidth=" + gridWidth +
                ", gridHeight=" + gridHeight +
                ", startX=" + startX +
                ", startY=" + startY +
                ", exitX=" + exitX +
                ", exitY=" + exitY +
                ", minExitPathLength=" + minExitPathLength +
                ", totalAmountOfDoors=" + totalAmountOfDoors +
                '}';
    }
}
