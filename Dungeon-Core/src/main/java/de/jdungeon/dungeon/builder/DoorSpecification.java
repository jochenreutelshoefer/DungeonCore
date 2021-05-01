package de.jdungeon.dungeon.builder;

import java.util.Collection;

public interface DoorSpecification {

	Collection<DoorMarker> getDoors();

	Collection<DoorMarker> getWalls();
}
