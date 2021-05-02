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
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

	Collection<LocationBuilder> locations = new HashSet<>();
	LocationBuilder startLocation;
	Collection<LocationsLeastDistanceConstraint> locationsLeastDistanceConstraints = new HashSet<>();

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
		String aspSource = this.generateASPCode();
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
			return new DungeonResultASP(createDungeon(aspResult), new JDPoint(this.startX, startY), this.toString());
		}
		if (aspResult != null) {
			Log.warning(aspResult.getFullOutput());
		}
		throw new DungeonGenerationException("No solvable dungeon found for specification");
	}

	private Dungeon createDungeon(Result aspResult) {
		final Dungeon dungeon = new Dungeon(this.gridWidth, this.gridHeight);

		// set exit
		//dungeon.getRoom(this.exitX, this.exitY).setLocation(new LevelExit());

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

		Room startRoom = dungeon.getRoom(this.startX, this.startY);
		startRoom.addItem(new VisibilityCheatBall());

		Stream<Fact> locationFactStream = aspResult.getFacts()
				.stream()
				.filter(fact -> fact.getPredicate().equals(LOCATION_PREDICATE));

		locationFactStream.forEach(locationFact -> {
			Fact.Literal locationClazzName = locationFact.get(0);
			Fact roomPosFact = locationFact.getFacts()[0];
			int posX = roomPosFact.get(0).asNumber();
			int posY = roomPosFact.get(1).asNumber();
			try {
				Class<?> locationClazz = Class.forName("de.jdungeon.location." + locationClazzName.asString());
				Object newLocationInstance = null;

				Constructor<?>[] constructors = locationClazz.getConstructors();
				Room room = dungeon.getRoom(posX, posY);

				for (Constructor<?> constructor : constructors) {
					// use the standard constructor if available
					if (constructor.getParameterCount() == 0) {
						newLocationInstance = constructor.newInstance();
						break;
					}
					if (constructor.getParameterCount() == 1) {
						// use the room constructor if applicable
						if (constructor.getParameterTypes()[0].equals(Room.class)) {
							if (room.getLocation() != null) {
								Log.severe("Room already has a location! :" + room.toString() + " (" + room.getLocation() + ")");
							}
							newLocationInstance = constructor.newInstance(room);
							break;
						}
					}
				}

				room.setLocation((Location) newLocationInstance);
			}
			catch (ClassNotFoundException e) {
				Log.severe("Could not find location class for name: " + locationClazzName.asString());
				e.printStackTrace();
			}
			catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				Log.severe("Could not find/execute constructor for location class: " + locationClazzName);
				e.printStackTrace();
			}
		});

		return dungeon;
	}

	private JDPoint getPoint(Fact roomFact) {
		Fact.Literal literalX = roomFact.get(0);
		Fact.Literal literalY = roomFact.get(1);
		return new JDPoint(Integer.parseInt(literalX.asString()), Integer.parseInt(literalY.asString()));
	}

	@Override
	public DungeonBuilder gridSize(int width, int height) {
		gridWidth = width;
		gridHeight = height;
		return this;
	}

	@Override
	public DungeonBuilder setStartingPoint(LocationBuilder start) {
		this.startLocation = start;
		this.locations.add(start);
		if (start.hasFixedRoomPosition()) {
			this.startX = start.getRoomPosition().getX();
			this.startY = start.getRoomPosition().getY();
		}
		return this;
	}

	@Override
	public DungeonBuilder addLocation(LocationBuilder location) {
		this.locations.add(location);
		return this;
	}

	@Override
	public DungeonBuilder addLocationsLeastDistanceConstraint(LocationBuilder locationA, LocationBuilder locationB, int distance) {
		this.locationsLeastDistanceConstraints.add(new LocationsLeastDistanceConstraint(locationA, locationB, distance));
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
		DefaultDoorSpecification hall = DungeonGeneratorASPUtils.createHall(upperLeftCornerX, upperLeftCornerY, width, height, setAllInternalDoors);
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
	private static final String LOCATION_PREDICATE = "locationPosition";
	private static final String ROOM_PREDICATE = "room";

	private String generateASPCode() {
		Map<String, String> valueReplacements = new HashMap<>();
		valueReplacements.put(GRID_WIDTH, "" + (this.gridWidth - 1));
		valueReplacements.put(GRID_HEIGHT, "" + (this.gridHeight - 1));
		valueReplacements.put(START_X, "" + this.startX);
		valueReplacements.put(START_Y, "" + this.startY);
		valueReplacements.put(EXIT_X, "" + this.exitX);
		valueReplacements.put(EXIT_Y, "" + this.exitY);
		int doorsMin = 1;
		int doorsMax = 100;
		if (this.totalAmountOfDoorsMin > 0) {
			doorsMin = totalAmountOfDoorsMin;
			doorsMax = 2 * doorsMin;
		}
		else if (totalAmountOfDoorsMax > 0) {
			doorsMax = totalAmountOfDoorsMax;
			doorsMin = totalAmountOfDoorsMax / 2;
		}
		valueReplacements.put(DOORS_AMOUNT_MIN, "" + doorsMin);
		valueReplacements.put(DOORS_AMOUNT_MAX, "" + doorsMax);
		valueReplacements.put(PREDEFINED_DOORS, "" + generatePredefinedDoorsASPCode(this.predefinedDoors, false));
		valueReplacements.put(PREDEFINED_WALLS, "" + generatePredefinedDoorsASPCode(this.predefinedWalls, true));
		//valueReplacements.put(SHORTEST_EXIT_PATH, minExitPathLength > 0 ? "" + generateShortPathRestrictionsASPCode(this.minExitPathLength) : "");
		StringSubstitutor substitutor = new StringSubstitutor(valueReplacements);
		String templateString = null;
		//File templateFile = new File("ASP_dungeon_generation_template2.lp");
		String templateFilename = "ASP_dungeon_generation_template2.lp";
		InputStream resourceAsStream = this.getClass()
				.getClassLoader()
				.getResourceAsStream(templateFilename);
		//String content = IOUtils.read(resourceAsStream, "UTF-8");
		try {
			templateString = IOUtils.toString(resourceAsStream, "UTF-8");
			//templateString = FileUtils.readFileToString(templateFile, "UTF-8");
		}
		catch (IOException e) {
			Log.severe("Could not read ASP template file: " + templateFilename);
			e.printStackTrace();
		}

		StringBuilder aspSourceBuffy = new StringBuilder();
		aspSourceBuffy.append(substitutor.replace(templateString));

		// this seems to be faster than constraining each location individually (while grounding is larger)
		aspSourceBuffy.append("% each location has to be reachable from the start point\n");
		aspSourceBuffy.append(":- not reachable(START, POS) , locationPosition(LOC, POS), location(LOC), locationPosition(\"" + startLocation
				.getSimpleName() + "\" ,  START) .\n\n");

		// insert reachability rules
		String[] doorCoordPairs = { "MID_X=TO_X, MID_Y+1=TO_Y", "MID_X=TO_X, MID_Y-1=TO_Y", "MID_X+1=TO_X, MID_Y=TO_Y", "MID_X-1=TO_X, MID_Y=TO_Y" };
		aspSourceBuffy.append("%transitive reachability using doors\n");
		for (String doorCoordPair : doorCoordPairs) {
			aspSourceBuffy.append("reachable(" + this.startLocation.getSimpleName()
					.toLowerCase() + "Pos, room(TO_X, TO_Y)) :- reachable(" + this.startLocation
					.getSimpleName()
					.toLowerCase() + "Pos, room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), " + doorCoordPair + " .\n");
		}
		aspSourceBuffy.append("\n");
		/*
		%%
% door north
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y+1=TO_Y.

% door south
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X=TO_X, MID_Y-1=TO_Y.

% door west
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X+1=TO_X, MID_Y=TO_Y.

% door east
reachable(room(FROM_X, FROM_Y), room(TO_X, TO_Y)) :- reachable(room(FROM_X, FROM_Y), room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), MID_X-1=TO_X, MID_Y=TO_Y.

		 */

		// setting starting stuff
		String startPosName = this.startLocation.getSimpleName().toLowerCase()+"Pos";
		aspSourceBuffy.append("% start is the reachable starting reference point\n");
		aspSourceBuffy.append("reachable("+startPosName+", TO) :- door("+startPosName+", TO) .\n\n");

		// all rooms with doors must be reachable
		aspSourceBuffy.append("% we MUST not have doors to some point that is not reachable from start\n");
		String[] doorCoordPairs2 = { "FROM_X+1=TO_X, FROM_Y=TO_Y", "FROM_X-1=TO_X, FROM_Y=TO_Y", "FROM_X=TO_X, FROM_Y+1=TO_Y", "FROM_X=TO_X, FROM_Y-1=TO_Y" };
		for (String doorCoordPair : doorCoordPairs2) {
			aspSourceBuffy.append(":- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), " + doorCoordPair + ", not reachable(" + this.startLocation
					.getSimpleName()
					.toLowerCase() + "Pos, room(TO_X, TO_Y))  .\n");
		}
		/*

:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), 	,			locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .
:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), 	,		locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .
:- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), 	,		locationPosition(start ,  START), 	not reachable(START, room(TO_X, TO_Y))  .

		 */

		aspSourceBuffy.append("% every room with a door must be reachable from start\n");
		aspSourceBuffy.append(":- door(FROM, TO), not reachable(STARTLOCA, TO) , locationPosition(\"" + startLocation.getSimpleName() + "\" ,  STARTLOCA).\n\n");

		// limit dead ends
		aspSourceBuffy.append("% limit amount of rooms with one door\n");
		aspSourceBuffy.append("oneDoors(room(X1,Y1)) :- 1{room(X2,Y2) : door(room(X1,Y1), room(X2,Y2))} 1 , room(X1,Y1) .\n");
		aspSourceBuffy.append("S < 6 :- S = #sum{1,R : oneDoors(R) } .\n\n");

		// insert paths optimization (to have more of a network)
		aspSourceBuffy.append("%% minimize adjacent rooms with 2 doors each\n");
		aspSourceBuffy.append("pathway(room(X1,Y1), room(X2,Y2)) :- door(room(X1,Y1), room(X2,Y2)) , twoDoors(room(X1,Y1)), twoDoors(room(X2,Y2)) .\n");
		aspSourceBuffy.append("twoDoors(room(X1,Y1)) :- 2{room(X2,Y2) : door(room(X1,Y1), room(X2,Y2))} 2 , room(X1,Y1) .\n");
		aspSourceBuffy.append("#minimize{1, R1, R2 : pathway(R1, R2) } .\n\n");

		// insert locations code (this will also set the fixed starting point if existing)
		appendLocationsASPCode(aspSourceBuffy);
		aspSourceBuffy.append("\n\n");

		// insert locations distance constraints code
		appendLocationMinDistanceConstraints(aspSourceBuffy);
		aspSourceBuffy.append("\n\n");

		return aspSourceBuffy.toString();
	}

	private void appendLocationsASPCode(StringBuilder buffy) {
		if (locations.size() > 0) {
			buffy.append("\n%%Additional Locations\n");
		}
		locations.forEach(location -> {
			String locationName = location.getClazz().getSimpleName();
			buffy.append("location(\"" + locationName + "\") .\n");
			if (location.hasFixedRoomPosition()) {
				JDPoint locationPos = location.getRoomPosition();
				String locationPosName = locationName.toLowerCase() + "Pos";
				buffy.append("#const " + locationPosName + " = room(" + locationPos.getX() + "," + locationPos
						.getY() + ") .\n");
				buffy.append("locationPosition(\"" + locationName + "\", " + locationName.toLowerCase() + "Pos) .\n");
				if (!location.equals(this.startLocation)) {
					buffy.append(":- not reachable(" + this.startLocation.getSimpleName()
							.toLowerCase() + "Pos, " + locationPosName + ") . \n\n ");
				}
				else {
					buffy.append("\n");
				}
			}
		});
	}

	private void appendLocationMinDistanceConstraints(StringBuilder buffy) {
		if (locationsLeastDistanceConstraints.size() > 0) {
			buffy.append("\n%%Locations Min Distance Constraints\n");
		}
		locationsLeastDistanceConstraints.forEach(lldc -> {
			String locationNameA = lldc.getLocationA().getClazz().getSimpleName();
			String locationNameAConstant = locationNameA.toLowerCase() + "Pos";
			String locationNameB = lldc.getLocationB().getClazz().getSimpleName();
			String locationNameBConstant = locationNameB.toLowerCase() + "Pos";
			int minDistance = lldc.getMinDistance();
			// we start a distance breadth first spreading dist a location A
			buffy.append("dist(" + locationNameAConstant + "," + locationNameAConstant + ", 0) .\n");
			buffy.append(":-not dist(" + locationNameAConstant + ", " + locationNameBConstant + ", " + minDistance + ").\n");
			for (int i = 1; i < minDistance; i++) {
				buffy.append(":- dist(" + locationNameAConstant + ", " + locationNameBConstant + ", " + i + ") .\n");
			}
		});

		/*
		%:-not dist(START, EXITPOS, 10) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 1) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 2) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 3) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 4) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 5) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 6) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 7) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 8) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .
%:- dist(START, EXITPOS, 9) , locationPosition("EXIT" ,  EXITPOS) , locationPosition(start ,  START) .



%% Search start distance
dist(START, START, 0) :- locationPosition(start ,  START)  .
		 */
	}

	private String generatePredefinedDoorsASPCode(Collection<DoorMarker> predefinedDoors, boolean isWall) {
		StringBuilder buffy = new StringBuilder();
		for (DoorMarker predefinedDoor : predefinedDoors) {
			if (isWall) buffy.append("not ");
			buffy.append("door(room(" + predefinedDoor.x1 + "," + predefinedDoor.y1 + "), room(" + predefinedDoor.x2 + "," + predefinedDoor.y2 + ")) .\n");
		}
		return buffy.toString();
	}

	private String generateShortPathRestrictionsASPCode(int minExitPathLength) {
		StringBuilder buffy = new StringBuilder();
		buffy.append(":-not dist(START, EXIT, " + minExitPathLength + "), locationPosition(\"EXIT\" ,  EXIT) , locationPosition(start ,  START).\n");
		for (int i = 1; i < minExitPathLength; i++) {
			buffy.append(":- dist(START, EXIT, " + i + ") , locationPosition(\"EXIT\" ,  EXIT) , locationPosition(start ,  START) .\n");
		}
		return buffy.toString();
	}
}
