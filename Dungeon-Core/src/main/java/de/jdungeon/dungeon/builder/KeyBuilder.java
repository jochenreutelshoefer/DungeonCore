package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;

public class KeyBuilder {

	private final String keyString;

	private final Collection<LocationBuilder> locationsReachableWithoutKey = new HashSet<>();
	private final Collection<LocationBuilder> locationsNotReachableWithoutKey = new HashSet<>();

	public KeyBuilder(String keyString) {
		this.keyString = keyString;
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

	public Collection<LocationBuilder> getLocationsReachableWithoutKey() {
		return locationsReachableWithoutKey;
	}

	public Collection<LocationBuilder> getLocationsNotReachableWithoutKey() {
		return locationsNotReachableWithoutKey;
	}
}
