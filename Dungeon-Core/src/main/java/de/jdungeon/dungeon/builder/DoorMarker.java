package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import de.jdungeon.dungeon.util.RouteInstruction;

public class DoorMarker {

	int x1;
	int y1;
	int x2;
	int y2;

	public DoorMarker(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static Collection<DoorMarker> createAllDoorsOfRoom(int x, int y) {
		Set<DoorMarker> result = new HashSet<>();
		result.add(create(x, y, RouteInstruction.Direction.West));
		result.add(create(x, y, RouteInstruction.Direction.East));
		result.add(create(x, y, RouteInstruction.Direction.North));
		result.add(create(x, y, RouteInstruction.Direction.East));
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
		throw new IllegalArgumentException("not a vaid direction");
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
}
