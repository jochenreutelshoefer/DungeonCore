package de.jdungeon.figure.other;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.ai.AbstractAI;
import de.jdungeon.ai.AttitudeDefaultHero;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.percept.AttackPercept;
import de.jdungeon.figure.percept.FightEndedPercept;
import de.jdungeon.figure.percept.FleePercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.skill.attack.AttackSkill;

public class FirAI extends AbstractAI {

	private FigureInfo info;
	private List<FigureInfo> attackers = new LinkedList<>();

	FirAI(FigureInfo firInfo) {
		super(new AttitudeDefaultHero());
		this.info = firInfo;
	}

	@Override
	public Action chooseFightAction() {
		RoomInfo roomInfo = info.getRoomInfo();
		PositionInRoomInfo pos = info.getPos();
		int lastIndex = pos.getPreviousIndex();
		int nextIndex = pos.getNextIndex();
		FigureInfo lastFigure = roomInfo.getPositionInRoom(lastIndex).getFigure();
		FigureInfo nextFigure = roomInfo.getPositionInRoom(nextIndex).getFigure();
		if (attackers.contains(lastFigure)) {
			return this.info.getSkill(AttackSkill.class)
					.newActionFor(info)
					.target(lastFigure)
					.get();
		}
		if (attackers.contains(nextFigure)) {
			return this.info.getSkill(AttackSkill.class)
					.newActionFor(info)
					.target(lastFigure)
					.get();
		}
		return new EndRoundAction();
	}

	@Override
	public Action chooseMovementAction() {
		// should not happen as Fir disappears at end of fight
		return new EndRoundAction();
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if (f instanceof HeroInfo) {
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
		if (p instanceof AttackPercept) {
			AttackPercept attack = (AttackPercept) p;
			if (((AttackPercept) p).getVictim(this.info).equals(this.info)) {
				FigureInfo currentAttacker = attack.getAttacker(info);
				attackers.add(currentAttacker);
			}
		}
	}
}
