package dungeon.generate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.01.18.
 */
public class DeadEndPath {

	private final Room endRoom;

	private final List<Room> rooms;
	public DeadEndPath(Room endRoom, List<Room> rooms) {
		this.endRoom = endRoom;
		this.rooms = rooms;
	}

	public Room getEndRoom() {
		return endRoom;
	}

	public int getLength() {
		return rooms.size();
	}

	public static DeadEndPath getLongestDeadEndPath(Collection<DeadEndPath> paths) {
		if(paths == null || paths.isEmpty()) return null;
		List<DeadEndPath> sortList = new ArrayList<>(paths);
		Collections.sort(sortList, new Comparator<DeadEndPath>() {
			@Override
			public int compare(DeadEndPath o1, DeadEndPath o2) {
				return Integer.compare(o2.getLength(), o1.getLength());
			}
		});
		return sortList.get(0);
	}




}
