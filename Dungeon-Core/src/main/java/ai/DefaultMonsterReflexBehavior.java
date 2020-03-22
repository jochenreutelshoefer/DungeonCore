package ai;

import java.util.LinkedList;
import java.util.List;

import dungeon.Position;
import dungeon.Room;
import dungeon.util.DungeonUtils;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.action.StepAction;
import figure.monster.Monster;
import figure.monster.MonsterInfo;

public class DefaultMonsterReflexBehavior extends AbstractReflexBehavior{

	private final Monster m;

	public DefaultMonsterReflexBehavior(Monster m) {
		super(m);
		this.m = m;
	}
	
	protected Action getMovementAction() {
		Action a = new EndRoundAction();
		if(shockRounds > 0){
			Action b = stepAwayFromDoor();
			if(b != null) {
				a = b;
			}
		}
		
		this.convincedRounds = 0;
		this.shockRounds = 0;
		
		return a;
	}
	
	private Action stepAwayFromDoor() {
		Room other = m.getLastFrighener().getRoom();
		Room here = m.getRoom();
		int dir = DungeonUtils.getNeighbourDirectionFromTo(here, other)
				.getValue();
		int pos = getDoorPos(dir);
		if(m.getPositionInRoom() == pos) {
			Position p = getRandomFreePos();
			if(p != null) {
				return new StepAction(p.getIndex());
			}
			
		}
		return null;
	}
	
	private Position getRandomFreePos() {
		Position [] possis = m.getRoom().getPositions();
		List<Position> l = new LinkedList<Position>();
		for (int i = 0; i < possis.length; i++) {
			if(possis[i].getFigure() == null) {
				l.add(possis[i]);
			}
		}
		
		Position p = null;
		if(l.size() > 0) {
			p = l.get((int)(Math.random()*l.size()));
		}
		return p;
	}
	
	private int getDoorPos(int dir) {
		if(dir == 1) {
			return 1;
		}
		if(dir == 2) {
			return 3;
		}
		if(dir == 3) {
			return 5;
		}
		if(dir == 4) {
			return 7;
		}
		System.out.println("no valid direction given! DefaultMonsterReflexBehavior.getDoorPos()");
		return -1;
	}
	
	protected boolean testExtension() {
		if(Math.random() > 0.5) {
			shockRounds += 1;
			return true;
		}
		return false;
	}
	
	private Action getShockAction() {
		if(shockRounds > 0) {
			//l�uft
			active = true;
			shockRounds--;
			if(m.getRoom().fightRunning()) {
				return getFleeAction();
			}else {
				return getMovementAction();
			}
		}else {
			
			if(active){
				//vielleicht beenden
				if(testExtension()) {
					//weiter
					shockRounds--;
					if(m.getRoom().fightRunning()) {
						return getFleeAction();
					}else {
						return getMovementAction();
					}
				}else {
					//beenden
					active = false;
					shockRounds = 0;
					return null;
				}
			}else {
				//l�uft nicht
				return null;
			}
		}
	}
	

	@Override
	public Action getAction() {
		Action a = null;
		a = getShockAction();
		if(a != null) {
			return a;
		}
		a = getConvincedAction();
		if(a != null) {
			return a;
		}
		
		return null;
		
	}
	

	
	protected Action getConvincedAction() {
		if(this.convincedRounds > 0) {
			convincedRounds--;
			if(f.getRoom().fightRunning()) {
				List<Figure> l = f.getRoom().getRoomFigures();
				Figure fig = null;
				if(l.size() > 2) {	
					while(fig == null || fig == f || fig == this.convincor) {
						fig = l.get((int)(Math.random()*l.size()));
					}
					return new AttackAction(fig.getFigureID());
				}else {
					Action a = getFleeAction();
					if(a != null) {
						return a;
					}else {
						if(f.getRoom().getRoomFigures().contains(this.convincor)) {
							return new AttackAction(this.convincor.getFigureID());
						}else {
							convincedRounds = 0;
							return null;
						}
					}
				}
				
			}else {
				return new EndRoundAction();
			}
		}
		return null;
	}
	
	protected Action getFleeAction() {
		if(this.actualType == AbstractReflexBehavior.TYPE_FLEE) {
			Action a = DefaultMonsterIntelligence.getFleeAction((MonsterInfo)(m.makeInfoObject(m.getRoomVisibility())));
			if(a != null) {
				return a;
			} else {
				return DefaultMonsterIntelligence.getStepAwayAction((MonsterInfo)(m.makeInfoObject(m.getRoomVisibility())),FigureInfo.makeFigureInfo(m.getLastFrighener(),m.getRoomVisibility()));
			}
		}
		return null;
	}
}
