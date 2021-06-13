package de.jdungeon.dungeon.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Hall extends DefaultDoorSpecification {

	private Set<AbstractLocationBuilder> locations;

	public Hall(Set<AbstractLocationBuilder> locations) {
		super(new ArrayList<>(), new ArrayList<>());
		this.locations = locations;
	}

	public Hall(DoorSpecification doorSpecification, Set<AbstractLocationBuilder> locations) {
		super(doorSpecification.getDoors(), doorSpecification.getWalls());
		this.locations = locations;
	}

	public Hall(DoorSpecification doorSpecification, AbstractLocationBuilder location) {
		super(doorSpecification.getDoors(), doorSpecification.getWalls());
		this.locations = new HashSet<>();
		this.locations.add(location);
	}

	public Set<AbstractLocationBuilder> getLocations() {
		return locations;
	}
}
