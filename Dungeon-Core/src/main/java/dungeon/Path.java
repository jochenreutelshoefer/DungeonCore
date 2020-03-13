package dungeon;

import java.util.LinkedList;
import java.util.List;

public class Path {

	private final List<RoomInfo> roomInfos;


	public Path(List<RoomInfo> r) {
		roomInfos = r;
	}

	public RoomInfo getStartRoomInfo() {
		return ( roomInfos.get(0));
	}

	public int size() {
		return roomInfos.size();
	}

	public RoomInfo get(int i) {
		return (roomInfos.get(i));
	}

	public RoomInfo getDestinationRoomInfo() {
		return (roomInfos.get(roomInfos.size() - 1));
	}


	public List<Room> getRooms() {
		List<Room> result = new LinkedList<Room>();
		for (RoomInfo room : roomInfos) {
			result.add(room.getRoom());
		}
		return result;
	}

}
