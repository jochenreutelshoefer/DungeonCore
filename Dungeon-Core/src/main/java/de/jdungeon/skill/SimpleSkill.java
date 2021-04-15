package de.jdungeon.skill;

import de.jdungeon.figure.FigureInfo;

/**
 * A de.jdungeon.skill where an action does not need additional parameters to be executed,
 * hence only one generic parameter (actor) is required.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.04.20.
 */
public abstract class SimpleSkill extends Skill<SimpleSkillAction>{

	public SimpleSkill(int dustCosts) {
		super(dustCosts);
	}

	public SimpleSkill() {
		super(0);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public SimpleSkillActionBuilder newActionFor(FigureInfo actor) {
		return new SimpleSkillActionBuilder(this, actor, SimpleSkillAction.class);
	}

	@Override
	protected boolean checkPositionOk(SimpleSkillAction action) {
		// assumed position for SimpleSkills is arbitrary by default (overwrite otherwise)
		return true;
	}

	@Override
	protected boolean checkDistanceOk(SimpleSkillAction action) {
		// as we dont deal with a target here, the distance to target is okay.
		return true;
	}

}
