/*
 * Created on 04.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.memory.DoorMemory;
import game.InfoEntity;
import game.JDEnv;
import gui.Paragraph;
import util.JDColor;

public class DoorInfo extends InfoEntity {

	private final Door d;

	public DoorInfo(Door door, DungeonVisibilityMap m) {
		super(m);
		d = door;
	}

	private boolean getShrineVisibility() {
		return map.getVisibilityStatus(getRooms()[0].getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE
				|| map.getVisibilityStatus(getRooms()[1].getLocation()) >= RoomObservationStatus.VISIBILITY_SHRINE;
	}
	
	@Override
	public DoorMemory getMemoryObject(FigureInfo info) {
		return d.getMemoryObject(info);
	}
	
	public int getDir(JDPoint p) {
		return d.getDir(p);
	}

	private boolean getFoundDiscovery() {
		return map.getVisibilityStatus(getRooms()[0].getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND
				|| map.getVisibilityStatus(getRooms()[1].getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND;
	}
	
//	private boolean getFoundDiscovery() {
//		return map.getVisibilityStatus(getRooms()[0].getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND
//				|| map.getVisibilityStatus(getRooms()[1].getLocation()) >= RoomObservationStatus.VISIBILITY_FOUND;
//	}

	public Boolean isLocked() {
		if (getShrineVisibility()) {
			return new Boolean(d.getLocked());
		}
		return null;
	}

	public String getLock() {
		if (getFoundDiscovery()) {
			return d.getLock();
		}
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof DoorInfo) {
			if(((DoorInfo)o).d == this.d) {
				return true;
			}
		}
		return false;
	}

	public Boolean isPassable() {
		if (getShrineVisibility()) {
			return new Boolean(d.isPassable(map.getFigure()));
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
		if (d.isPassable(map.getFigure())) {
			p[1] = new Paragraph(JDEnv.getString("door_passable"));
		} else {
			p[1] = new Paragraph(JDEnv.getString("door_not_passable"));
		}
		p[1].setSize(14);
		p[1].setCentered();
		p[1].setBold();
		String s2 = new String();
		if (!d.getLock().equals("unlockable")) {
			s2 += JDEnv.getString("door_lock")+": " + d.getLock() + "  Status: ";
			if (d.getLocked()) {
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
		if (d.getBlockings().size() > 0) {
			s3 += JDEnv.getString("door_blocked")+": ";
			// for(int i = 0; i < blockings)
		}
		if(d.hasEscapeRoute(this.map.getFigure())) {
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
			return new Boolean(d.getBlockings().size() > 0);
		}
		return null;
	}

	public Boolean hasLock() {
		if(getFoundDiscovery()) {
		return new Boolean(d.hasLock());
		}
		return null;
	}

	public RoomInfo[] getRooms() {
		RoomInfo[] infos = new RoomInfo[2];
		infos[0] = RoomInfo.makeRoomInfo(d.getRooms()[0], map);
		infos[1] = RoomInfo.makeRoomInfo(d.getRooms()[1], map);
		return infos;
	}

	public RoomInfo getOtherRoom(RoomInfo r) {
		return d.getOtherRoomInfo(r, map);
	}

	

}
