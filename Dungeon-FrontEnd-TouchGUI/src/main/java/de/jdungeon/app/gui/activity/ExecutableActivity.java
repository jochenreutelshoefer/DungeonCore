package de.jdungeon.app.gui.activity;

import figure.action.result.ActionResult;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public interface ExecutableActivity extends Activity {

	void execute();

	@Deprecated
	boolean isCurrentlyPossible();

	ActionResult possible();

}
