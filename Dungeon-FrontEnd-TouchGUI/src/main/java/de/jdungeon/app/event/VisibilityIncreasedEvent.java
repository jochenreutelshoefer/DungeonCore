package de.jdungeon.app.event;

import java.util.ArrayList;
import java.util.List;

import dungeon.JDPoint;
import event.Event;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public class VisibilityIncreasedEvent extends Event {

	public List<JDPoint> getPoints() {
		return points;
	}

	private final List<JDPoint> points = new ArrayList<>();

	public VisibilityIncreasedEvent(JDPoint point) {
		this.points.add(point);
	}

	public VisibilityIncreasedEvent( List<JDPoint> points) {
		this.points.addAll(points);
	}
}
