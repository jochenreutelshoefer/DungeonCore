package de.jdungeon.dungeon.builder;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Lock;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.serialization.DungeonDTO;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.dungeon.builder.serialization.LockDTO;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.item.Key;
import de.jdungeon.log.Log;

public class DTODungeonFactory {

	LevelDTO levelDTO;

	public DTODungeonFactory(LevelDTO levelDTO) {
		this.levelDTO = levelDTO;
	}

	public Dungeon create() {
		DungeonDTO dungeonDTO = levelDTO.getDungeonDTO();
		Dungeon dungeon = new Dungeon(dungeonDTO.getWidth(), dungeonDTO.getHeight());

		// insert doors
		Set<DoorMarker> doors = dungeonDTO.getDoors();
		doors.forEach(doorDTO -> {
			Room room1 = dungeon.getRoom(doorDTO.x1, doorDTO.y1);
			Room room2 = dungeon.getRoom(doorDTO.x2, doorDTO.y2);
			room1.setDoorTo(room2);
			if(doorDTO.getKeyString() != null) {
				Door door = room1.getDoor(RouteInstruction.Direction.fromPoints(room1.getPoint(), room2.getPoint()));
				door.setLock(new Lock(new Key(doorDTO.getKeyString()), door));
			}
		});

		// create locks
		Set<LockDTO> locks = dungeonDTO.getLocks();
		locks.stream().forEach(lockDTO -> {
			DoorMarker doorMarker = lockDTO.getDoor();
			Room room1 = dungeon.getRoom(doorMarker.x1, doorMarker.y1);
			Room room2 = dungeon.getRoom(doorMarker.x2, doorMarker.y2);
			Door door = room1.getDoor(room1.getDirection(room2));

			door.setKey(new Key(lockDTO.getKey()));
		});

		// insert locations
		Map<JDPoint, LocatedEntityBuilder> locations = dungeonDTO.getLocations();
		locations.entrySet().stream().sorted(Comparator.comparingInt(entryA -> {
			LocatedEntityBuilder value = entryA.getValue();
			if(value != null) {
				return value.getBuildPriority();
			} else {
				Log.severe("Builder value was null!");
				return 0;
			}
		}))
				.forEach(entry -> {
					JDPoint point = entry.getKey();
					if(point != null) {
						LocatedEntityBuilder locatedEntityBuilder = entry.getValue();
						if(locatedEntityBuilder != null) {
							locatedEntityBuilder.insert(dungeon, point.getX(), point.getY());
						}
					}
				});
		/*
		locations.keySet().stream().forEach(point -> {
			LocatedEntityBuilder locatedEntityBuilder = locations.get(point);
			locatedEntityBuilder.insert(dungeon, point.getX(), point.getY());
		});

		 */

		return dungeon;
	}
}
