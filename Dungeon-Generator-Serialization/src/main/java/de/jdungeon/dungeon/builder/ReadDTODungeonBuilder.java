package de.jdungeon.dungeon.builder;

import java.util.Collection;

import com.badlogic.gdx.Gdx;

/**
 * This is a Dummy DungeonBuilder that will never actually be used,
 * as in this case the pre-generated dungeon is read from the asset files.
 */
public class ReadDTODungeonBuilder implements DungeonBuilder {
	@Override
	public DungeonResult build() throws DungeonGenerationException {
		Gdx.app.log("ERROR", "build() should never be called on " + this.getClass().getSimpleName());
		return null;
	}

	@Override
	public DungeonBuilder gridSize(int width, int height) {
		return error();
	}
	public DungeonBuilder error() {
		Gdx.app.log("ERROR", "build() should never be called on " + this.getClass().getSimpleName());
		return null;
	}

	@Override
	public DungeonBuilder setStartingPoint(StartLocationBuilder startBuilder) {
		return error();
	}

	@Override
	public DungeonBuilder setMaxDeadEnds(int maxDeadEnds) {
		return error();
	}

	@Override
	public DungeonBuilder addLocation(LocatedEntityBuilder location) {
		return error();
	}

	@Override
	public DungeonBuilder addKey(KeyBuilder key) {
		return error();
	}

	@Override
	public DungeonBuilder addLocationsShortestDistanceExactlyConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int distance) {
		return error();
	}

	@Override
	public DungeonBuilder addLocationsShortestDistanceAtLeastConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int distance) {
		return error();
	}

	@Override
	public DungeonBuilder setMinExitPathLength(int pathLength) {
		return error();
	}

	@Override
	public DungeonBuilder addHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height, boolean setAllInternalDoors) {
		return error();
	}

	@Override
	public DungeonBuilder addHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height) {
		return error();
	}

	@Override
	public DungeonBuilder setMinAmountOfDoors(int numberOfDoors) {
		return error();
	}

	@Override
	public DungeonBuilder setMaxAmountOfDoors(int numberOfDoors) {
		return error();
	}

	@Override
	public DungeonBuilder addPredefinedDoor(int x1, int y1, int x2, int y2) {
		return error();
	}

	@Override
	public DungeonBuilder addPredefinedWall(int x1, int y1, int x2, int y2) {
		return error();
	}

	@Override
	public boolean hasSolution() {
		try {
			return this.build() != null;
		}
		catch (DungeonGenerationException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public DungeonBuilder addDoorSpecification(DoorSpecification hall) {
		return error();
	}

	@Override
	public DungeonBuilder addPredefinedWalls(Collection doors) {
		return error();
	}

	@Override
	public DungeonBuilder addPredefinedDoors(Collection doors) {
		return error();
	}

	@Override
	public DungeonBuilder addLocations(Collection location) {
		return error();
	}
}
