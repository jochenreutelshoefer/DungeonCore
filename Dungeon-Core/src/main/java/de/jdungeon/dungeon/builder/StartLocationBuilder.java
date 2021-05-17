package de.jdungeon.dungeon.builder;

import java.util.Objects;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.log.Log;

public class StartLocationBuilder extends AbstractLocationBuilder {

	public JDPoint exitPosition;

	public StartLocationBuilder(JDPoint startPosition, JDPoint exitPosition) {
		super(startPosition);
		this.exitPosition = exitPosition;
	}

	/**
	 * Required for JSON serialization
	 */
	public StartLocationBuilder() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StartLocationBuilder that = (StartLocationBuilder) o;
		return exitPosition.equals(that.exitPosition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(exitPosition);
	}

	@Override
	public String getIdentifier() {
		return "Start";
	}

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		Room room = dungeon.getRoom(x, y);
		if (room.getLocation() != null) {
			Log.severe("Location clash. Already location existing when creating start location: " + room.getLocation());
		}
		else {
			room.setLocation(new RevealMapShrine(dungeon.getRoom(exitPosition)));
		}
	}
}
