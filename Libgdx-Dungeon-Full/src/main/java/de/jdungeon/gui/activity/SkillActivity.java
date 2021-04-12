package de.jdungeon.gui.activity;

import de.jdungeon.skill.Skill;

import de.jdungeon.world.PlayerController;

/**
 * An activity to trigger a de.jdungeon.skill
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public abstract class SkillActivity<SKILL extends Skill> extends AbstractExecutableActivity<SKILL> {

	public SkillActivity(PlayerController playerController) {
		super(playerController);
	}
}
