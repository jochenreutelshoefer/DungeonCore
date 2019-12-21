package de.jdungeon.app.gui.activity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public interface ExecutableActivity extends Activity {

	void execute();

	boolean isCurrentlyPossible();

}
