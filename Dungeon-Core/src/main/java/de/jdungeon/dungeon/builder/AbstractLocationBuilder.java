package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

public abstract class AbstractLocationBuilder implements LocatedEntityBuilder {

	protected JDPoint room;

	public AbstractLocationBuilder() {
	}

	public AbstractLocationBuilder(JDPoint room) {
		this.room = room;
	}

	public abstract String getIdentifier();

	public AbstractLocationBuilder setRoom(int x, int y) {
		this.room = new JDPoint(x, y);
		return this;
	}

	public boolean hasFixedRoomPosition() {
		return room != null;
	}

	public JDPoint getRoomPosition() {
		return room;
	}

}
