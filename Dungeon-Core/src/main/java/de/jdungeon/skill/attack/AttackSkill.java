package de.jdungeon.skill.attack;

import de.jdungeon.dungeon.Position;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.AttackPercept;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.log.Log;
import de.jdungeon.skill.ActionBuilder;
import de.jdungeon.skill.Skill;
import de.jdungeon.skill.SkillAction;

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

		assert m != null;
		int dist = Position.getMinDistanceFromTo(actor.getPositionInRoom(),
				m.getPositionInRoom());

		float weaponBaseChance = actor.getActualChanceToHit(m);
		float rangedPrecision = precisionReductionByRange(weaponBaseChance, dist);
		float precision = (float) (rangedPrecision * (((double) actor.getActualRangeCapability(dist)) / 100));

		int value = damageReductionByRange(actor.getSlapStrength(m), dist);

		if (actor.raiding) {
			actor.raiding = false;
			actor.half_bonus = true;
			value += 4;
			precision *= 2;
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

		return new Slap(actor, value, precision);
	}

	public float precisionReductionByRange(float c, int dist) {

		if (dist == 1) {
			c = ((c * 100) / 100);
		}
		if (dist == 2) {
			c = ((c * 75) / 100);
		}
		if (dist == 3) {
			c = ((c * 50) / 100);
		}
		if (dist == 4) {
			c = 0;
		}
		return c;
	}

	public int damageReductionByRange(int rawDamage, int dist) {

		if (dist == 1) {
			return rawDamage;
		}
		if (dist == 2) {
			return (int) ((float) rawDamage * 0.8);
		}
		if (dist == 3) {
			return (int) ((float) rawDamage * 0.6);
		}
		if (dist == 4) {
			return (int) ((float) rawDamage * 0.4);
		}
		Log.warning("Invalid position distance for slap: " + dist);
		return rawDamage;
	}

	@Override
	public AttackActionBuilder newActionFor(FigureInfo actor) {
		return new AttackActionBuilder(this, actor);
	}

	public static class AttackActionBuilder extends ActionBuilder<AttackSkill, AttackSkillAction> {

		private FigureInfo target;

		private AttackActionBuilder(AttackSkill skill, FigureInfo attacker) {
			super(skill, attacker);
			this.skill = skill;
		}

		public AttackActionBuilder target(FigureInfo target) {
			if (target == null) {
				throw new IllegalArgumentException("target my not be null");
			}
			this.target = target;
			return this;
		}

		@Override
		public AttackSkillAction get() {
			return new AttackSkillAction(skill, actor, target.getFigureID());
		}
	}

	public static class AttackSkillAction extends SkillAction {

		private final Figure attacker;
		private final Figure target;

		AttackSkillAction(AttackSkill skill, FigureInfo attackerInfo, int targetID) {
			super(skill, attackerInfo);
			attacker = attackerInfo.getVisMap().getDungeon().getFigureIndex().get(attackerInfo.getFigureID());
			target = attackerInfo.getVisMap().getDungeon().getFigureIndex().get(targetID);
		}
	}
}
