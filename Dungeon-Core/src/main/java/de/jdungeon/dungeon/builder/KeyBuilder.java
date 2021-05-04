package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.item.Key;

public class KeyBuilder extends AbstractLocationBuilder {

	private final String keyString;

	private final Collection<AbstractLocationBuilder> locationsReachableWithoutKey = new HashSet<>();
	private final Collection<AbstractLocationBuilder> locationsNotReachableWithoutKey = new HashSet<>();

	public KeyBuilder(String keyString, int x, int y) {
		super(new JDPoint(x, y));
		this.keyString = keyString;
		locationsReachableWithoutKey.add(this);
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
	public KeyBuilder addReachableLocation(LocationBuilder location) {
		this.locationsReachableWithoutKey.add(location);
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

	public Collection<AbstractLocationBuilder> getLocationsReachableWithoutKey() {
		return locationsReachableWithoutKey;
	}

	public Collection<AbstractLocationBuilder> getLocationsNotReachableWithoutKey() {
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
