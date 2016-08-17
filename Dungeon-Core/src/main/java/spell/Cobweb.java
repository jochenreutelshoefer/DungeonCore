package spell;

import figure.Figure;
import figure.FigureInfo;
import game.InfoEntity;
import game.JDEnv;

public class Cobweb extends Spell implements TargetSpell{
	
	public static int[][] values = { { 1, 1, 10, 30,1 }, { 15, 13, 12, 25,1 } };
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
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
	
	
	
	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	
	@Override
	public void sorcer(Figure f, Object target) {
		
		((Figure)target).setCobwebbed(this.level * this.getStrength());
	}
	
	@Override
	public int getLernCost() {
		return 1;
	}
	
	@Override
	public String getName() {
		return JDEnv.getString("spell_net_name");
	}
	
	@Override
	public int getType() {
		return Spell.SPELL_COBWEB;
	}
	
	@Override
	public boolean isApplicable(Figure f, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_net_text");
		return s;
	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

}
