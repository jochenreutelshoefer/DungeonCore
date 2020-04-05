/*
 * Created on 05.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;

/**
 * Angriffsaktion. Enthaelt das Ziel.
 */
public class AttackAction extends AbstractExecutableAction {
	
	private final Figure target;
	private final Figure attacker;

	public AttackAction(FigureInfo attackerInfo, int targetID) {
		super();
		attacker = attackerInfo.getMap().getDungeon().getFigureIndex().get(attackerInfo.getFighterID());
		target = attackerInfo.getMap().getDungeon().getFigureIndex().get(targetID);
	}

	public AttackAction(Figure att, int targetID) {
		super();
		attacker = att;
		target = att.getActualDungeon().getFigureIndex().get(targetID);
	}

	public String toString() {
		return (this.getClass()+" :"+target);
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		if (target == null) {
			return ActionResult.WRONG_TARGET;
		}
		if (attacker.getRoom().fightRunning()) {
			if (attacker.canPayActionPoints(1)) {
				if (attacker.getRoom() == target.getRoom()) {
					if (doIt) {
						attacker.payActionPoint(this, round);
						attacker.attack(target, round);
						return ActionResult.DONE;
					}
					return ActionResult.POSSIBLE;
				}
			}
			return ActionResult.NOAP;
		}
		return ActionResult.MODE;
	}
}
