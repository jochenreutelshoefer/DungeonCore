package location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dungeon.JDPoint;
import dungeon.Position;
import dungeon.Room;
import dungeon.RoomEntity;
import figure.DungeonVisibilityMap;
import game.InfoEntity;
import game.InfoProvider;
import game.Turnable;
import item.Item;
import item.interfaces.Usable;

/**
 * Abstrakte Oberklasse aller Schreine/Oertlichkeiten.
 * Ist von Anfang an einem Raum zugeordnet, nicht mobil.
 */
public abstract class Location implements Usable, Turnable, InfoProvider, RoomEntity {


	public static final LocationState DEFAULT_STATE = new LocationState() {};

	protected Room location;

	private String name;

	protected String story;
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
	public abstract void turn(int round);

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
	public JDPoint getLocation() {
		return location.getNumber();
	}

	public String getName() {
		return name;
	}
}

