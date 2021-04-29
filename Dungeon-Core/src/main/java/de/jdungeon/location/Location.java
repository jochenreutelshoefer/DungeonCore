package de.jdungeon.location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.dungeon.InfoEntity;
import de.jdungeon.dungeon.InfoProvider;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.Turnable;
import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.Usable;

/**
 * Abstrakte Oberklasse aller Schreine/Oertlichkeiten.
 * Ist von Anfang an einem Raum zugeordnet, nicht mobil.
 */
public abstract class Location implements Usable, Turnable, InfoProvider, RoomEntity {


	public static final LocationState DEFAULT_STATE = () -> "";

	protected Room location;
	String story;
	protected String text;

	public Location(Room p) {
		location = p;
	}

	public Location() {

	}

	public LocationState getState() {
		return DEFAULT_STATE;
	}

	public int getSecondIdentifier() {
		return -1;
	}

	@Override
	public Collection<Position> getInteractionPositions() {
		return Collections.singletonList(this.getRoom().getPositions()[2]);
	}

	public void setLocation(Room p) {
		location = p;
	}

	protected final List<Item> items = new ArrayList<>();

	public List<Item> getItems() {
		return Collections.unmodifiableList(items);
	}

	@Override
	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		return new LocationInfo(this, map);
	}

	@Override
	public abstract void turn(int round, DungeonWorldUpdater mode);

	@Deprecated
	public int getType() {
		return 1;
	}

	public abstract String getStory();

	@Override
	public abstract String toString();

	public abstract String getText();

	public abstract String getStatus();

	public Room getRoom() {
		return location;
	}

	@Override
	public JDPoint getRoomNumber() {
		return location.getNumber();
	}

}

