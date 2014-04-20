package gui.bot;

import java.util.List;

import ai.AI;
import dungeon.JDPoint;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import figure.percept.Percept;

public class MyDemoBot extends AI {

	private HeroInfo hero;

	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			this.hero = ((HeroInfo) f);
		}

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return f instanceof MonsterInfo;
	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}


	@Override
	public Action chooseFightAction() {
		delay();
		List<MonsterInfo> monsterInfos = hero.getRoomInfo().getMonsterInfos();

		return new AttackAction(monsterInfos.get(0).getFighterID());
	}

	private void delay() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Action chooseMovementAction() {
		delay();
		int[] roomDoors = hero.getRoomDoors();
		int dir = 0;
		for (int i : roomDoors) {
			if (i == 2 | i == 1) {
				break;
			}
			dir++;
		}
		int dirPos = Figure.getDirPos(dir + 1);
		if (hero.getPositionInRoomIndex() == dirPos) {
			return new MoveAction(dir + 1);

		} else {
			if (dirPos != -1) {
				return new StepAction(dirPos);

			} else {
				return new EndRoundAction();
			}
		}

	}

	@Override
	protected void processPercept(Percept p) {
		// TODO Auto-generated method stub

	}


}
