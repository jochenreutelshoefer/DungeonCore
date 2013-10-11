package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.hero.HeroInfo;
import figure.percept.FleePercept;
import figure.percept.MovePercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;

public class VimAI extends DefaultMonsterIntelligence {

	JDPoint lastHeroLocation = null;
	List perceptList = new LinkedList();
	public Action chooseFightAction() {
		
		Action a = this.stepToEnemy();
		if(a != null) {
			return a;
		}
		
		a = this.wannaSpell();
		if(a != null) {
			return a;
		}
		
		return Action.makeActionAttack(getHeroIndex());
		
	}
	
	public void processPercept(Percept p) {
		perceptList.add(p);
	}
	
	protected void processPercepts() {
		Collections.sort(perceptList, new PerceptComparator());
		for (Iterator iter = perceptList.iterator(); iter.hasNext();) {
			Percept element = (Percept) iter.next();
			if(element instanceof MovePercept) {
				if(((MovePercept)element).getFigure() instanceof HeroInfo) {
					this.lastHeroLocation = ((MovePercept)element).getTo().getPoint();
					return;
				}
			}
			if(element instanceof FleePercept) {
				if(((FleePercept)element).getFigure() instanceof HeroInfo) {
					int dir = ((FleePercept)element).getDir();
					RoomInfo r = ((FleePercept)element).getRoom();
					this.lastHeroLocation = r.getNeighbourRoom(dir).getNumber();
					return;
				}
			}
			if(element instanceof ScoutPercept) {
				if(((ScoutPercept)element).getFigure() instanceof HeroInfo) {
					int dir = ((ScoutPercept)element).getDir();
					RoomInfo r = ((ScoutPercept)element).getRoom();
					this.lastHeroLocation = r.getNeighbourRoom(dir).getNumber();
					return;
				}
			}
		}
		
		perceptList.clear();
	}
	
	public Action chooseMovementAction() {
		
		processPercepts();
		
		if (actionQueue.size() > 0) {
			Action a = (Action) actionQueue.remove(0);
			lastAction = a;
			return a;
		}
		
		if(lastHeroLocation == null) {
			return new EndRoundAction();
		}
		if(lastHeroLocation.equals(monster.getRoomNumber())) {
			return new EndRoundAction();
		}
		
		List l = monster.getShortestWayFromTo(monster.getRoomNumber(),lastHeroLocation);
		if(l.size() > 1) {
		JDPoint p = (JDPoint)l.get(1);
		
		//System.out.println(p.toString());
		int dir = Dungeon.getNeighbourDirectionFromTo(monster.getRoomNumber(),p);
		return walk(dir);
		}
		
		return new EndRoundAction();
	}
	
	class PerceptComparator implements Comparator {
		
		public int compare(Object o1, Object o2) {
			if(o1 instanceof Percept && o2 instanceof Percept) {
				Percept p1 = ((Percept)o1);
				Percept p2 = ((Percept)o2);
				if(p1.getRound() < p2.getRound()) {
					return 1;
				}else if(p1.getRound() > p2.getRound()) {
					return -1;
				}
				
			}
			return 0;
		}
		
	}

}
