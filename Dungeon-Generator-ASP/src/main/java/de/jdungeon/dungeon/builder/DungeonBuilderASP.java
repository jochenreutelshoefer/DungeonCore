package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.asp.Fact;
import de.jdungeon.dungeon.builder.asp.Result;
import de.jdungeon.dungeon.builder.asp.ShellASPSolver;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.item.VisibilityCheatBall;
import de.jdungeon.location.LevelExit;
import de.jdungeon.log.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

public class DungeonBuilderASP implements DungeonBuilder<DungeonResultASP> {

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
	protected int totalAmountOfDoorsMin = -1;
	protected int totalAmountOfDoorsMax = -1;

	protected Collection<DoorMarker> predefinedDoors = new HashSet<>();
	protected Collection<DoorMarker> predefinedWalls = new HashSet<>();

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
	public DungeonResultASP build() throws DungeonGenerationException {
		String aspSource = this.generateASPSource();
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
			Log.info("Dungeon generation with ASP solver took: " + duration + "ms");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			throw new DungeonGenerationException("Could not execute level generation with ASP: " + e.getMessage());
		}
		if (aspResult != null && aspResult.hasModel()) {
			return new DungeonResultASP(createDungeon(aspResult), new JDPoint(this.startX - 1, startY - 1), this.toString());
		}
		if (aspResult != null) {
			Log.warning(aspResult.getFullOutput());
		}
		throw new DungeonGenerationException("No solvable dungeon found for specification");
	}

	private Dungeon createDungeon(Result aspResult) {
		final Dungeon dungeon = new Dungeon(this.gridWidth, this.gridHeight);

		// set exit
		dungeon.getRoom(this.exitX - 1, this.exitY - 1).setLocation(new LevelExit());

		Stream<Fact> doorFactStream = aspResult.getFacts()
				.stream()
				.filter(fact -> fact.getPredicate().equals(DOOR_PREDICATE));

		// insert each single door into dungeon
		doorFactStream.forEach(fact -> {
			JDPoint point1 = getPoint(fact.getFacts()[0]);
			JDPoint point2 = getPoint(fact.getFacts()[1]);
			Room room1 = dungeon.getRoom(point1);
			Room room2 = dungeon.getRoom(point2);
			RouteInstruction.Direction direction = RouteInstruction.Direction.fromPoints(point1, point2);
			room1.setDoor(new Door(room1, room2), direction, true);
		});

		Room startRoom = dungeon.getRoom(this.startX - 1, this.startY - 1);
		startRoom.addItem(new VisibilityCheatBall());

		return dungeon;
	}

	private JDPoint getPoint(Fact roomFact) {
		Fact.Literal literalX = roomFact.get(0);
		Fact.Literal literalY = roomFact.get(1);
		// we need -1 because ASP starts indexing with 1 while Dungeon starts indexing with zero
		return new JDPoint(Integer.parseInt(literalX.asString()) - 1, Integer.parseInt(literalY.asString()) - 1);
	}

	@Override
	public DungeonBuilder gridSize(int width, int height) {
		gridWidth = width;
		gridHeight = height;
		return this;
	}

	@Override
	public DungeonBuilder setStartingPoint(int x, int y) {
		this.startX = x;
		this.startY = y;
		return this;
	}

	@Override
	public DungeonBuilder setExitPoint(int x, int y) {
		this.exitX = x;
		this.exitY = y;
		return this;
	}

	@Override
	public DungeonBuilder setMinExitPathLength(int pathLength) {
		this.minExitPathLength = pathLength;
		return this;
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

	private static final String GRID_WIDTH = "GRID_WIDTH";
	private static final String GRID_HEIGHT = "GRID_HEIGHT";
	private static final String START_X = "START_X";
	private static final String START_Y = "START_Y";
	private static final String EXIT_X = "EXIT_X";
	private static final String EXIT_Y = "EXIT_Y";
	private static final String DOORS_AMOUNT_MIN = "DOORS_AMOUNT_MIN";
	private static final String DOORS_AMOUNT_MAX = "DOORS_AMOUNT_MAX";
	private static final String SHORTEST_EXIT_PATH = "SHORTEST_EXIT_PATH";
	private static final String SHORTER_EXIT_PATHS_CONSTRAINTS = "SHORTER_EXIT_PATHS_CONSTRAINTS";
	private static final String PREDEFINED_DOORS = "PREDEFINED_DOORS";
	private static final String PREDEFINED_WALLS = "PREDEFINED_WALLS";
	private static final String DOOR_PREDICATE = "printDoor";
	private static final String ROOM_PREDICATE = "room";

	private String generateASPSource() {
		Map<String, String> valueReplacements = new HashMap<>();
		valueReplacements.put(GRID_WIDTH, "" + this.gridWidth);
		valueReplacements.put(GRID_HEIGHT, "" + this.gridHeight);
		valueReplacements.put(START_X, "" + this.startX);
		valueReplacements.put(START_Y, "" + this.startY);
		valueReplacements.put(EXIT_X, "" + this.exitX);
		valueReplacements.put(EXIT_Y, "" + this.exitY);
		int doorsMin = 1;
		int doorsMax = 100;
		if (this.totalAmountOfDoorsMin > 0) {
			doorsMin = totalAmountOfDoorsMin;
			doorsMax = 2 * doorsMin;
		} else if(totalAmountOfDoorsMax > 0) {
			doorsMax = totalAmountOfDoorsMax;
			doorsMin = totalAmountOfDoorsMax / 2;
		}
		valueReplacements.put(DOORS_AMOUNT_MIN, "" + doorsMin);
		valueReplacements.put(DOORS_AMOUNT_MAX, "" + doorsMax);
		valueReplacements.put(PREDEFINED_DOORS, "" + generatePredefinedDoors(this.predefinedDoors, false));
		valueReplacements.put(PREDEFINED_WALLS, "" + generatePredefinedDoors(this.predefinedWalls, true));
		valueReplacements.put(SHORTEST_EXIT_PATH, minExitPathLength > 0 ? "" + generateShortPathRestrictions(this.minExitPathLength) : "");
		StringSubstitutor substitutor = new StringSubstitutor(valueReplacements);
		String templateString = null;
		File templateFile = new File("ASP_dungeon_generation_template.lp");
		InputStream resourceAsStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream("ASP_dungeon_generation_template.lp");
		//String content = IOUtils.read(resourceAsStream, "UTF-8");
		try {
			templateString = IOUtils.toString(resourceAsStream, "UTF-8");
			//templateString = FileUtils.readFileToString(templateFile, "UTF-8");
		}
		catch (IOException e) {
			Log.severe("Could not read ASP template file " + templateFile);
			e.printStackTrace();
		}
		return substitutor.replace(templateString);
	}

	private String generatePredefinedDoors(Collection<DoorMarker> predefinedDoors, boolean isWall) {
		StringBuilder buffy = new StringBuilder();
		for (DoorMarker predefinedDoor : predefinedDoors) {
			if (isWall) buffy.append("not ");
			buffy.append("door(room(" + predefinedDoor.x1 + "," + predefinedDoor.y1 + "), room(" + predefinedDoor.x2 + "," + predefinedDoor.y2 + ")) .\n");
		}
		return buffy.toString();
	}

	private String generateShortPathRestrictions(int minExitPathLength) {
		StringBuilder buffy = new StringBuilder();
		buffy.append(":-not dist(start, end, " + minExitPathLength + ") .\n");
		for (int i = 1; i < minExitPathLength; i++) {
			buffy.append(":- dist(start, end, " + i + ") .\n");
		}
		return buffy.toString();
	}
}
