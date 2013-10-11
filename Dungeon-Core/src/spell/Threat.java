package spell;

import spell.Spell;
import spell.TargetSpell;
import fight.Frightening;
import figure.Figure;
import game.JDEnv;

public class Threat extends Spell implements TargetSpell{

	public static int [][] values = { {1,1,2,4,1},
		{15,13,12,25,2}
		};
	
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Threat() {
		super(1, values[0]);
		this.isPossibleInFight = true;
		this.isPossibleNormal = false;
//		this.strength = 4;
//		this.diff = 1;
//		this.diffMin = 1;
//		this.cost = 2;
//		this.level = 1;
	}
	public int getType() {
	
		return Spell.SPELL_THREAT;
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public String toString() {
		return getName();
	}



	public String getText() {
		
		return JDEnv.getResourceBundle().getString("spell_threat_text");
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			int balance = mage.getKnowledgeBalance((Figure)target);
			if(balance < 0) {
				return false;
			}
			return true;
		}
		return false;
	}

	private int getStrength(Figure mage,Figure target) {
		int bal = mage.getKnowledgeBalance(target);
		int res = 0;
		if(bal == 0) {
			res = (strength / 2);	
			
		}
		else if(bal == 1) {
			res = (strength / 1);	
			
		}
		else if(bal > 1) {
			res = (strength * 2);	
		}
		return res;
			
	}

	public void sorcer(Figure mage, Object target) {
		if(target instanceof Figure) {
			int value = getStrength(mage,(Figure)target);
			((Figure)target).putFrightening(new Frightening(mage, value,
					Frightening.TYPE_THREAT));
			
		}

	}

	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_threat_name");

	}

}
