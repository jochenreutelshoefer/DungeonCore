package de.jdungeon.androidapp.event;

import dungeon.JDPoint;
import event.Event;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public class VisibilityIncreasedEvent extends Event {

	public JDPoint getPoint() {
		return point;
	}

	private final JDPoint point;

	public VisibilityIncreasedEvent(JDPoint point) {
		this.point = point;
	}
}
