package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.location.Location;

public class LocationBuilder {

	private JDPoint room;

	private Class<? extends Location> clazz;

	public LocationBuilder(Class<? extends Location> clazz) {
		this.clazz = clazz;
	}

	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public LocationBuilder setRoom(int x, int y) {
		this.room = new JDPoint(x, y);
		return this;
	}

	public boolean hasFixedRoomPosition() {
		return room != null;
	}

	public JDPoint getRoomPosition() {
		return room;
	}

	public Class<? extends Location> getClazz() {
		return clazz;
	}
}
