package de.jdungeon.figure.percept;

import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.location.Location;
import de.jdungeon.location.LocationInfo;
import de.jdungeon.location.LocationState;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.05.20.
 */
public class LocationStateChangePercept extends OpticalPercept {

	private final Location location;
	private final LocationState oldState;
	private final LocationState newState;

	public LocationStateChangePercept(Location location, Room room, LocationState oldState, LocationState newState, int round) {
		super(room.getNumber(), round);
		this.location = location;
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return Collections.emptyList();
	}

	public LocationInfo getLocation(FigureInfo perceiver) {
		return LocationInfo.makeLocationInfo(this.location, perceiver.getMap());
	}
}
