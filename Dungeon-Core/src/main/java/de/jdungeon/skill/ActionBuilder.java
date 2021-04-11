package de.jdungeon.skill;

import de.jdungeon.figure.FigureInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class ActionBuilder<SKILL extends Skill, ACTION extends SkillAction> {

	protected final FigureInfo actor;
	protected SKILL skill;

	protected ActionBuilder(SKILL skill, FigureInfo actor) {
		this.actor = actor;
		this.skill = skill;
	}

	public abstract ACTION get();
}
