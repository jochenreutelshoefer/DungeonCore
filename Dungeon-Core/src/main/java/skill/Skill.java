package skill;

import figure.FigureInfo;
import figure.action.result.ActionResult;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class Skill<ACTION extends SkillAction> {

	private int dustCosts = 0;

	public Skill() {
	}

	public Skill(int dustCosts) {
		this.dustCosts = dustCosts;
	}

	public ActionResult execute(ACTION action, boolean doIt, int round) {
		if(! action.getActor().canPayDust(dustCosts)) {
			return ActionResult.DUST;
		}
		if(! action.getActor().canPayActionPoints(1)) {
			return ActionResult.NOAP;
		}

		// if done, pay dust costs
		ActionResult actionResult = doExecute(action, doIt, round);
		if(doIt && actionResult.getSituation() == ActionResult.Situation.done) {
			action.getActor().payDust(dustCosts);
		}
		return actionResult;
	}


	public abstract ActionResult doExecute(ACTION action, boolean doIt, int round);

	public abstract <BUILDER extends ActionBuilder<?, ACTION>> BUILDER newActionFor(FigureInfo actor);

}
