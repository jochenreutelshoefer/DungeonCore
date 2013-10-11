package spell;

import fight.Poisoning;
import figure.Figure;
import game.JDEnv;

public class Poison extends Spell {

	public static int[][] values = { { 1, 1, 8, 8, 1 }, { 15, 13, 12, 25, 2 } };
	
	public static final String suffix = "poisoning";
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Poison(int level) {
		super(level, values[level-1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public Poison(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	public int getType() {
		return Spell.SPELL_POISONING;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	public String getText() {
		return JDEnv.getString("spell_"+suffix+"_text");
	}

	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}

	public void sorcer(Figure mage, Object target) {
		if(target instanceof Figure) {
			((Figure)target).poison(new Poisoning(mage,this.getStrength(),8));
		}

	}

public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

}
