/*
 * Created on 24.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import dungeon.Door;
import dungeon.Dungeon;
import dungeon.JDPoint;

public class DungeonVisibilityMap {

	private RoomObservationStatus rooms[][];

	private Figure f;

	private boolean hasVisCheat = false;

	private Dungeon dungeon;

	private static DungeonVisibilityMap allVis;

	public static DungeonVisibilityMap getAllVisMap(Dungeon d) {
		if (allVis == null) {
			allVis = new DungeonVisibilityMap(d);
			RoomObservationStatus[][] stats = d.getNewRoomVisibilityMap(allVis);
			allVis.setMap(stats);
			allVis.setVisCheat();
		}
		return allVis;

	}

	public void setOtherDungeon(Dungeon d) {
		dungeon = d;
		RoomObservationStatus[][] stats = d.getNewRoomVisibilityMap(this);
		rooms = stats;
	}

	public void setFigure(Figure f) {
		this.f = f;
	}

	public Figure getFigure() {
		return f;
	}

	public DungeonVisibilityMap(Dungeon d) {
		dungeon = d;
	}

	public DungeonVisibilityMap() {

	}

	public void setVisCheat() {
		hasVisCheat = true;
	}

	public JDPoint getSuperiorPoint(Door d) {
		JDPoint p1 = d.getRooms()[0].getLocation();
		JDPoint p2 = d.getRooms()[1].getLocation();
		int status1 = getVisibilityStatus(p1);
		int status2 = getVisibilityStatus(p2);

		if (status1 >= status2) {
			return p1;
		}
		return p2;
	}

	public void setMap(RoomObservationStatus[][] r) {
		this.rooms = r;
	}

	public int getSizeX() {
		return rooms.length;
	}

	public int getSizeY() {
		return rooms[0].length;
	}

	public RoomObservationStatus getStatusObject(JDPoint p) {
		if (p == null)
			return null;
		return rooms[p.getX()][p.getY()];
	}

	public void setVisibilityStatus(int x, int y, int status) {
		rooms[x][y].setVisibilityStatus(status);
	}

	public void setDiscoveryStatus(int x, int y, int status) {
		rooms[x][y].setDiscoveryStatus(status);

	}

	public void resetVisibilityStatus(int x, int y) {
		rooms[x][y].resetVisibilityStatus();
	}

	public int getVisibilityStatus(int x, int y) {
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		if (rooms[x][y] != null) {
			return rooms[x][y].getVisibilityStatus();
		} else {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
	}

	public int getDiscoveryStatus(int x, int y) {
		if(x >= rooms.length || y >= rooms[0].length) {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		if (rooms[x][y] != null) {
			return rooms[x][y].getDiscoveryStatus();
		} else {
			return RoomObservationStatus.VISIBILITY_UNDISCOVERED;
		}
	}

	public void resetVisibilityStatus(JDPoint p) {
		resetVisibilityStatus(p.getX(), p.getY());
	}

	public int getVisibilityStatus(JDPoint p) {
		if(p == null) return -1;
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		return getVisibilityStatus(p.getX(), p.getY());
	}


	public int getDiscoveryStatus(JDPoint p) {
		if (hasVisCheat) {
			return RoomObservationStatus.VISIBILITY_SECRET;
		}
		return getDiscoveryStatus(p.getX(), p.getY());
	}

	public void setDiscoveryStatus(JDPoint p, int status) {
		setDiscoveryStatus(p.getX(), p.getY(), status);
	}

	public void setVisibilityStatus(JDPoint p, int status) {
		setVisibilityStatus(p.getX(), p.getY(), status);
	}

	/**
	 * @return Returns the dungeon.
	 */
	public Dungeon getDungeon() {
		return dungeon;
	}

	/**
	 * @param dungeon
	 *            The dungeon to set.
	 */
	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public RoomObservationStatus[][] getRooms() {
		return rooms;
	}

}
