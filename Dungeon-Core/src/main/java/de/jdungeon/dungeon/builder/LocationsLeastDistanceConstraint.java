package de.jdungeon.dungeon.builder;

public class LocationsLeastDistanceConstraint {

	private final LocationBuilder locationA;
	private final LocationBuilder locationB;
	private final int minDistance;

	public LocationsLeastDistanceConstraint(LocationBuilder locationA, LocationBuilder locationB, int minDistance) {
		this.locationA = locationA;
		this.locationB = locationB;
		this.minDistance = minDistance;
	}

	public LocationBuilder getLocationA() {
		return locationA;
	}

	public LocationBuilder getLocationB() {
		return locationB;
	}

	public int getMinDistance() {
		return minDistance;
	}
}
