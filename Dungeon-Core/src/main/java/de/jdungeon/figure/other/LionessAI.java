package de.jdungeon.figure.other;

import java.util.Iterator;
import java.util.List;

import de.jdungeon.ai.AttitudeDefaultHero;
import de.jdungeon.ai.AbstractAI;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.action.MoveAction;
import de.jdungeon.figure.percept.EntersPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.skill.attack.AttackSkill;

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
			int dir = new RouteInstruction(currentWalkTarget.getNumber()).getWay(info.getRoomInfo(), this.master.getVisMap());
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
			FigureInfo figure = movement.getFigure(this.info);
			if(figure.equals(master)) {
				currentWalkTarget = movement.getTo(this.info);
			}
		}

	}

}
