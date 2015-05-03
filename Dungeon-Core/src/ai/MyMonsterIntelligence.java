/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ai;

import java.util.List;

import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.action.Action;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;

public class MyMonsterIntelligence extends
		figure.control.AbstractMonsterFightIntelligence implements
		MovementIntelligence {

	public MyMonsterIntelligence(MonsterInfo m) {
		super(m);
	}

	public Action chooseMovementAction() {
		return null;
	}

	public Action chooseFightAction() {
		int monsterCount = monster.getRoomInfo().getFigureInfos().size();
		List infos = monster.getRoomInfo().getFigureInfos();
		int heroIndex = -1;
		for (int i = 0; i < infos.size(); i++) {
			if (infos.get(i) instanceof HeroInfo) {
				heroIndex = i;
			}
		}
		int healthLevel = monster.getHealthLevel();
		if (monsterCount == 1) {
			// if(healthLevel <= Fighter.WOUNDED) {

			if (healthLevel <= Figure.STATUS_HEALTHY && Math.random() < 0.2) {
				return Action.makeActionFlee();
			} else {
				return Action
						.makeActionAttack(/* monster.getFighterID(), */heroIndex);
			}
		}
		if (monsterCount == 2) {
			if (healthLevel <= Figure.STATUS_CRITICAL) {
				return Action.makeActionFlee();
			} else {
				return Action
						.makeActionAttack(/* monster.getFighterID(), */heroIndex);
			}
		}
		if (monsterCount > 2) {
			return Action
					.makeActionAttack(/* monster.getFighterID(), */heroIndex);
		}
		return Action.makeActionAttack(/* monster.getFighterID(), */heroIndex);
	}

	private int getFleeDirection() {
		int last = monster.getLastMove();
		int back = RouteInstruction.turnOpp(last);
		return back;
	}
}
