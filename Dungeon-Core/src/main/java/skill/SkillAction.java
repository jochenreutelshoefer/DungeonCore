package skill;

import figure.action.Action;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class SkillAction extends Action {

	private final Skill skill;

	public SkillAction(Skill skill) {
		this.skill = skill;
	}

	public Skill getSkill() {
		return skill;
	}
}
