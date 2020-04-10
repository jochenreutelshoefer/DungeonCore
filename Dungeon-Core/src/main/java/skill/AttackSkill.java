package skill;

import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public class AttackSkill extends Skill<AttackSkill.AttackSkillAction> {

	@Override
	public ActionResult execute(AttackSkillAction a, boolean doIt, int round) {
		if (a.target == null) {
			return ActionResult.WRONG_TARGET;
		}
		if (a.attacker.getRoom().fightRunning()) {
			if (a.attacker.canPayActionPoints(1)) {
				if (a.attacker.getRoom() == a.target.getRoom()) {
					if (doIt) {
						a.attacker.payActionPoint(a, round);
						a.attacker.attack(a.target, round);
						return ActionResult.DONE;
					}
					return ActionResult.POSSIBLE;
				}
			}
			return ActionResult.NOAP;
		}
		return ActionResult.MODE;
	}

	@Override
	public AttackActionBuilder newAction() {
		return new AttackActionBuilder(this);
	}

	public static class AttackActionBuilder extends ActionBuilder<AttackSkillAction> {

		private final AttackSkill skill;
		private  FigureInfo attacker;
		private  FigureInfo target;

		public AttackActionBuilder(AttackSkill skill) {
			this.skill = skill;
		}

		public  AttackActionBuilder attacker(FigureInfo info) {
			this.attacker = info;
			return this;
		}

		public AttackActionBuilder target(FigureInfo target) {
			this.target = target;
			return this;
		}

		@Override
		public AttackSkillAction get() {
			return new AttackSkillAction(skill, attacker, target.getFighterID());
		}
	}

	public static class AttackSkillAction extends SkillAction {

		private final Figure attacker;
		private final Figure target;

		AttackSkillAction(AttackSkill skill, FigureInfo attackerInfo, int targetID) {
			super(skill);
			attacker = attackerInfo.getMap().getDungeon().getFigureIndex().get(attackerInfo.getFighterID());
			target = attackerInfo.getMap().getDungeon().getFigureIndex().get(targetID);
		}

	}
}
