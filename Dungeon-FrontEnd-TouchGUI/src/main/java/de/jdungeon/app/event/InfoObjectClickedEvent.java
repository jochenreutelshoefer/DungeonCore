package de.jdungeon.app.event;

import event.Event;
import game.RoomInfoEntity;

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
