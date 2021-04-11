package de.jdungeon.dungeon.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.generate.undo.DungeonChangeAction;
import de.jdungeon.dungeon.generate.undo.RemoveDoor;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.item.Item;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.17.
 */
public class RoomQuestWall extends ReversibleRoomQuest {

	public RoomQuestWall(DungeonFiller df, int x, int y) {
		super(df, x, y);
	}

	@Override
	List<DungeonChangeAction> createActionList() {
		List<DungeonChangeAction> actions = new ArrayList<>();

		for(int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				Room room = rooms[y][x];
				for(int dir = 1; dir < 5; dir++) {
					RouteInstruction.Direction direction = RouteInstruction.Direction.fromInteger(dir);
					Door door = room.getDoor(direction);
					if(door != null) {
						actions.add(new RemoveDoor(room, direction));
					}
				}
			}
		}

		return actions;
	}

	@Override
	public Collection<Item> finalizeRoomQuest() {
		Room[][] rooms = getRooms();
		for (Room[] col : rooms) {
			for (Room room : col) {
				room.setIsWall(true);
			}
		}

		return Collections.emptyList();
	}

}
