package spell;

import figure.Figure;
import figure.percept.TextPercept;
import game.JDEnv;



public class Heal extends NoTargetSpell {
	

	public static int [][] values = { {9,5,7,30,1},
								{7,11,9,45,2}
								};
	
	private final boolean isPossibleNormal = true;
	private final boolean isPossibleInFight = true;
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public Heal(int l) {
		super(l,values[l-1]);
	}

	public Heal() {
		this.strength = 30;
		this.cost = 6;
	}

	@Override
	public int getType() {
		return AbstractSpell.SPELL_HEAL;
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_heal_text");
	}
	
	public Heal(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
	}
	
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_heal_name");
	}

	@Override
	public void sorcer(Figure mage, int round) {
				mage.heal(strength, round);
				mage.healPoisonings();
				String str = JDEnv.getResourceBundle().getString("spell_heal_cast");
				mage.tellPercept(new TextPercept(str, round));
	}

	
	
}

