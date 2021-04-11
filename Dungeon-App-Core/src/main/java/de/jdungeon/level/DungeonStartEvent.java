package de.jdungeon.level;

import de.jdungeon.event.Event;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class DungeonStartEvent extends Event {

	public DungeonSelectedEvent getEvent() {
		return event;
	}

	DungeonSelectedEvent event;

	public DungeonStartEvent(DungeonSelectedEvent event) {
		this.event = event;
	}
}
