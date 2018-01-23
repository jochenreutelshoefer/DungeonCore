/*
 * Created on 04.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import java.util.ArrayList;
import java.util.Collection;

import dungeon.util.RouteInstruction;
import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.memory.DoorMemory;
import game.JDEnv;
import game.RoomInfoEntity;
import gui.Paragraph;
import util.JDColor;

public class DoorInfo extends RoomInfoEntity {

	private final Door door;

	public DoorInfo(Door d, DungeonVisibilityMap m) {
		super(m);
		if(d == null) {
			throw new IllegalArgumentException("object for info object may not be null!");
		}
		door = d;
	}

	private boolean getShrineVisibility() {
		return map.getVisibilityStatus(getRooms()[0].getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE
				|| map.getVisibilityStatus(getRooms()[1].getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE;
	}


	@Override
	public Collection<PositionInRoomInfo> getInteractionPositions() {
		Collection<PositionInRoomInfo> result = new ArrayList<>();
		Room[] rooms = door.getRooms();
		Position posA = door.getPositionAtDoor(rooms[0]);
		Position posB = door.getPositionAtDoor(rooms[1]);
		result.add(new PositionInRoomInfo(posA, this.map));
		result.add(new PositionInRoomInfo(posB, this.map));
		return result;
	}
	
	@Override
	public DoorMemory getMemoryObject(FigureInfo info) {
		return door.getMemoryObject(info);
	}
	
	public int getDir(JDPoint p) {
		return door.getDir(p);
	}

	public RouteInstruction.Direction getDirection(JDPoint p) {
		return RouteInstruction.Direction.fromInteger(door.getDir(p));
	}

	private boolean getFoundDiscovery() {
		return map.getVisibilityStatus(getRooms()[0].getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND
				|| map.getVisibilityStatus(getRooms()[1].getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND;
	}
	
	public Boolean isLocked() {
		if (getShrineVisibility()) {
			return door.getLocked();
		}
		return null;
	}

	public LockInfo getLock() {
		if (getFoundDiscovery()) {
			return new LockInfo(door.getLock(), map);
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof DoorInfo))
			return false;

		return door.equals(((DoorInfo) obj).door);
	}

	public PositionInRoomInfo getPositionAtDoor(RoomInfo room, boolean other) {
		return new PositionInRoomInfo(door.getPositionAtDoor(map.getDungeon().getRoom(room.getPoint()), other), map);
	}


	@Override
	public int hashCode() {
		return door != null ? door.hashCode() : 0;
	}

	public Boolean isPassable() {
		if (getShrineVisibility()) {
			return door.isPassable(map.getFigure());
		}
		return null;
	}
	


	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(JDEnv.getString("door"));
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(JDColor.orange);
		p[0].setBold();
		if (door.isPassable(map.getFigure())) {
			p[1] = new Paragraph(JDEnv.getString("door_passable"));
		} else {
			p[1] = new Paragraph(JDEnv.getString("door_not_passable"));
		}
		p[1].setSize(14);
		p[1].setCentered();
		p[1].setBold();
		String s2 = new String();
		if (door.getLock() != null) {
			s2 += JDEnv.getString("door_lock")+": " + door.getLock() + "  Status: ";
			if (door.getLocked()) {
				s2 += JDEnv.getString("door_locked");
			} else {
				s2 += JDEnv.getString("door_open");
			}
		} else {
			s2 = new String();
		}
		p[2] = new Paragraph(s2);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setBold();

		String s3 = new String();
		if (!door.getBlockings().isEmpty()) {
			s3 += JDEnv.getString("door_blocked")+": ";
			// for(int i = 0; i < blockings)
		}
		if(door.hasEscapeRoute(this.map.getFigure())) {
			s3 += " - "+JDEnv.getString("spell_escape_route_name");
		}
		p[3] = new Paragraph(s3);
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setBold();
		return p;

	}

	public Boolean isBlocked() {
		if (getShrineVisibility()) {
			return !door.getBlockings().isEmpty();
		}
		return null;
	}

	public Boolean hasLock() {
		if(getFoundDiscovery()) {
		return door.hasLock();
		}
		return null;
	}

	public RoomInfo[] getRooms() {
		RoomInfo[] infos = new RoomInfo[2];
		infos[0] = RoomInfo.makeRoomInfo(door.getRooms()[0], map);
		infos[1] = RoomInfo.makeRoomInfo(door.getRooms()[1], map);
		return infos;
	}

	public RoomInfo getOtherRoom(RoomInfo r) {
		return door.getOtherRoomInfo(r, map);
	}

	@Override
	public String toString() {
		return door.toString();
	}

}
