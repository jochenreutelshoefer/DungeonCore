package de.jdungeon.dungeon.util;

import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.dungeon.Chest;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.LockInfo;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.game.InfoEntity;
import de.jdungeon.game.RoomInfoEntity;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.item.interfaces.ItemOwner;
import de.jdungeon.location.Location;
import de.jdungeon.location.LocationInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class InfoUnitUnwrapper {

	private final Dungeon dungeon;

	public InfoUnitUnwrapper(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public Collection<Object> unwrappObjects(Collection<? extends InfoEntity> entities) {
		Collection<Object> result = new HashSet<>();
		for (InfoEntity entity : entities) {
			Object o = unwrappObject(entity);
			if(o != null) {
				result.add(o);
			}
		}
		return result;
	}

	public RoomEntity unwrappObject(InfoEntity o) {
		if(o == null) {
			return null;
		}
		if (o instanceof ItemInfo) {
			InfoEntity info = ((ItemInfo) o).getOwner();
			ItemOwner owner = (ItemOwner) unwrappObject(info);
			return owner.unwrapItem((ItemInfo) o);
		}
		if(o instanceof LocationInfo) {
			return getLocation(((LocationInfo)o));
		}
		if (o instanceof RoomInfo) {
			return getRoom((RoomInfo) o);
		}
		if (o instanceof DoorInfo) {
			return getDoor((DoorInfo) o);
		}
		if (o instanceof LockInfo) {
			RoomInfoEntity lockedObject = ((LockInfo) o).getLockedObject();
			if(lockedObject instanceof DoorInfo) {
				return ((Door)unwrappObject(lockedObject)).getLock();
			}
			if(lockedObject instanceof ChestInfo) {
				return ((Chest)unwrappObject(lockedObject)).getLock();
			}
		}
		if (o instanceof FigureInfo) {
			return dungeon.getFigureIndex().get(((FigureInfo) o).getFigureID());
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

	private Location getLocation(LocationInfo o) {
		JDPoint p = o.getLocation();
		return dungeon.getRoom(p).getLocation();
	}

	public Room getRoom(RoomInfo r) {
		JDPoint p = r.getRoomNumber();
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
