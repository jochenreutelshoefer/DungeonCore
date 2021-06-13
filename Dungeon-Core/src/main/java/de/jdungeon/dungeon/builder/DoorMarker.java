package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;

public class DoorMarker implements Json.Serializable {

	int x1;
	int y1;
	int x2;
	int y2;

	public DoorMarker(int x1, int y1, int x2, int y2) {
		if(x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0) {
			throw new IllegalArgumentException("Invalid DoorMarker coordinates: "+x1 +" - " + y1+ "; " + x2+" - "+ y2);
		}
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public DoorMarker(JDPoint p1, JDPoint p2) {
		this.x1 = p1.getX();
		this.y1 = p1.getY();
		this.x2 = p2.getX();
		this.y2 = p2.getY();
	}

	/**
	 * Required for JSON serialization
	 */
	public DoorMarker() {
	}

	public static Collection<DoorMarker> createAllDoorsOfRoom(int x, int y) {
		Set<DoorMarker> result = new HashSet<>();
		result.add(create(x, y, RouteInstruction.Direction.West));
		result.add(create(x, y, RouteInstruction.Direction.East));
		result.add(create(x, y, RouteInstruction.Direction.North));
		result.add(create(x, y, RouteInstruction.Direction.East));
		return result;
	}

	public static Collection<DoorMarker> createAllDoorsOfRoomIfInRange(int x, int y, int dungeonWidth, int dungeonHeight) {
		Set<DoorMarker> result = new HashSet<>();
		result.add(createIfInRange(x, y, RouteInstruction.Direction.West, dungeonWidth, dungeonHeight));
		result.add(createIfInRange(x, y, RouteInstruction.Direction.East, dungeonWidth, dungeonHeight));
		result.add(createIfInRange(x, y, RouteInstruction.Direction.North, dungeonWidth, dungeonHeight));
		result.add(createIfInRange(x, y, RouteInstruction.Direction.East, dungeonWidth, dungeonHeight));
		result.remove(null);
		return result;
	}

	public static DoorMarker create(int x, int y, RouteInstruction.Direction dir) {
		if (dir == RouteInstruction.Direction.East) {
			return new DoorMarker(x, y, x + 1, y);
		}
		if (dir == RouteInstruction.Direction.West) {
			return new DoorMarker(x, y, x - 1, y);
		}
		if (dir == RouteInstruction.Direction.South) {
			return new DoorMarker(x, y, x, y + 1);
		}
		if (dir == RouteInstruction.Direction.North) {
			return new DoorMarker(x, y, x, y - 1);
		}
		throw new IllegalArgumentException("not a valid direction");
	}

	public static DoorMarker createIfInRange(int x, int y, RouteInstruction.Direction dir, int dungeonWidth, int dungeonHeight) {
		if (dir == RouteInstruction.Direction.East) {
			int xCoord = x + 1;
			if(xCoord < dungeonWidth-1) {
				return new DoorMarker(x, y, xCoord, y);
			}
		}
		if (dir == RouteInstruction.Direction.West) {
			int xCoord = x - 1;
			if(xCoord >= 0) {
				return new DoorMarker(x, y, xCoord, y);
			}
		}
		if (dir == RouteInstruction.Direction.South) {
			int yCoord = y + 1;
			if(yCoord < dungeonHeight-1) {
				return new DoorMarker(x, y, x, yCoord);
			}
		}
		if (dir == RouteInstruction.Direction.North) {
			int yCoord = y - 1;
			if(yCoord >= 0) {
				return new DoorMarker(x, y, x, yCoord);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "DoorMarker{" +
				"x1=" + x1 +
				", y1=" + y1 +
				", x2=" + x2 +
				", y2=" + y2 +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DoorMarker that = (DoorMarker) o;
		return x1 == that.x1 && y1 == that.y1 && x2 == that.x2 && y2 == that.y2 || x1 == that.x2 && y1 == that.y2 && x2 == that.x1 && y2 == that.y1;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x1, y1, x2, y2) + Objects.hash(x2, y2, x1, y1);
	}

	@Override
	public void write(Json json) {
		json.writeFields(this);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		json.readFields(this, jsonData);
	}
}
