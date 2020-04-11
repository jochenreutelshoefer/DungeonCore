package figure.other;

import java.util.Iterator;
import java.util.List;

import ai.AttitudeDefaultHero;
import ai.AbstractAI;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.percept.EntersPercept;
import figure.percept.Percept;
import skill.AttackSkill;

public class LionessAI extends AbstractAI {

	FigureInfo master;
	RoomInfo currentWalkTarget = null;
	
	public LionessAI(FigureInfo info, FigureInfo master) {
		super(new AttitudeDefaultHero());
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
		return this.info.getSkill(AttackSkill.class)
				.newActionFor(info)
				.target(target)
				.get();
	}

	@Override
	public Action chooseMovementAction() {
		if(master.getRoomNumber().equals(info.getRoomNumber())) return new EndRoundAction();
		
		if(currentWalkTarget != null) {
			int dir = new RouteInstruction(currentWalkTarget.getNumber()).getWay(info.getRoomInfo(), this.master.getMap());
			return new MoveAction(this.info, this.info.getRoomNumber(), dir);
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
	public void notifyVisibilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processPercept(Percept p) {
		if(master.getRoomNumber().equals(info.getRoomNumber())) return;
		if(p instanceof EntersPercept) {
			EntersPercept movement = ((EntersPercept)p);
			FigureInfo figure = movement.getFigure();
			if(figure.equals(master)) {
				currentWalkTarget = movement.getTo();
			}
		}

	}

}
