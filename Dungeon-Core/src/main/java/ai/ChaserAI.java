package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.util.DungeonUtils;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.hero.HeroInfo;
import figure.percept.FleePercept;
import figure.percept.MovePercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;

public class ChaserAI extends DefaultMonsterIntelligence {


	private HeroPositionLog heroLog = new HeroPositionLog();


	@Override
	public Action chooseFightAction() {
		
		Action a = this.stepToEnemy();
		if(a != null && Math.random() > 0.3) {
			return a;
		}
		
		a = this.wannaSpell();
		if(a != null) {
			return a;
		}
		
		return Action.makeActionAttack(getHeroIndex());
		
	}
	
	@Override
	public void processPercept(Percept p) {
		heroLog.tellPecept(p);

	}
	
	@Override
	public Action chooseMovementAction() {
		
		heroLog.processPercepts();
		
		if (!actionQueue.isEmpty()) {
			Action a = actionQueue.remove(0);
			lastAction = a;
			return a;
		}

		JDPoint lastHeroLocation = heroLog.getLastHeroPosition();
		if(lastHeroLocation == null) {
			return new EndRoundAction();
		}
		if(lastHeroLocation.equals(monster.getRoomNumber())) {
			return new EndRoundAction();
		}
		
		List<JDPoint> l = monster.getShortestWayFromTo(monster.getRoomNumber(),
				lastHeroLocation);
		if(l.size() > 1) {
		JDPoint p = l.get(1);
		
		//System.out.println(p.toString());
			int dir = DungeonUtils.getNeighbourDirectionFromTo(
					monster.getRoomNumber(), p).getValue();
		return walk(dir);
		}
		
		return new EndRoundAction();
	}


}
