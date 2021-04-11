package de.jdungeon.app.event;

import de.jdungeon.event.Event;
import de.jdungeon.gui.Paragraphable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class FocusEvent extends Event {

	private final Paragraphable infoEntity;

	private final Object source;

	public FocusEvent(Paragraphable infoEntity, Object source) {
		this.infoEntity = infoEntity;
		this.source = source;
	}

	public Paragraphable getObject() {
		return infoEntity;
	}

	public Object getSource() {
		return source;
	}
}
