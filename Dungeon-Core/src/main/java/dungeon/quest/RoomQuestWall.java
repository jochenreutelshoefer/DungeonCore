package dungeon.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dungeon.Door;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.generate.DungeonFiller;
import dungeon.generate.RectArea;
import dungeon.generate.undo.DungeonChangeAction;
import dungeon.generate.undo.RemoveDoor;
import dungeon.util.RouteInstruction;
import item.Item;

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
