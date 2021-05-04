package de.jdungeon.dungeon.builder;

public class LocationsLeastDistanceConstraint {

	private final LocatedEntityBuilder locationA;
	private final LocatedEntityBuilder locationB;
	private final int minDistance;

	public LocationsLeastDistanceConstraint(LocatedEntityBuilder locationA, LocatedEntityBuilder locationB, int minDistance) {
		this.locationA = locationA;
		this.locationB = locationB;
		this.minDistance = minDistance;
	}

	public LocatedEntityBuilder getLocationA() {
		return locationA;
	}

	public LocatedEntityBuilder getLocationB() {
		return locationB;
	}

	public int getMinDistance() {
		return minDistance;
	}
}
