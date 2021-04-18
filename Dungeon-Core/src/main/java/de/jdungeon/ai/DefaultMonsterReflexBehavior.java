package de.jdungeon.ai;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Position;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.skill.attack.AttackSkill;

public class DefaultMonsterReflexBehavior extends AbstractReflexBehavior{

	private final Monster m;

	public DefaultMonsterReflexBehavior(Monster m) {
		super(m);
		this.m = m;
	}
	
	protected Action getMovementAction() {
		Action a = new EndRoundAction();

		this.convincedRounds = 0;
		
		return a;
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
					return this.f.getSkill(AttackSkill.class)
							.newActionFor(FigureInfo.makeFigureInfo(f, f.getViwMap()))
							.target(FigureInfo.makeFigureInfo(fig, f.getViwMap()))
							.get();
				} else {
					Action a = getFleeAction();
					if(a != null) {
						return a;
					}else {
						if(f.getRoom().getRoomFigures().contains(this.convincor)) {
							return this.f.getSkill(AttackSkill.class)
									.newActionFor(FigureInfo.makeFigureInfo(f, f.getViwMap()))
									.target(FigureInfo.makeFigureInfo(this.convincor, f.getViwMap()))
									.get();
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
			Action a = DefaultMonsterIntelligence.getFleeAction((MonsterInfo)(m.makeInfoObject(m.getViwMap())));
			if(a != null) {
				return a;
			}
		}
		return null;
	}
}
