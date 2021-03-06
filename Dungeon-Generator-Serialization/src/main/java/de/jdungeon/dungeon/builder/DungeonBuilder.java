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
     * @return builder
     */
    DungeonBuilder setStartingPoint(StartLocationBuilder startBuilder);

    /**
     * Sets the maximum number of dead ends allow.
     * A dead end is a room with only a door in one direction.
     *
     * @param maxDeadEnds
     * @return builder
     */
    DungeonBuilder setMaxDeadEnds(int maxDeadEnds);

    /**
     * Adds a location to the dungeon. I can have a fixed position.
     * If no fixed position is specified, it will be determined by the dungeon generator.
     *
     * @param location
     * @return
     */
    DungeonBuilder addLocation(LocatedEntityBuilder location);

    /**
     *
     * @set DungeonBuilder#addLocation()
     *
     * @param location
     * @return
     */
    DungeonBuilder addLocations(Collection<LocatedEntityBuilder> location);


    DungeonBuilder addKey(KeyBuilder key);

    /**
     * Adds a constraint leading to a dungeon, where the shortest path between the two locations is exactly 'distance'.
     * (Ignores locks).
     *
     * @param locationA
     * @param locationB
     * @param distance
     * @return
     */
    DungeonBuilder addLocationsShortestDistanceExactlyConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int distance );

    /**
     * Adds a constraint leading to a dungeon, where the shortest path between the two locations is exactly 'distance'.
     * (Ignores locks).
     *
     * @param locationA
     * @param locationB
     * @param distance
     * @return
     */
    DungeonBuilder addLocationsShortestDistanceAtLeastConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int distance );


    /**
     * Sets the length of the shortest path from start to exit.
     *
     * @param pathLength the length of the shortest path from start to exit.
     * @return builder
     */
    DungeonBuilder setMinExitPathLength(int pathLength);


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

    /**
     * Adds a DoorSpecification to the build process.
     *
     * @param hall
     * @return builder
     */
    DungeonBuilder addDoorSpecification(DoorSpecification hall);

    /**
     * Adds a Hall to the build process.
     *
     * @param hall
     * @return builder
     */
    DungeonBuilder addHall(Hall hall);
}
