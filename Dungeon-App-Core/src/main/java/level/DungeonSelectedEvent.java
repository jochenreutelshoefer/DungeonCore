package level;

import org.w3c.dom.events.Event;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.04.16.
 */
public class DungeonSelectedEvent extends event.Event {

	private DungeonFactory dungeon;

	public DungeonSelectedEvent(DungeonFactory dungeon) {
		this.dungeon = dungeon;
	}

	public DungeonFactory getDungeon() {
		return dungeon;
	}
}