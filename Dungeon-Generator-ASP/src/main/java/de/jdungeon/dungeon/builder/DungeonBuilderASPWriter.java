package de.jdungeon.dungeon.builder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.log.Log;

public class DungeonBuilderASPWriter {

	static final String GRID_WIDTH = "GRID_WIDTH";
	static final String GRID_HEIGHT = "GRID_HEIGHT";
	static final String START_X = "START_X";
	static final String START_Y = "START_Y";
	static final String EXIT_X = "EXIT_X";
	static final String EXIT_Y = "EXIT_Y";
	static final String DOORS_AMOUNT_MIN = "DOORS_AMOUNT_MIN";
	static final String DOORS_AMOUNT_MAX = "DOORS_AMOUNT_MAX";
	static final String PREDEFINED_DOORS = "PREDEFINED_DOORS";
	static final String PREDEFINED_WALLS = "PREDEFINED_WALLS";
	static final String DOOR_PREDICATE = "printDoor";
	static final String LOCKED_PREDICATE = "locked";
	static final String LOCATION_PREDICATE = "locationPosition";

	private DungeonBuilderASP builder;

	public DungeonBuilderASPWriter(DungeonBuilderASP builder) {
		this.builder = builder;
	}

	public String generateASPCode() {
		Map<String, String> valueReplacements = new HashMap<>();
		valueReplacements.put(GRID_WIDTH, "" + (builder.gridWidth - 1));
		valueReplacements.put(GRID_HEIGHT, "" + (builder.gridHeight - 1));
		valueReplacements.put(START_X, "" + builder.startX);
		valueReplacements.put(START_Y, "" + builder.startY);
		valueReplacements.put(EXIT_X, "" + builder.exitX);
		valueReplacements.put(EXIT_Y, "" + builder.exitY);
		int doorsMin = 1;
		int doorsMax = 100;
		if (builder.totalAmountOfDoorsMin > 0 && builder.totalAmountOfDoorsMax > 0) {
			doorsMin = builder.totalAmountOfDoorsMin;
			doorsMax = builder.totalAmountOfDoorsMax;
		}
		else if (builder.totalAmountOfDoorsMin > 0) {
			doorsMin = builder.totalAmountOfDoorsMin;
			doorsMax = 2 * doorsMin;
		}
		else if (builder.totalAmountOfDoorsMax > 0) {
			doorsMax = builder.totalAmountOfDoorsMax;
			doorsMin = builder.totalAmountOfDoorsMax / 2;
		}
		valueReplacements.put(DOORS_AMOUNT_MIN, "" + doorsMin);
		valueReplacements.put(DOORS_AMOUNT_MAX, "" + doorsMax);
		valueReplacements.put(PREDEFINED_DOORS, "" + generatePredefinedDoorsASPCode(builder.predefinedDoors, false));
		valueReplacements.put(PREDEFINED_WALLS, "" + generatePredefinedDoorsASPCode(builder.predefinedWalls, true));
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
		aspSourceBuffy.append(":- not reachable(START, POS) , locationPosition(LOC, POS), location(LOC), locationPosition(\"" + builder.startLocation
				.getIdentifier() + "\" ,  START) .\n\n");

		// insert reachability rules
		String[] doorCoordPairs = { "MID_X=TO_X, MID_Y+1=TO_Y", "MID_X=TO_X, MID_Y-1=TO_Y", "MID_X+1=TO_X, MID_Y=TO_Y", "MID_X-1=TO_X, MID_Y=TO_Y" };
		aspSourceBuffy.append("%transitive reachability using doors\n");
		for (String doorCoordPair : doorCoordPairs) {
			aspSourceBuffy.append("reachable(" + builder.startLocation.getIdentifier()
					.toLowerCase() + "Pos, room(TO_X, TO_Y)) :- reachable(" + builder.startLocation
					.getIdentifier()
					.toLowerCase() + "Pos, room(MID_X, MID_Y)), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), " + doorCoordPair + " .\n");
		}
		aspSourceBuffy.append("\n");

		// setting starting stuff
		String startPosName = builder.startLocation.getIdentifier().toLowerCase() + "Pos";
		aspSourceBuffy.append("% start is the reachable starting reference point\n");
		aspSourceBuffy.append("reachable(" + startPosName + ", TO) :- door(" + startPosName + ", TO) .\n\n");

		// all rooms with doors must be reachable
		aspSourceBuffy.append("% we MUST not have doors to some point that is not reachable from start\n");
		String[] doorCoordPairs2 = { "FROM_X+1=TO_X, FROM_Y=TO_Y", "FROM_X-1=TO_X, FROM_Y=TO_Y", "FROM_X=TO_X, FROM_Y+1=TO_Y", "FROM_X=TO_X, FROM_Y-1=TO_Y" };
		for (String doorCoordPair : doorCoordPairs2) {
			aspSourceBuffy.append(":- door(room(FROM_X, FROM_Y), room(TO_X, TO_Y)), " + doorCoordPair + ", not reachable(" + builder.startLocation
					.getIdentifier()
					.toLowerCase() + "Pos, room(TO_X, TO_Y))  .\n");
		}

		aspSourceBuffy.append("% every room with a door must be reachable from start\n");
		aspSourceBuffy.append(":- door(FROM, TO), not reachable(STARTLOCA, TO) , locationPosition(\"" + builder.startLocation
				.getIdentifier() + "\" ,  STARTLOCA).\n\n");

		// limit dead ends
		aspSourceBuffy.append("% limit amount of rooms with one door\n");
		aspSourceBuffy.append("oneDoors(room(X1,Y1)) :- 1{room(X2,Y2) : door(room(X1,Y1), room(X2,Y2))} 1 , room(X1,Y1) .\n");
		aspSourceBuffy.append("S <= " + builder.maxDeadEnds + " :- S = #sum{1,R : oneDoors(R) } .\n\n");

		// insert paths optimization (to have more of a network)
		aspSourceBuffy.append("%% minimize adjacent rooms with 2 doors each\n");
		aspSourceBuffy.append("pathway(room(X1,Y1), room(X2,Y2)) :- door(room(X1,Y1), room(X2,Y2)) , twoDoors(room(X1,Y1)), twoDoors(room(X2,Y2)) .\n");
		aspSourceBuffy.append("twoDoors(room(X1,Y1)) :- 2{room(X2,Y2) : door(room(X1,Y1), room(X2,Y2))} 2 , room(X1,Y1) .\n");
		aspSourceBuffy.append("#minimize{1, R1, R2 : pathway(R1, R2) } .\n\n");

		// insert locations code (this will also set the fixed starting point if existing)
		appendLocationsASPCode(aspSourceBuffy);
		aspSourceBuffy.append("\n\n");

		// insert locations shortest path exactly constraints code
		appendLocationShortestPathExactlyConstraints(aspSourceBuffy);
		aspSourceBuffy.append("\n\n");

		// insert locations shortest path at least constraints code
		appendLocationShortestPathAtLeastConstraints(aspSourceBuffy);
		aspSourceBuffy.append("\n\n");

		// insert keys
		appendKeysASPCode(aspSourceBuffy);
		aspSourceBuffy.append("\n\n");

		return aspSourceBuffy.toString();
	}

	private void appendKeysASPCode(StringBuilder buffy) {
		buffy.append("\n%% Keys/Locks in general\n");
		buffy.append("%% General key related code\n");
		buffy.append("% For each key there must be exactly one door to hold the lock of this key\n");
		buffy.append("1{lock(door(room(X1, Y1), room(X2, Y2)), K) : key(K), door(room(X1, Y1), room(X2, Y2)), room(X1, Y1), room(X2, Y2) }   1 :- key(K) .\n");
		buffy.append("% The door holding the lock is locked\n");
		buffy.append("locked(door(room(X2, Y2), room(X1, Y1)), K) :- lock(door(room(X1, Y1), room(X2, Y2)), K) .\n");
		buffy.append("% The inverse door is also locked (we modelled both directions separately here in ASP)\n");
		buffy.append("locked(door(room(X2, Y2), room(X1, Y1)), K) :- locked(door(room(X1, Y1), room(X2, Y2)), K) .\n");
		buffy.append("% The lock can not be where no door exists\n");
		buffy.append(":- locked(door(room(X2, Y2), room(X1, Y1)), K), not door(room(X2, Y2), room(X1, Y1)) .\n\n");

		if (builder.keys.size() > 0) {
			String startLocationName = builder.startLocation.getIdentifier().toLowerCase() + "Pos";

			buffy.append("%% Reachability from starting point\n");
			buffy.append("%% Initiate reachability tracing with keys/locks\n");
			buffy.append("reachableWithoutKey(" + startLocationName + ", TO, KEY) :- door(" + startLocationName + ", TO), not locked(door(" + startLocationName + ", TO), KEY), key(KEY) .\n");
			buffy.append("%% Conduct reachability tracing with keys/locks\n");
			buffy.append("reachableWithoutKey(" + startLocationName + ", room(TO_X, TO_Y), KEY) :- reachableWithoutKey(" + startLocationName + ", room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X=TO_X, MID_Y+1=TO_Y .");
			buffy.append("reachableWithoutKey(" + startLocationName + ", room(TO_X, TO_Y), KEY) :- reachableWithoutKey(" + startLocationName + ", room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X=TO_X, MID_Y-1=TO_Y .");
			buffy.append("reachableWithoutKey(" + startLocationName + ", room(TO_X, TO_Y), KEY) :- reachableWithoutKey(" + startLocationName + ", room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X+1=TO_X, MID_Y=TO_Y .");
			buffy.append("reachableWithoutKey(" + startLocationName + ", room(TO_X, TO_Y), KEY) :- reachableWithoutKey(" + startLocationName + ", room(MID_X, MID_Y), KEY), door(room(MID_X, MID_Y), room(TO_X, TO_Y)), not locked(door(room(MID_X, MID_Y), room(TO_X, TO_Y)), KEY), key(KEY), MID_X-1=TO_X, MID_Y=TO_Y .");

			builder.keys.forEach(key -> {
				buffy.append("\n% Define key\n");
				buffy.append("key(\"" + key.getKeyString() + "\") . \n");

				// without-key-non-reachable locations
				key.getLocationsNotReachableWithoutKey().forEach(locationNotReachable -> {
					buffy.append("\n% It may not happen that the location is reachable-without-key from start\n");
					buffy.append(":- reachableWithoutKey(" + startLocationName + "," + locationNotReachable.getIdentifier()
							.toLowerCase() + "Pos" + ", \"" + key.getKeyString() + "\") .\n");
				});

				// without-key-reachable locations
				key.getLocationsReachableWithoutKey().forEach(locationReachable -> {

					if (locationReachable instanceof KeyBuilder /*&& !locationReachable.hasFixedRoomPosition()*/) {
						buffy.append("% It may not happen that the key location is _not_ reachable-without-key from start\n");

						//1{ locationPosition("Key", KEYPOS) : KEYPOS != revealmapshrinePos, oneDoors(KEYPOS), reachableWithoutKey(revealmapshrinePos, KEYPOS, "Schraubenschluessel")} 1 .
						// the key needs to be put at some place that is reachable with out it and that is not equal to the starting point and that only has one door
						buffy.append("1{ locationPosition(\"" + key.getIdentifier() + "\", KEYPOS) : KEYPOS != " + startLocationName + ", oneDoors(KEYPOS), reachableWithoutKey(" + startLocationName + ", KEYPOS, \"" + key
								.getKeyString() + "\")} 1 . \n");

						DoorMarker lockDoor = ((KeyBuilder) locationReachable).getLockDoor();
						if (lockDoor != null) {

							// set lock position if predefined
							buffy.append("% Set predefined lock positions\n");
							// lock(door(room(X1, Y1), room(X2, Y2)), K)
							buffy.append("lock(door(room(" + lockDoor.x1 + ", " + lockDoor.y1 + "), room(" + lockDoor.x2 + ", " + lockDoor.y2 + ")), \"" + ((KeyBuilder) locationReachable)
									.getKeyString() + "\") .");
						}
					}
					else {
						buffy.append("% It may not happen that the location is _not_ reachable-without-key from start\n");
						buffy.append(":- not reachableWithoutKey(" + startLocationName + "," + locationReachable.getIdentifier()
								.toLowerCase() + "Pos" + ", \"" + key.getKeyString() + "\") .\n");
					}
				});
			});
		}
	}

	private void appendLocationsASPCode(StringBuilder buffy) {
		if (builder.locations.size() > 0) {
			buffy.append("\n%%Additional Locations\n");
		}
		builder.locations.values().forEach(location -> {
			String locationName = location.getIdentifier();
			buffy.append("location(\"" + locationName + "\") .\n");
			if (location.hasFixedRoomPosition()) {
				JDPoint locationPos = location.getRoomPosition();
				String locationPosName = locationName.toLowerCase() + "Pos";
				buffy.append("#const " + locationPosName + " = room(" + locationPos.getX() + "," + locationPos
						.getY() + ") .\n");
				buffy.append("locationPosition(\"" + locationName + "\", " + locationName.toLowerCase() + "Pos) .\n");
				if (!location.equals(builder.startLocation)) {
					buffy.append(":- not reachable(" + builder.startLocation.getIdentifier()
							.toLowerCase() + "Pos, " + locationPosName + ") . \n\n ");
				}
				else {
					buffy.append("\n");
				}
			}
			else {
				// we do not have a fixed position location so we consider potential positions if existing
				Set<JDPoint> possiblePositions = location.getPossiblePositions();
				if (possiblePositions != null && !possiblePositions.isEmpty()) {
					// ROOM = room(1,3) ; ROOM = room(5,5) :- locationPosition("de_jdungeon_location_ScoutShrine_87059112_1", ROOM) .
					Iterator<JDPoint> iterator = possiblePositions.iterator();
					while (iterator.hasNext()) {
						JDPoint possiblePosition = iterator.next();
						buffy.append("ROOM = room(" + possiblePosition.getX() + "," + possiblePosition.getY() + ")");
						if (iterator.hasNext()) {
							buffy.append(" ; ");
						}
					}
					buffy.append(" :- locationPosition(\"" + locationName + "\", ROOM) .\n");
				}
			}
		});
	}

	private void appendLocationShortestPathExactlyConstraints(StringBuilder buffy) {
		if (builder.locationsShortestDistanceExactlyConstraints.size() > 0) {
			buffy.append("\n%%Locations Min Distance Constraints\n");
		}
		builder.locationsShortestDistanceExactlyConstraints.forEach(lldc -> {
			String locationNameA = lldc.getLocationA().getIdentifier();
			String locationNameAConstant = locationNameA.toLowerCase() + "Pos";
			String locationNameB = lldc.getLocationB().getIdentifier();
			String locationNameBConstant = locationNameB.toLowerCase() + "Pos";
			int minDistance = lldc.getMinDistance();
			buffy.append("\n% We need to have a path of exactly that length\n");
			buffy.append(":-not dist(" + locationNameAConstant + ", " + locationNameBConstant + ", " + minDistance + ").\n");
			appendLocationShortestPathAtLeastConstraints(buffy, lldc, minDistance);
		});
	}

	private void appendLocationShortestPathAtLeastConstraints(StringBuilder buffy) {
		if (builder.locationsShortestDistanceAtLeastConstraints.size() > 0) {
			buffy.append("\n%%Locations shortest distance at least Constraints\n");
		}
		builder.locationsShortestDistanceAtLeastConstraints.forEach(lldc -> {
			appendLocationShortestPathAtLeastConstraints(buffy, lldc, lldc.getMinDistance());
		});
	}

	private void appendLocationShortestPathAtLeastConstraints(StringBuilder buffy, LocationsLeastDistanceConstraint lldc, int minDistance) {
		String locationNameA = lldc.getLocationA().getIdentifier();
		String locationNameAConstant = locationNameA.toLowerCase() + "Pos";
		String locationNameB = lldc.getLocationB().getIdentifier();
		String locationNameBConstant = locationNameB.toLowerCase() + "Pos";

		boolean targetHasFixedDistance = lldc.getLocationB().hasFixedRoomPosition();

		buffy.append("\n% Forbidding shortest paths than min distance\n");
		// we start a distance breadth first spreading dist a location A

		buffy.append("dist(" + locationNameAConstant + "," + locationNameAConstant + ", 0) .\n");

		for (int i = 1; i < minDistance; i++) {
			if (targetHasFixedDistance) {
				buffy.append(":- dist(" + locationNameAConstant + ", " + locationNameBConstant + ", " + i + ") .\n");
			}
			else {
				// target position is not predefined
				buffy.append(":- dist(" + locationNameAConstant + ", TARGETPOS, " + i + ") , locationPosition(\"" + locationNameB + "\", TARGETPOS) .\n");
			}
		}
	}

	private static String generatePredefinedDoorsASPCode(Collection<DoorMarker> predefinedDoors, boolean isWall) {
		StringBuilder buffy = new StringBuilder();
		for (DoorMarker predefinedDoor : predefinedDoors) {
			if (predefinedDoor == null) continue;
			if (isWall) buffy.append("not ");
			buffy.append("door(room(" + predefinedDoor.x1 + "," + predefinedDoor.y1 + "), room(" + predefinedDoor.x2 + "," + predefinedDoor.y2 + ")) .\n");
			if (predefinedDoor.getKeyString() != null) {
				// lock(door(room(X1, Y1), room(X2, Y2)), K)
				buffy.append("lock(door(room(" + predefinedDoor.x1 + "," + predefinedDoor.y1 + "), room(" + predefinedDoor.x2 + "," + predefinedDoor.y2 + ")), \"" + predefinedDoor.getKeyString() + "\") .\n");
				buffy.append("key(\"" + predefinedDoor.getKeyString() + "\").\n");
			}
		}
		return buffy.toString();
	}
}
