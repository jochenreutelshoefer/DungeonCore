package de.jdungeon.dungeon.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.denkbares.strings.Strings;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.asp.Fact;
import de.jdungeon.dungeon.builder.asp.Result;
import de.jdungeon.dungeon.builder.serialization.DungeonDTO;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.dungeon.builder.serialization.LockDTO;
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

	public LevelDTO createDungeon(Result aspResult) {

		//final Dungeon dungeon = new Dungeon(builder.gridWidth, builder.gridHeight);
		DungeonDTO dungeonDTO = new DungeonDTO(builder.gridWidth, builder.gridHeight);
		LevelDTO levelDTO = new LevelDTO(dungeonDTO);

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
			DoorMarker doorMarker = new DoorMarker(point1, point2);

			//Room room1 = dungeon.getRoom(point1);
			//Room room2 = dungeon.getRoom(point2);
			//RouteInstruction.Direction direction = RouteInstruction.Direction.fromPoints(point1, point2);
			//Door door = new Door(room1, room2);
			Pair<JDPoint, JDPoint> pointPair = new Pair<>(point1, point2);
			if (locksMap.containsKey(pointPair)) {
				dungeonDTO.addLock(new LockDTO(locksMap.get(pointPair), doorMarker));
				//door.setKey(new Key(locksMap.get(pointPair)));
			}
			dungeonDTO.addDoor(doorMarker);
			//room1.setDoor(door, direction, true);
		});

		//Room startRoom = dungeon.getRoom(builder.startX, builder.startY);
		levelDTO.setStartPosition(new JDPoint(builder.startX, builder.startY));
		//startRoom.addItem(new VisibilityCheatBall());

		createLocations(aspResult, dungeonDTO);

		return levelDTO;
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

	private void createLocations(Result aspResult, DungeonDTO dungeon) {
		Stream<Fact> locationFactStream = aspResult.getFacts()
				.stream()
				.filter(fact -> fact.getPredicate().equals(LOCATION_PREDICATE));

		locationFactStream.forEach(locationFact -> {
			Fact.Literal locationIdentifier = locationFact.get(0);
			Fact roomPosFact = locationFact.getFacts()[0];
			int posX = roomPosFact.get(0).asNumber();
			int posY = roomPosFact.get(1).asNumber();
			LocatedEntityBuilder locationBuilder = builder.locations.get(locationIdentifier.asString());
			dungeon.addLocation(locationBuilder, posX, posY);
			//locationBuilder.insert(dungeon, posX, posY);
		});
	}

	private static JDPoint getPoint(Fact roomFact) {
		Fact.Literal literalX = roomFact.get(0);
		Fact.Literal literalY = roomFact.get(1);
		return new JDPoint(Integer.parseInt(literalX.asString()), Integer.parseInt(literalY.asString()));
	}
}
