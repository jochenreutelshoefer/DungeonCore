package de.jdungeon.gui.activity;

import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.gui.Paragraphable;

import de.jdungeon.world.PlayerController;

/**
 * An activity is something the player can trigger on the UI to make
 * his player character perform an action or a sequence of actions.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public interface Activity<T> extends Paragraphable {

	T getObject();

	ActionResult plugToController(Object target);

	ActivityPlan createExecutionPlan(boolean doIt, Object target);

	ActionResult isCurrentlyPossible(Object target);

	ActionResult possible(Object target);

	PlayerController getPlayerController();

}
