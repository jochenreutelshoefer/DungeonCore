package dungeon;

import java.util.LinkedList;
import java.util.List;

/**
 * Eine Liste von Raeumen die einen Weg darstellt
 * 
 */
public class Way {

	private final List<RoomInfo> roomInfos;

	private final boolean blocked;

	public Way(List<RoomInfo> r, boolean blocked) {
		roomInfos = r;
		this.blocked = blocked;
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

	public boolean isBlocked() {
		return blocked;
	}

	public List<RoomInfo> getRoomInfos() {
		return roomInfos;
	}
	
	public List<Room> getRooms() {
		List<Room> result = new LinkedList<Room>();
		for (RoomInfo room : roomInfos) {
			result.add(room.getRoom());
		}
		return result;
	}

}
