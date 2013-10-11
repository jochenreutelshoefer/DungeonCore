package spell;

import figure.Figure;
import game.JDEnv;

public class Cobweb extends Spell implements TargetSpell{
	
	public static int[][] values = { { 1, 1, 10, 30,1 }, { 15, 13, 12, 25,1 } };
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Cobweb(int level) {
		super(level, values[level-1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public Cobweb(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	
	public void sorcer(Figure f, Object target) {
		
		((Figure)target).setCobwebbed(this.level * this.getStrength());
	}
	
	public int getLernCost() {
		return 1;
	}
	
	public String getName() {
		return JDEnv.getString("spell_net_name");
	}
	
	public int getType() {
		return Spell.SPELL_COBWEB;
	}
	
	public boolean isApplicable(Figure f, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_net_text");
		return s;
	}

}
