package figure.action;

import figure.action.result.ActionResult;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.04.20.
 */
public abstract class AbstractExecutableAction extends Action {

	public abstract ActionResult handle(boolean doIt, int round);
}
