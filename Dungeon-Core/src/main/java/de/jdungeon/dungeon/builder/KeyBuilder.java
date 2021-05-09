package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.item.Key;

public class KeyBuilder extends AbstractLocationBuilder {

	private final String keyString;

	private DoorMarker lockDoor;

	private final Collection<LocatedEntityBuilder> locationsReachableWithoutKey = new HashSet<>();
	private final Collection<LocatedEntityBuilder> locationsNotReachableWithoutKey = new HashSet<>();

	public KeyBuilder(String keyString, int x, int y) {
		super(new JDPoint(x, y));
		this.keyString = keyString;
		locationsReachableWithoutKey.add(this);
	}

	public DoorMarker getLockDoor() {
		return lockDoor;
	}

	public KeyBuilder setLockDoor(DoorMarker lockDoor) {
		this.lockDoor = lockDoor;
		return this;
	}

	public KeyBuilder(String keyString) {
		super();
		this.keyString = keyString;
		locationsReachableWithoutKey.add(this);
	}

	/**
	 * Add a location that will be reachable from start position without this key.
	 *
	 * @param location position to be/stay reachable
	 * @return this
	 */
	public KeyBuilder addReachableLocation(LocatedEntityBuilder location) {
		this.locationsReachableWithoutKey.add(location);
		return this;
	}

	public KeyBuilder addReachableLocations(Collection<? extends LocatedEntityBuilder> locations) {
		this.locationsReachableWithoutKey.addAll(locations);
		return this;
	}

	/**
	 * Add a location that will NOT be reachable from start position without this key.
	 *
	 * @param location position to be NOT reachable
	 * @return this
	 */
	public KeyBuilder addNonReachableLocation(LocationBuilder location) {
		this.locationsNotReachableWithoutKey.add(location);
		return this;
	}

	public String getKeyString() {
		return keyString;
	}

	public Collection<LocatedEntityBuilder> getLocationsReachableWithoutKey() {
		return locationsReachableWithoutKey;
	}

	public Collection<LocatedEntityBuilder> getLocationsNotReachableWithoutKey() {
		return locationsNotReachableWithoutKey;
	}

	@Override
	public String getIdentifier() {
		return "KEY_" + getKeyString();
	}

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		dungeon.getRoom(x, y).addItem(new Key(this.keyString));
	}
}
