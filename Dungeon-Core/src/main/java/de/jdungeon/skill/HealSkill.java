package de.jdungeon.skill;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.04.20.
 */
public class HealSkill extends SimpleSkill {

	public HealSkill() {
		super(6);
	}

	@Override
	protected boolean isPossibleFight() {
		return true;
	}

	@Override
	protected boolean isPossibleNonFight() {
		return true;
	}

	@Override
	public ActionResult doExecute(SimpleSkillAction a, boolean doIt, int round) {
		if(doIt) {
			Figure actor = a.getActor();
			actor.heal(20, round);  // or whatever value ?
			actor.healPoisonings();
			actor.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("spell_heal_cast"), round));
			return ActionResult.DONE;
		}
		return ActionResult.POSSIBLE;
	}


}
