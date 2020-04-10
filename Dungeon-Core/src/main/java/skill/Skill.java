package skill;

import figure.action.result.ActionResult;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class Skill<T extends SkillAction> {

	public abstract ActionResult execute(T action, boolean doIt, int round);

	public abstract <B extends ActionBuilder<T>> B newAction();

}
