package gui.bot;

import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import figure.percept.Percept;
import game.ControlUnit;
import game.JDGUI;

import java.util.List;

import dungeon.JDPoint;

public class MyDemoBot implements ControlUnit {

	private HeroInfo info;
	private JDGUI gui;

	@Override
	public Action getAction() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(info.getRoomInfo().fightRunning()) {
			List<MonsterInfo> monsterInfos = info.getRoomInfo().getMonsterInfos();

			return new AttackAction(monsterInfos.get(0).getFighterID());
		}

		int[] roomDoors = info.getRoomDoors();
		int dir = 0;
		for (int i : roomDoors) {
			if (i == 2 | i == 1) {
				break;
			}
			dir++;
		}
		int dirPos = Figure.getDirPos(dir + 1);
		if (info.getPositionInRoomIndex() == dirPos) {
			return new MoveAction(dir + 1);

		} else {
			if (dirPos != -1) {
				return new StepAction(dirPos);

			} else {
				return new EndRoundAction();
			}
		}


		// return new EndRoundAction();
	}

	@Override
	public void actionDone(Action a, ActionResult res) {
		// TODO Auto-generated method stub

	}

	@Override
	public void tellPercept(Percept p) {
		if (gui != null) {
			gui.tellPercept(p);
		}

	}

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameOver() {
		System.out.println("Erreichte Punktzahl: " + info.getTotalExp());
	}

	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			this.info = ((HeroInfo) f);
		}

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetingRoomVisibility(JDPoint p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMonitoringGUI(JDGUI gui) {
		this.gui = gui;

	}

}
