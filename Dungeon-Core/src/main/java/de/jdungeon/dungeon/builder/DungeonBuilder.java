package de.jdungeon.dungeon.builder;

import java.util.Collection;

/**
 * A builder allows to build a dungeon according to particular specifications.
 */
public interface DungeonBuilder<T extends DungeonResult> {

    /**
     * Builds the actual dungeon with start, exit and rooms and doors.
     *
     * @return a dungeon level
     */
    T build() throws DungeonGenerationException;

    /**
     * Sets the dungeon coordinate system grid size to the given values.
     *
     * @param width max width
     * @param height max height
     * @return builder
     */
    DungeonBuilder gridSize(int width, int height);

    /**
     * Sets the point, where the player enters the dungeon.
     *
     * @param x x coordinate of entrance room
     * @param y y coordinate of entrance room
     * @return builder
     */
    DungeonBuilder setStartingPoint(LocationBuilder startBuilder);

    /**
     * Adds a location to the dungeon. I can have a fixed position.
     * If no fixed position is specified, it will be determined by the dungeon generator.
     *
     * @param location
     * @return
     */
    DungeonBuilder addLocation(LocationBuilder location);


    DungeonBuilder addKey(KeyBuilder key);

    /**
     * Adds a constraint leading
     * @param locationA
     * @param locationB
     * @param distance
     * @return
     */
    DungeonBuilder addLocationsLeastDistanceConstraint(LocationBuilder locationA, LocationBuilder locationB, int distance );

    /**
     * Sets the length of the shortest path from start to exit.
     *
     * @param pathLength the length of the shortest path from start to exit.
     * @return builder
     */
    DungeonBuilder setMinExitPathLength(int pathLength);

    /**
     * Sets ALL walls for the specified halls.
     * If the wall is meant to have entries/exits, then walls need
     * explicitly to be removed afterwards.
     *
     *
     * @param upperLeftCornerX x coordinate of the upper left corner room
     * @param upperLeftCornderY y coordinate of the upper left corner room
     * @param width size in number of rooms in x direction
     * @param height size in number of rooms in y direction
     * @param setAllInternalDoors whether to set all internal doors as predefined
     * @return builder
     */
    @Deprecated // use HallBuilder
    DungeonBuilder addHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height, boolean setAllInternalDoors);

    /**
     * Calls addHall with 'false' as the internal-doors-parameter.
     *
     */
    @Deprecated // use HallBuilder
    DungeonBuilder addHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height);


    /**
     * Specifies the total number of doors expected to be in the dungeon.
     * Note: Doors are always bidirectional, that is, a door goes von A to B and from B to A.
     *
     * @param numberOfDoors total number of door in the generated dungeon
     * @return builder
     */
    DungeonBuilder setMinAmountOfDoors(int numberOfDoors);

    DungeonBuilder setMaxAmountOfDoors(int numberOfDoors);

    /**
     * Sets a door connection between the rooms indicated by the given coordinates.
     * Note: The door is always bidirectional/symmetric, that is only one direction needs to be specified.
     *
     * @param x1 x coordinate of room 1
     * @param y1 y coordinate of room 1
     * @param x2 x coordinate of room 2
     * @param y2 y coordinate of room 2
     * @return builder
     */
    DungeonBuilder addPredefinedDoor(int x1, int y1, int x2, int y2);

    /**
     * Sets the given collection of doors @see addPredefinedDoor.
     *
     * @param doors doors to be set.
     * @return builder
     */
    DungeonBuilder addPredefinedDoors(Collection<DoorMarker> doors);

    /**
     * Specifies that on this room border there MUST NOT be a door.
     *
     * @param x1 x coordinate of room 1
     * @param y1 y coordinate of room 1
     * @param x2 x coordinate of room 2
     * @param y2 y coordinate of room 2
     * @return builder
     */
    DungeonBuilder addPredefinedWall(int x1, int y1, int x2, int y2);

    /**
     * Sets the given collection of doors @see addPredefinedWall.
     *
     * @param doors doors to be set.
     * @return builder
     */
    DungeonBuilder addPredefinedWalls(Collection<DoorMarker> doors);

    /**
     * Determines whether for the current dungeon specification, a solvable level can be generated.
     *
     * @return true if a solvable level can be generated, false otherwise.
     */
    boolean hasSolution();

    DungeonBuilder addDoorSpecification(DoorSpecification hall);
}
