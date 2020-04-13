package de.jdungeon.gui.activity;

import figure.action.result.ActionResult;
import skill.SimpleSkill;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
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
	public ActivityPlan createExecutionPlan(boolean doIt) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		return new SimpleActivityPlan(this, skill.newActionFor(playerController.getFigure()).get());
	}

	@Override
	public ActionResult possible() {
		SimpleSkill.SimpleSkillAction simpleSkillTestAction = skill.newActionFor(playerController.getFigure()).get();
		return skill.execute(simpleSkillTestAction, false, -1);
	}

	@Override
	public SimpleSkill getObject() {
		return skill;
	}
}
