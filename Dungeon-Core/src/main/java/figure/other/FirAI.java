package figure.other;

import java.util.LinkedList;
import java.util.List;

import ai.AttitudeDefaultHero;
import ai.AbstractAI;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.hero.HeroInfo;
import figure.percept.AttackPercept;
import figure.percept.FightEndedPercept;
import figure.percept.FleePercept;
import figure.percept.Percept;

public class FirAI extends AbstractAI {
	
	FigureInfo info;
	List<FigureInfo> attackers = new LinkedList<FigureInfo>();
	boolean fightEnded = false;
	
	public FirAI(FigureInfo firInfo) {
		super(new AttitudeDefaultHero());
		this.info = firInfo;
	}

	@Override
	public Action chooseFightAction() {
		RoomInfo roomInfo = info.getRoomInfo();
		PositionInRoomInfo pos = info.getPos();
		int lastIndex = pos.getLastIndex();
		int nextIndex = pos.getNextIndex();
		FigureInfo lastFigure = roomInfo.getPositionInRoom(lastIndex).getFigure();
		FigureInfo nextFigure = roomInfo.getPositionInRoom(nextIndex).getFigure();
		if(attackers.contains(lastFigure)) {
			return new AttackAction(info, lastFigure.getFighterID());
		}
		if(attackers.contains(nextFigure)) {
			return new AttackAction(info, nextFigure.getFighterID());
		}
		return new EndRoundAction();
	}

	@Override
	public Action chooseMovementAction() {
		// should not happen as Fir disappears at end of fight
		return null;
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if(f instanceof HeroInfo) {
			return false;
		}
		return true;
	}

	@Override
	public void setFigure(FigureInfo info) {
		this.info = info;
		
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processPercept(Percept p) {
		if(p instanceof AttackPercept) {
			AttackPercept attack = (AttackPercept)p;
			if(((AttackPercept) p).getVictim().equals(this.info)) {
				FigureInfo currentAttacker = attack.getAttacker();
				attackers.add(currentAttacker);
			}
		}
		if(p instanceof FleePercept) {
			if(((FleePercept)p).getFigure() instanceof HeroInfo) {
				fightEnded = true;
			}
		}
		if(p instanceof FightEndedPercept) {
			this.fightEnded = true;
		}
	}

}
