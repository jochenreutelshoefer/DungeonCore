package ai;

import java.io.Serializable;

import figure.Figure;
import figure.action.Action;
import figure.action.AttackAction;
import game.ActionSpecifier;

public abstract class AbstractReflexBehavior implements ActionSpecifier, Serializable {
	public static final int TYPE_FLEE = 1;
	public static final int TYPE_CONVINCED = 2;
	Figure f;
	int shockRounds = 0;
	boolean active = false;
	
	int actualType = 0;
	boolean raidAttacking = false;
	Figure raidTarget = null;
	Figure convincor = null;
	int convincedRounds = 0;
	
	public AbstractReflexBehavior(Figure f) {
		this.f = f;
	}

	@Override
	public abstract Action getAction(); 
	
	
	protected Action checkRaid() {
		if(raidAttacking) {
			raidAttacking = false;
			
			Action a = new AttackAction(f, raidTarget.getFigureID());
			raidTarget = null;
			return a;
		}
		return null;
	}

	public void setConvinced(Figure fig, int strength) {
		convincedRounds = strength / (1+1);
		this.convincor = fig;
		if(actualType == 0) {
			actualType = TYPE_CONVINCED;
		}
	}
	
	public void setRaidAttack(Figure target) {
		raidAttacking = true;
		raidTarget = target;
	}

}
