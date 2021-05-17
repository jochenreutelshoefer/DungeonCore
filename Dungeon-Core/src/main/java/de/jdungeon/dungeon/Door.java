//import javax.swing.de.jdungeon.text.*;
package de.jdungeon.dungeon;

import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.memory.DoorMemory;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.item.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.jdungeon.item.interfaces.Locatable;
import de.jdungeon.location.Statue;
import de.jdungeon.dungeon.util.DungeonUtils;
import de.jdungeon.log.Log;

/**
 * Eine Tuer stellt die Verbindung zwischen zwei Raeumen dar. Tueren koennen
 * verschlossen sein und dann mit dem richtigen Schluessel aufgeschlossen
 * werden. Tueren koennen versteckt sein und verhalten sich dann wie eine Mauer
 * und koennen durch Zufall oder durch den Entdecken-Zauberspruch enttarnt
 * werden. Tueren koennen durch Zaubersprueche eine zeitlang blockiert werden.
 */
public class Door implements Lockable, InfoProvider, Locatable, RoomEntity, Json.Serializable {

	public static final int NO_DOOR = 0;

	public static final int DOOR = 1;

	public static final int DOOR_LOCK_OPEN = 2;

	public static final int DOOR_LOCK_LOCKED = 3;

	private final Room[] rooms = new Room[2];

	private Lock lock = null;

	private boolean locked = false;

	private boolean hallDoor = false;

	public DoorMemory getMemoryObject(FigureInfo info) {
		return new DoorMemory(this);
	}

	public int getDir(JDPoint p) {
		int dir = rooms[0].getConnectionDirectionTo(rooms[1]);
		if (rooms[0].getRoomNumber().equals(p)) {
			return dir;
		}
		else {
			if (rooms[1].getRoomNumber().equals(p)) {
				return Dir.getOppositDir(dir);
			}
			else {
				return -1;
			}
		}
	}

	@Override
	public JDPoint getRoomNumber() {
		return rooms[0].getRoomNumber();
	}

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new DoorInfo(this, map);
	}

	private boolean statueBlocks;

	public Door(Room r1, Room r2) {
		rooms[0] = r1;
		rooms[1] = r2;
	}

	public Door(Room r1, RouteInstruction.Direction dir) {
		// System.out.println("Neue Tuer: "+r1.toString()+" - "+r2.toString());
		rooms[0] = r1;
		rooms[1] = r1.getNeighbourRoom(dir.getValue());
	}

	public Door(Room r1, RouteInstruction.Direction dir, Key key) {
		this(r1, dir);
		setKey(key);
	}

	public boolean isHallDoor() {
		return hallDoor;
	}

	public Position getPositionAtDoor(Room room) {
		return getPositionAtDoor(room, true);
	}

	public Position getPositionAtDoor(Room room, boolean other) {
		int roomTo = 0;
		if (room.equals(rooms[0])) {
			roomTo = 1;
		}
		Room otherRoom = rooms[roomTo];
		int dir = DungeonUtils.getNeighbourDirectionFromTo(room, otherRoom)
				.getValue();
		if (other) {
			return otherRoom.getPositions()[getPosIndexForDirection(dir)];
		}
		else {
			return room.getPositions()[getPosIndexForDirection(RouteInstruction.Direction.opposite(RouteInstruction.Direction
					.fromInteger(dir)).getValue())];
		}
	}

	private int getPosIndexForDirection(int dir) {
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
		return index;
	}

	public Door(Room r1, Room r2, Key k) {
		rooms[0] = r1;
		rooms[1] = r2;
		if (k != null) {
			setKey(k);
		}
	}

	public Key getKey() {
		return lock.getKey();
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock l) {
		this.lock = l;
	}

	public void setKey(Key k) {
		this.lock = new Lock(k, this);
		locked = true;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean b) {
		locked = b;
	}

	public boolean hasLock() {
		return lock != null;
	}

	public boolean lockMatches(Key k) {
		return k.getType().equals(lock.getKey().getType());
	}

	public void toggleLock(Key k) {
		if (lockMatches(k)) {
			locked = !locked;
		}
		else {
			Log.severe("Should not happen, as Lock matching was already to be checked before..");
		}
	}


	public Door(Door d, Key k) {
		rooms[0] = d.getRooms()[0];
		rooms[1] = d.getRooms()[1];
		if (k != null) {
			setKey(k);
		}
	}




	public Room[] getRooms() {
		return rooms;
	}

	@Override
	public String toString() {
		return "Tür von: " + rooms[0] + " nach: "
				+ rooms[1];
	}

	public boolean isPassable(Figure f) {
		statueBlocks = false;
		for (int i = 0; i < 2; i++) {
			if (rooms[i].getLocation() != null
					&& rooms[i].getLocation() instanceof Statue & f instanceof Monster) {
				statueBlocks = true;
			}
		}
		return !locked & !statueBlocks;
	}

	public Room getOtherRoom(Room r) {
		if (r == null) return r;
		if (r.equals(rooms[0])) {
			return rooms[1];
		}
		if (r.equals(rooms[1])) {
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

	@Override
	public Collection<Position> getInteractionPositions() {
		Collection<Position> result = new HashSet<>();
		result.add(getPositionAtDoor(rooms[0]));
		result.add(getPositionAtDoor(rooms[1]));
		return result;
	}

	private static final String START_TIME = "start_time";

	@Override
	public void write(Json json) {
		json.writeFields(this);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		json.readFields(this, jsonData);
	}
}
