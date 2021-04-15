package de.jdungeon.gui.activity;

import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.skill.SimpleSkill;

import de.jdungeon.skill.SimpleSkillAction;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.04.20.
 */
public class SimpleSkillActivity extends SkillActivity<SimpleSkill> {

	private final SimpleSkill skill;

	public SimpleSkillActivity(PlayerController controller, SimpleSkill skill) {
		super(controller);
		this.skill = skill;
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt, Object target) {
		return new SimpleActivityPlan(this, skill.newActionFor(playerController.getFigure()).get());
	}

	@Override
	public ActionResult possible(Object target) {
		SimpleSkillAction simpleSkillTestAction = skill.newActionFor(playerController.getFigure()).get();
		return skill.execute(simpleSkillTestAction, false, -1);
	}

	@Override
	public SimpleSkill getObject() {
		return skill;
	}
}
