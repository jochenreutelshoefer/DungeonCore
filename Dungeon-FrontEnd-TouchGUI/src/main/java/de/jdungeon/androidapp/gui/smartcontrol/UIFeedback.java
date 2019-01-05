package de.jdungeon.androidapp.gui.smartcontrol;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.19.
 */
public enum UIFeedback {

	SelectEnemy("Gegner auswählen");

	public String getMessage() {
		return message;
	}

	private final String message;

	UIFeedback(String message) {
		this.message = message;
	}
}
