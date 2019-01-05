package de.jdungeon.androidapp.screen;

import event.Event;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.19.
 */
public class InfoMessagePopupEvent extends Event {

	public String getMessage() {
		return message;
	}

	private final String message;

	public InfoMessagePopupEvent(String message) {
		this.message = message;
	}
}
