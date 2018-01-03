package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
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

	private JDPoint lastHeroLocation = null;
	private int lastHeroLocationInfoRound;
	private final List<Percept> perceptList = new LinkedList<>();
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
		perceptList.add(p);
	}
	
	protected void processPercepts() {
		Collections.sort(perceptList, new PerceptComparator());
		for (Iterator<Percept> iter = perceptList.iterator(); iter.hasNext();) {
			Percept element = iter.next();
			if(element instanceof MovePercept) {
				if(((MovePercept)element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					this.lastHeroLocation = ((MovePercept)element).getTo().getPoint();
					lastHeroLocationInfoRound = element.getRound();
				}
			}
			if(element instanceof FleePercept) {
				if(((FleePercept)element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					int dir = ((FleePercept)element).getDir();
					RoomInfo r = ((FleePercept)element).getRoom();
					this.lastHeroLocation = r.getNeighbourRoom(dir).getNumber();
					lastHeroLocationInfoRound = element.getRound();
				}
			}
			if(element instanceof ScoutPercept) {
				if(((ScoutPercept)element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					int dir = ((ScoutPercept)element).getDir();
					RoomInfo r = ((ScoutPercept)element).getRoom();
					this.lastHeroLocation = r.getNeighbourRoom(dir).getNumber();
					lastHeroLocationInfoRound = element.getRound();
				}
			}
		}
		perceptList.clear();

	}
	
	@Override
	public Action chooseMovementAction() {
		
		processPercepts();
		
		if (!actionQueue.isEmpty()) {
			Action a = actionQueue.remove(0);
			lastAction = a;
			return a;
		}
		
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
	
	static class PerceptComparator implements Comparator<Percept> {
		
		@Override
		public int compare(Percept o1, Percept o2) {
			if(o1 != null && o2 != null) {
				if((o1).getRound() < (o2).getRound()) {
					return 1;
				} else if((o1).getRound() > (o2).getRound()) {
					return -1;
				}
				
			}
			return 0;
		}
		
	}

}
