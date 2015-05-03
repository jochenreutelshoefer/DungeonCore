//import javax.swing.text.*;
package dungeon;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.memory.DoorMemory;
import figure.monster.Monster;
import game.InfoEntity;
import game.InfoProvider;
import item.Key;

import java.util.LinkedList;
import java.util.List;

import shrine.Statue;
import dungeon.util.DungeonUtils;

/**
 * Eine Tuer stellt die Verbindung zwischen zwei Raeumen dar. Tueren koennen
 * verschlossen sein und dann mit dem richtigen Schluessel aufgeschlossen
 * werden. Tueren koennen versteckt sein und verhalten sich dann wie eine Mauer
 * und koennen durch Zufall oder durch den Entdecken-Zauberspruch enttarnt
 * werden. Tueren koennen durch Zaubersprueche eine zeitlang blockiert werden.
 * 
 */
public class Door implements  InfoProvider {

	public static final int NO_DOOR = 0;

	public static final int DOOR = 1;

	public static final int DOOR_LOCK_OPEN = 2;

	public static final int DOOR_LOCK_LOCKED = 3;

	private final Room[] rooms = new Room[2];

	private final boolean passable = true;

	private final LinkedList blockings = new LinkedList();

	private final List escapeRoutes = new LinkedList();
	
	private String lock = "unlockable";

	private boolean locked = false;

	private boolean hallDoor = false;

	private boolean isHidden = false;

	private int difficultyToFind = 0;

	public int getDifficultyToFind() {
		return difficultyToFind;
	}

	public boolean hasEscapeRoute(Figure f) {
		return escapeRoutes.contains(f);
	}
	public void setEscapeRoute(Figure f){
		escapeRoutes.add(f);
	}
	
	public DoorMemory getMemoryObject(FigureInfo info) {
		return new  DoorMemory(this);
	}
	
	public boolean unsetEscapeRoute(Figure f) {
		return escapeRoutes.remove(f);
	}
	
	public int getDir(JDPoint p) {
		int dir = rooms[0].getConnectionDirectionTo(rooms[1]);
		if (rooms[0].getLocation().equals(p)) {
			return dir;
		} else {
			if (rooms[1].getLocation().equals(p)) {
				return Dir.getOppositDir(dir);
			} else {
				return -1;
			}
		}
	}

	public void setDifficultyToFind(int difficultyToFind) {
		this.difficultyToFind = difficultyToFind;
	}

	public boolean isHidden() {
		return isHidden;
	}

	@Override
	public JDPoint getLocation() {
		return rooms[0].getLocation();
	}

	public void setHidden(boolean isHidden) {
		difficultyToFind = 150;
		this.isHidden = isHidden;
	}

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		JDPoint point = map.getSuperiorPoint(this);
		return new DoorInfo(this, map);
	}

	Key schluessel;

	private boolean statueBlocks;

	public Door(Room r1, Room r2) {
		// System.out.println("Neue Tuer: "+r1.toString()+" - "+r2.toString());
		rooms[0] = r1;
		rooms[1] = r2;
	}

	public void setHallDoor(boolean b) {
		hallDoor = b;
	}

	public boolean isHallDoor() {
		return hallDoor;
	}

	public Position getPositionBehind(Room r) {
		//System.out.println("mageRoom: "+r.toString());
		int roomTo = 0;
		if (r.equals(rooms[0])) {
			roomTo = 1;
		}
		Room otherRoom = rooms[roomTo];
		//System.out.println("otherRomm: "+otherRoom.toString());
		int dir = DungeonUtils.getNeighbourDirectionFromTo(r, otherRoom)
				.getValue();
		int index = -1;
		if (dir == Dir.NORTH) {
			index = 5;
		}
		if (dir == Dir.EAST) {
			index = 7;
		}
		if (dir == Dir.SOUTH) {
			index = 1;
		}
		if (dir == Dir.WEST) {
			index = 3;
		}
		
		Position p = otherRoom.getPositions()[index];
		//System.out.println("Pos: "+p.toString());
		return p;

	}

	public Door(Room r1, Room r2, Key k) {
		// System.out.println("T�r mit Schloss geschaffen: zu");
		rooms[0] = r1;
		rooms[1] = r2;
		this.lock = k.getType();
		this.schluessel = k;
		locked = true;
	}

	public Key getKey() {
		return schluessel;
	}

	public String getLock() {
		return lock;
	}

	public Door(Door d, Key k) {
		rooms[0] = d.getRooms()[0];
		rooms[1] = d.getRooms()[1];
		this.lock = k.getType();
		this.schluessel = k;
		locked = true;
	}

	

	public void addBlocking(DoorBlock b) {
		blockings.add(b);
	}

	public boolean removeBlocking(Object s) {
		for (int i = 0; i < blockings.size(); i++) {
			DoorBlock b = ((DoorBlock) blockings.get(i));
			if (b.getSource() == s) {
				blockings.remove(b);
				return true;
			}
		}
		return false;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean b) {
		locked = b;
	}

	public boolean found(int psycho) {
		int k = (int) (Math.random() * difficultyToFind);
		if (k < psycho - 2) {
			isHidden = false;
			return true;
		} else {
			return false;
		}

	}

	public boolean hasLock() {
		if (lock.equals("unlockable")) {
			return false;
		}
		return true;
	}

	public boolean matches(Key k) {
		return k.getType().equals(lock);
	}

	public boolean lock(Key k, boolean doIt) {
		if (matches(k)) {
			if (doIt) {
				locked = !locked;
			}
			return true;
		}
		return false;
	}

	public void setLock(String lock) {
		this.lock = lock;
	}

	public Room[] getRooms() {
		return rooms;
	}

	@Override
	public String toString() {
		return "Tür von: " + rooms[0].toString() + " nach: "
				+ rooms[1].toString();
	}

	/**
	 * F�r Monster (1) nicht passierbar wenn Heldenstatue im Raum
	 * 
	 * 
	 * @param type
	 * @return
	 */
	public boolean isPassable(Figure f) {
		statueBlocks = false;
			for (int i = 0; i < 2; i++) {
				if (rooms[i].getShrine() != null
						&& rooms[i].getShrine() instanceof Statue & f instanceof Monster) {
					statueBlocks = true;
				}
			}
		//}
		boolean blocked = blockings.size() > 0;
		return !locked & !statueBlocks & !blocked;
	}

	public boolean partOfRoomQuest() {
		return (rooms[0].getRoomQuest() != null)
				|| (rooms[1].getRoomQuest() != null);

	}

	public boolean isDoor(Door d) {
		for (int i = 0; i < 2; i++) {
			if (!this.hasRoom(d.getRooms()[i])) {
				return false;
			}
			break;

		}
		return true;
	}

	public Room getOtherRoom(Room r) {
		if (rooms[0] == r) {
			return rooms[1];
		}
		if (rooms[1] == r) {
			return rooms[0];
		}
		return null;
	}

	public RoomInfo getOtherRoomInfo(RoomInfo r, DungeonVisibilityMap map) {
		if (RoomInfo.makeRoomInfo(rooms[0], map).equals(r)) {
			return RoomInfo.makeRoomInfo(rooms[1], map);
		}
		if (RoomInfo.makeRoomInfo(rooms[1], map).equals(r)) {
			return RoomInfo.makeRoomInfo(rooms[0], map);
		}
		return null;
	}

	public boolean hasRoom(Room r) {
		if (rooms[0] == r) {
			return true;
		}
		if (rooms[1] == r) {
			return true;
		}
		return false;

	}

	public LinkedList getBlockings() {
		return blockings;
	}

}
