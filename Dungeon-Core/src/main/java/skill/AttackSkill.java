package skill;

import org.jetbrains.annotations.NotNull;
import dungeon.Position;
import fight.Slap;
import fight.SlapResult;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.percept.AttackPercept;
import figure.percept.TextPercept;
import game.JDEnv;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public class AttackSkill extends Skill<AttackSkill.AttackSkillAction> {

	@Override
	protected boolean checkPositionOk(AttackSkillAction action) {
		return true;
	}

	@Override
	protected boolean checkDistanceOk(AttackSkillAction action) {
		return true;
	}

	@Override
	protected boolean isPossibleFight() {
		return true;
	}

	@Override
	protected boolean isPossibleNonFight() {
		return false;
	}

	@Override
	public ActionResult doExecute(AttackSkillAction a, boolean doIt, int round) {
		if (a.target == null) {
			return ActionResult.WRONG_TARGET;
		}
		if (a.attacker.getRoom().fightRunning()) {
			if (a.attacker.canPayActionPoints(1)) {
				if (a.attacker.getRoom() == a.target.getRoom()) {
					if (doIt) {
						a.attacker.payActionPoint(a, round);
						attack(a.getActor(), a.target, round);
						return ActionResult.DONE;
					}
					return ActionResult.POSSIBLE;
				}
			}
			return ActionResult.NOAP;
		}
		return ActionResult.MODE;
	}

	private void attack(Figure actor, Figure op, int round) {
		// new look dir towards opponent
		actor.setLookDir(Position.getDirFromTo(actor.getPositionInRoom(),
				op.getPositionInRoom()));

		Slap u = slay(actor, op, round);

		actor.getRoom().distributePercept(new AttackPercept(actor, op, u, round));
		SlapResult res = op.getSlap(u, round);

		actor.receiveSlapResult(res); // todo: refactor : unify and extract to here
	}

	private Slap slay(Figure actor, Figure m, int round) {

		double tumbleFactor = 1;

		assert m != null;
		int dist = Position.getMinDistanceFromTo(actor.getPositionInRoom(),
				m.getPositionInRoom());

		float weaponBaseChance = actor.getActualChanceToHit(m);
		float rangedPrecision = actor.rangeFilter(weaponBaseChance, dist);
		float precision = (float) (rangedPrecision * (((double) actor.getActualRangeCapability(dist)) / 100));

		int value = actor.getSlapStrength(m);

		if (actor.raiding) {
			actor.raiding = false;
			actor.half_bonus = true;
			value += 4;
			precision *= 2;
			tumbleFactor = 4;
			actor.tellPercept(new TextPercept(JDEnv.getResourceBundle().getString("raiding_attack"), round));
		}

		if (actor.half_bonus) {
			actor.half_bonus = false;
			value *= 1.5;
		}
		if (actor.double_bonus) {
			actor.double_bonus = false;
			value *= 2;
		}

		return new Slap(actor, value, (int) (tumbleFactor * actor.getTumbleValue(m)), precision);
	}

	@Override
	public AttackActionBuilder newActionFor(FigureInfo actor) {
		return new AttackActionBuilder(this, actor);
	}

	public static class AttackActionBuilder extends ActionBuilder<AttackSkill, AttackSkillAction> {

		private FigureInfo target;

		private AttackActionBuilder(AttackSkill skill, @NotNull FigureInfo attacker) {
			super(skill, attacker);
			this.skill = skill;
		}

		public AttackActionBuilder target(FigureInfo target) {
			if(target == null) {
				throw new IllegalArgumentException("target my not be null");
			}
			this.target = target;
			return this;
		}

		@Override
		public AttackSkillAction get() {
			return new AttackSkillAction(skill, actor, target.getFighterID());
		}
	}

	public static class AttackSkillAction extends SkillAction {

		private final Figure attacker;
		private final Figure target;

		AttackSkillAction(AttackSkill skill, @NotNull FigureInfo attackerInfo, int targetID) {
			super(skill, attackerInfo);
			attacker = attackerInfo.getMap().getDungeon().getFigureIndex().get(attackerInfo.getFighterID());
			target = attackerInfo.getMap().getDungeon().getFigureIndex().get(targetID);
		}
	}
}
