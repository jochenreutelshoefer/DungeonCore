package de.jdungeon.dungeon.builder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.asp.Result;
import de.jdungeon.dungeon.builder.asp.ShellASPSolver;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.log.Log;

public class DungeonBuilderASP implements DungeonBuilder<DTODungeonResult> {

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
	protected int maxDeadEnds = 5;

	// number of doors
	protected int totalAmountOfDoorsMin = -1;
	protected int totalAmountOfDoorsMax = -1;

	protected Collection<DoorMarker> predefinedDoors = new HashSet<>();
	protected Collection<DoorMarker> predefinedWalls = new HashSet<>();

	//Collection<AbstractLocationBuilder> locations = new HashSet<>();
	Map<String, LocatedEntityBuilder> locations = new HashMap<>();
	AbstractLocationBuilder startLocation;
	Collection<LocationsLeastDistanceConstraint> locationsShortestDistanceExactlyConstraints = new HashSet<>();
	Collection<LocationsLeastDistanceConstraint> locationsShortestDistanceAtLeastConstraints = new HashSet<>();
	Collection<KeyBuilder> keys = new HashSet<>();

	@Override
	public String toString() {
		return "DungeonBuilderASP{" +
				"gridWidth=" + gridWidth +
				", gridHeight=" + gridHeight +
				", startX=" + startX +
				", startY=" + startY +
				", exitX=" + exitX +
				", exitY=" + exitY +
				", minExitPathLength=" + minExitPathLength +
				", totalAmountOfDoorsMin=" + totalAmountOfDoorsMin +
				", totalAmountOfDoorsMax=" + totalAmountOfDoorsMax +
				", predefinedDoors=" + predefinedDoors +
				", predefinedWalls=" + predefinedWalls +
				'}';
	}

	@Override
	public DTODungeonResult build() throws DungeonGenerationException {
		String aspSource = new DungeonBuilderASPWriter(this).generateASPCode();
		File aspOutFile = new File("ASP_source.lp");
		try {
			FileUtils.write(aspOutFile, aspSource, "UTF-8");
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new DungeonGenerationException("Could not write ASP File: " + e);
		}

		ShellASPSolver shellASPSolver = new ShellASPSolver(aspSource);
		Result aspResult = null;
		try {
			long before = System.currentTimeMillis();
			aspResult = shellASPSolver.solve(true);
			long duration = System.currentTimeMillis() - before;
			Log.info("Dungeon generation with ASP solver took: " + duration + "ms (solution " + aspResult.getSolutionNumber() + " taken)");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			throw new DungeonGenerationException("Could not execute level generation with ASP: " + e.getMessage());
		}
		if (aspResult != null && aspResult.hasModel()) {
			LevelDTO dungeon = new DungeonBuilderASPReader(this).createDungeon(aspResult);
			dungeon.setStartPosition(new JDPoint(this.startX, startY));
			return new DTODungeonResult(dungeon, this.toString());
		}
		if (aspResult != null) {
			Log.warning(aspResult.getFullOutput());
		}
		throw new DungeonGenerationException("No solvable dungeon found for specification");
	}

	@Override
	public DungeonBuilder gridSize(int width, int height) {
		gridWidth = width;
		gridHeight = height;
		return this;
	}

	@Override
	public DungeonBuilder setStartingPoint(StartLocationBuilder start) {
		this.startLocation = start;
		this.locations.put(start.getIdentifier(), start);
		if (start.hasFixedRoomPosition()) {
			this.startX = start.getRoomPosition().getX();
			this.startY = start.getRoomPosition().getY();
		}
		return this;
	}

	@Override
	public DungeonBuilder setMaxDeadEnds(int maxDeadEnds) {
		this.maxDeadEnds = maxDeadEnds;
		return this;
	}

	@Override
	public DungeonBuilder addLocation(LocatedEntityBuilder location) {
		this.locations.put(location.getIdentifier(), location);
		return this;
	}

	@Override
	public DungeonBuilder addLocations(Collection<LocatedEntityBuilder> locations) {
		locations.forEach(location -> {
			this.locations.put(location.getIdentifier(), location);
		});
		return this;
	}

	@Override
	public DungeonBuilder addKey(KeyBuilder key) {
		// add to keys so the ASPs solver can find a suitable door for the lock
		this.keys.add(key);
		// KeyBuilder is also a LocationBuilder for the key location
		this.locations.put(key.getIdentifier(), key);
		return this;
	}

	@Override
	public DungeonBuilder addLocationsShortestDistanceExactlyConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int distance) {
		this.locationsShortestDistanceExactlyConstraints.add(new LocationsLeastDistanceConstraint(locationA, locationB, distance));
		return this;
	}

	@Override
	public DungeonBuilder addLocationsShortestDistanceAtLeastConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int distance) {
		this.locationsShortestDistanceAtLeastConstraints.add(new LocationsLeastDistanceConstraint(locationA, locationB, distance));
		return this;
	}

	@Override
	public DungeonBuilder setMinExitPathLength(int pathLength) {
		this.minExitPathLength = pathLength;
		return this;
	}

	@Deprecated // use HallBuilder
	@Override
	public DungeonBuilder addHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height, boolean setAllInternalDoors) {
		DefaultDoorSpecification hall = DungeonBuilderASPUtils.createHall(upperLeftCornerX, upperLeftCornerY, width, height, setAllInternalDoors);
		this.addPredefinedWalls(hall.getWalls());
		this.addPredefinedDoors(hall.getDoors());
		return this;
	}

	@Deprecated // use HallBuilder
	@Override
	public DungeonBuilder addHall(int upperLeftCornerX, int upperLeftCornerY, int width, int height) {
		return addHall(upperLeftCornerX, upperLeftCornerY, width, height, false);
	}

	@Override
	public DungeonBuilder setMinAmountOfDoors(int numberOfDoors) {
		this.totalAmountOfDoorsMin = numberOfDoors;
		return this;
	}

	@Override
	public DungeonBuilder setMaxAmountOfDoors(int numberOfDoors) {
		this.totalAmountOfDoorsMax = numberOfDoors;
		return this;
	}

	@Override
	public DungeonBuilder addPredefinedDoor(int x1, int y1, int x2, int y2) {
		this.predefinedDoors.add(new DoorMarker(x1, y1, x2, y2));
		return this;
	}

	@Override
	public DungeonBuilder addPredefinedDoors(Collection<DoorMarker> doors) {
		this.predefinedDoors.addAll(doors);
		return this;
	}

	@Override
	public DungeonBuilder addPredefinedWall(int x1, int y1, int x2, int y2) {
		this.predefinedWalls.add(new DoorMarker(x1, y1, x2, y2));
		return this;
	}

	@Override
	public DungeonBuilder addPredefinedWalls(Collection<DoorMarker> doors) {
		this.predefinedWalls.addAll(doors);
		return this;
	}

	@Override
	public boolean hasSolution() {
		return false;
	}

	@Override
	public DungeonBuilder addDoorSpecification(DoorSpecification hall) {
		this.addPredefinedDoors(hall.getDoors());
		this.addPredefinedWalls(hall.getWalls());
		return this;
	}
}
