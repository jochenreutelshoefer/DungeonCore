package de.jdungeon.androidapp.event;

import event.Event;
import game.InfoEntity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 02.01.17.
 */
public class InfoObjectClickedEvent extends Event {

	public InfoEntity getClickedEntity() {
		return clickedEntity;
	}

	private final InfoEntity clickedEntity;

	public InfoObjectClickedEvent(InfoEntity clickedEntity) {
		this.clickedEntity = clickedEntity;
	}
}
