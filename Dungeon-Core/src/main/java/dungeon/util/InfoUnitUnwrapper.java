package dungeon.util;

import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;
import game.InfoEntity;
import item.ItemInfo;
import item.interfaces.ItemOwner;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class InfoUnitUnwrapper {

	private Dungeon dungeon;

	public InfoUnitUnwrapper(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public static Figure getFighter(int index) {
		return Figure.getFigure(index);
	}


	public Object unwrappObject(InfoEntity o) {
		if (o instanceof ItemInfo) {
			InfoEntity info = ((ItemInfo) o).getOwner();
			ItemOwner owner = (ItemOwner) unwrappObject(info);
			return owner.getItem((ItemInfo) o);
		}

		if (o instanceof RoomInfo) {
			return getRoom((RoomInfo) o);
		}
		if (o instanceof DoorInfo) {
			return getDoor((DoorInfo) o);
		}
		if (o instanceof FigureInfo) {
			return this.getFighter(((FigureInfo) o).getFighterID());
		}
		if (o instanceof PositionInRoomInfo) {
			int index = ((PositionInRoomInfo)o).getIndex();
			JDPoint roomPoint  = ((PositionInRoomInfo)o).getLocation();
			Room room = this.dungeon.getRoom(roomPoint);
			return room.getPositions()[index];
		}

		System.out.println("failed unwrappObject!");
		return null;
	}


	public Room getRoom(RoomInfo r) {
		JDPoint p = r.getLocation();
		return dungeon.getRoom(p);
	}

	public Door getDoor(DoorInfo d) {
		RoomInfo room = d.getRooms()[0];
		Room r = getRoom(room);
		Door[] doors = r.getDoors();
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				DoorInfo info = new DoorInfo(doors[i], null);
				if (info.equals(d)) {
					return doors[i];
				}
			}
		}

		return null;
	}
}
