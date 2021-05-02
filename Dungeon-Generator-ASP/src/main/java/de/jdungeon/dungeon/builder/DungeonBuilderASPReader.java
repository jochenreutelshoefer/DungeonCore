package de.jdungeon.dungeon.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.asp.Fact;
import de.jdungeon.dungeon.builder.asp.Result;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.item.Key;
import de.jdungeon.item.VisibilityCheatBall;
import de.jdungeon.location.Location;
import de.jdungeon.log.Log;
import de.jdungeon.util.Pair;

import static de.jdungeon.dungeon.builder.DungeonBuilderASPWriter.*;

/**
 * This helper helps to create the dungeon object model from the ASP solver result data.
 */
public class DungeonBuilderASPReader {

	private DungeonBuilderASP builder;

	public DungeonBuilderASPReader(DungeonBuilderASP dungeonBuilderASP) {
		this.builder = dungeonBuilderASP;
	}

	public Dungeon createDungeon(Result aspResult) {

		final Dungeon dungeon = new Dungeon(builder.gridWidth, builder.gridHeight);

		// set exit
		//dungeon.getRoom(this.exitX, this.exitY).setLocation(new LevelExit());

		Stream<Fact> doorFactStream = aspResult.getFacts()
				.stream()
				.filter(fact -> fact.getPredicate().equals(DOOR_PREDICATE));
		Stream<Fact> lockedFactStream = aspResult.getFacts()
				.stream()
				.filter(fact -> fact.getPredicate().equals(LOCKED_PREDICATE));
		Map<Pair<JDPoint, JDPoint>, String> locksMap = prepareLocks(lockedFactStream);

		// insert each single door into dungeon
		doorFactStream.forEach(fact -> {
			JDPoint point1 = getPoint(fact.getFacts()[0]);
			JDPoint point2 = getPoint(fact.getFacts()[1]);
			Room room1 = dungeon.getRoom(point1);
			Room room2 = dungeon.getRoom(point2);
			RouteInstruction.Direction direction = RouteInstruction.Direction.fromPoints(point1, point2);
			Door door = new Door(room1, room2);
			Pair<JDPoint, JDPoint> pointPair = new Pair<>(point1, point2);
			if (locksMap.containsKey(pointPair)) {
				door.setKey(new Key(locksMap.get(pointPair)));
			}
			room1.setDoor(door, direction, true);
		});

		Room startRoom = dungeon.getRoom(builder.startX, builder.startY);
		startRoom.addItem(new VisibilityCheatBall());

		createLocations(aspResult, dungeon);

		return dungeon;
	}


	private static Map<Pair<JDPoint, JDPoint>, String> prepareLocks(Stream<Fact> lockedFactStream) {
		Map<Pair<JDPoint, JDPoint>, String> result = new HashMap<>();
		lockedFactStream.forEach(lockFact -> {
			// locked(door(room(7,0),room(8,0)),"Key1")
			String keyString = lockFact.get(0).asString();
			Fact doorFact = lockFact.getFacts()[0];
			Fact[] roomsFacts = doorFact.getFacts();
			JDPoint roomA = new JDPoint(roomsFacts[0].get(0).asNumber(), roomsFacts[0].get(1).asNumber());
			JDPoint roomB = new JDPoint(roomsFacts[1].get(0).asNumber(), roomsFacts[1].get(1).asNumber());
			result.put(new Pair<>(roomA, roomB), keyString);
		});
		return result;
	}

	private static void createLocations(Result aspResult, Dungeon dungeon) {
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
	}

	private static JDPoint getPoint(Fact roomFact) {
		Fact.Literal literalX = roomFact.get(0);
		Fact.Literal literalY = roomFact.get(1);
		return new JDPoint(Integer.parseInt(literalX.asString()), Integer.parseInt(literalY.asString()));
	}
}
