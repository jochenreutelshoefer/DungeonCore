package figure.other;

import java.util.Iterator;
import java.util.List;

import ai.AI;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.DoNothingAction;
import figure.action.MoveAction;
import figure.percept.MovePercept;
import figure.percept.Percept;

public class LionessAI extends AI {

	FigureInfo master;
	RoomInfo currentWalkTarget = null;
	
	public LionessAI(FigureInfo info, FigureInfo master) {
		this.info = info;
		this.master = master;
	}

	@Override
	public Action chooseFightAction() {
		List<FigureInfo> figureInfos = info.getRoomInfo().getFigureInfos();
		Iterator<FigureInfo> iterator = figureInfos.iterator();
		for (FigureInfo fighter : figureInfos) {
			if(!master.isHostile(fighter)) {
				iterator.remove();
			}
		}
		FigureInfo target = figureInfos.get(((int)(Math.random() * figureInfos.size())));
		
		return new AttackAction(target.getFighterID());
	}

	@Override
	public Action chooseMovementAction() {
		if(master.getRoomNumber().equals(info.getRoomNumber())) return new DoNothingAction();
		
		if(currentWalkTarget != null) {
			int dir = new RouteInstruction(currentWalkTarget).getWay(info.getRoomInfo());
			return new MoveAction(dir);
		}
		return null;
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return master.isHostile(f);
	}

	@Override
	public void setFigure(FigureInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processPercept(Percept p) {
		if(master.getRoomNumber().equals(info.getRoomNumber())) return;
		if(p instanceof MovePercept) {
			MovePercept movement = ((MovePercept)p);
			FigureInfo figure = movement.getFigure();
			if(figure.equals(master)) {
				RoomInfo target = movement.getTo();
				currentWalkTarget = target;
			}
		}

	}

}
