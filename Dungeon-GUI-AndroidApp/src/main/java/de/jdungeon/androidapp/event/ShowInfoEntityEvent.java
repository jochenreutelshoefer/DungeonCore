package de.jdungeon.androidapp.event;

import event.Event;
import gui.Paragraphable;

import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class ShowInfoEntityEvent extends Event {

	private final Paragraphable infoEntity;

	public ShowInfoEntityEvent(Paragraphable infoEntity) {
		this.infoEntity = infoEntity;
	}

	public Paragraphable getInfoEntity() {
		return infoEntity;
	}
}
