package de.jdungeon.androidapp.event;

import event.Event;
import game.InfoEntity;
import game.RoomEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 02.01.17.
 */
public class InfoObjectClickedEvent extends Event {

	public RoomEntity getClickedEntity() {
		return clickedEntity;
	}

	private final RoomEntity clickedEntity;

	public InfoObjectClickedEvent(RoomEntity clickedEntity) {
		this.clickedEntity = clickedEntity;
	}
}
