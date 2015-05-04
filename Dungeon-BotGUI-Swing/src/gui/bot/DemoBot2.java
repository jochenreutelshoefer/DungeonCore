package gui.bot;

import java.util.List;

import dungeon.JDPoint;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.StepAction;
import figure.monster.MonsterInfo;
import figure.percept.Percept;

public class DemoBot2 extends AbstractHeroBotAI {

	@Override
	public Action chooseFightAction() {
		List<MonsterInfo> monsterInfos = hero.getRoomInfo().getMonsterInfos();
		return new AttackAction(monsterInfos.get(0).getFighterID());
	}

	@Override
	public Action chooseMovementAction() {
		int pos = (int) (Math.random() * 8);
		return new StepAction(pos);
	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processPercept(Percept p) {
		// TODO Auto-generated method stub

	}

}
