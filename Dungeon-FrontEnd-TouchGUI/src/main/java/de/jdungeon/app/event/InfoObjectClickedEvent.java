package de.jdungeon.app.event;

import de.jdungeon.event.Event;
import de.jdungeon.game.RoomInfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 02.01.17.
 */
public class InfoObjectClickedEvent extends Event {

	public RoomInfoEntity getClickedEntity() {
		return clickedEntity;
	}

	private final RoomInfoEntity clickedEntity;

	public InfoObjectClickedEvent(RoomInfoEntity clickedEntity) {
		this.clickedEntity = clickedEntity;
	}
}
