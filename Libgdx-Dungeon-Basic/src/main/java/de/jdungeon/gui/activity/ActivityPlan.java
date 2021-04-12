package de.jdungeon.gui.activity;

import de.jdungeon.figure.action.Action;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public interface ActivityPlan<ACTIVITY extends Activity> {

	/**
	 * The origin activity that created this plan
	 *
	 * @return origin activity
	 */
	ACTIVITY getActivity();

	/**
	 * The next action the execute the plan
	 *
	 * @return next action
	 */
	Action getNextAction();

	/**
	 * true if plan is completed, i. e. all actions have been fetched
	 *
	 * @return true if completed, false otherwise
	 */
	boolean isCompleted();

	/**
	 * The length of the plan, i. e. number of action to be performed.
	 *
	 * @return length of the plan
	 */
	int getLength();

	/**
	 * The total amount of dust costs for all actions to be performed.
	 *
	 * @return amount of dust cost
	 */
	int totalDustCosts();
}
