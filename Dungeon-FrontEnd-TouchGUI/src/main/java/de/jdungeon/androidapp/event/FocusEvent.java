package de.jdungeon.androidapp.event;

import event.Event;
import gui.Paragraphable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class FocusEvent extends Event {

	private final Paragraphable infoEntity;

	public FocusEvent(Paragraphable infoEntity) {
		this.infoEntity = infoEntity;
	}

	public Paragraphable getObject() {
		return infoEntity;
	}
}
