package event;

import figure.action.Action;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 29.12.16.
 */
public class ActionEvent extends Event {

	private final Action action;

	public ActionEvent(Action action) {
		this.action = action;
	}

	public Action getAction() {
		return action;
	}
}
