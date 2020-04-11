package de.jdungeon.gui.activity;

import java.util.Collections;

import figure.action.result.ActionResult;
import skill.SimpleSkill;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.04.20.
 */
public class SimpleSkillActivity extends AbstractExecutableActivity<SimpleSkill> {

	private final PlayerController controller;
	private final SimpleSkill skill;

	public SimpleSkillActivity(PlayerController controller, SimpleSkill skill) {
		this.controller = controller;
		this.skill = skill;
	}

	@Override
	public void execute() {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		controller.getActionAssembler().plugActions(Collections.singletonList(skill.newActionFor(controller.getFigure()).get()));
	}

	@Override
	public boolean isCurrentlyPossible() {
		SimpleSkill.SimpleSkillAction simpleSkillTestAction = skill.newActionFor(controller.getFigure()).get();
		ActionResult testResult = skill.execute(simpleSkillTestAction, false, -1);
		return testResult.getSituation() == ActionResult.Situation.possible;
	}

	@Override
	public SimpleSkill getObject() {
		return skill;
	}
}
