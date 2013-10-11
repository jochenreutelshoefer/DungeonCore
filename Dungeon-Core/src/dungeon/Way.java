package dungeon;

import java.util.*;

/**
 * Eine Liste von Raeumen die einen Weg darstellt
 * 
 */
public class Way {

	LinkedList<RoomInfo> RoomInfos;

	boolean blocked;

	public Way(LinkedList<RoomInfo> r, boolean blocked) {
		if (r == null) {
			System.out.println("way mit nullliste erzeugt!");
			System.exit(0);
		}
		RoomInfos = r;
		this.blocked = blocked;
	}

	public RoomInfo getStartRoomInfo() {
		return ( RoomInfos.get(0));
	}

	public int size() {
		return RoomInfos.size();
	}

	public RoomInfo get(int i) {
		return ((RoomInfo) RoomInfos.get(i));
	}

	public RoomInfo getDestinationRoomInfo() {
		return ((RoomInfo) RoomInfos.getLast());
	}

	/**
	 * @return
	 * 
	 * @uml.property name="blocked"
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="RoomInfos"
	 */
	public LinkedList<RoomInfo> getRoomInfos() {
		return RoomInfos;
	}
	
	public LinkedList<Room> getRooms() {
		LinkedList<Room> result = new LinkedList<Room>();
		for (RoomInfo room : RoomInfos) {
			result.add(room.getRoom());
		}
		return result;
	}

}
