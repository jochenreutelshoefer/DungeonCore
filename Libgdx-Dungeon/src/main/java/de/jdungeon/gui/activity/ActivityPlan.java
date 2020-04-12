package de.jdungeon.gui.activity;

import figure.action.Action;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public interface ActivityPlan<ACTIVITY extends Activity> {

	ACTIVITY getActivity();

	Action getNextAction();

	boolean isCompleted();
}
