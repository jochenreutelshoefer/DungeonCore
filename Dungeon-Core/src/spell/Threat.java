package spell;

import fight.Frightening;
import figure.Figure;
import figure.FigureInfo;
import game.InfoEntity;
import game.JDEnv;

public class Threat extends Spell implements TargetSpell{

	public static int [][] values = { {1,1,2,4,1},
		{15,13,12,25,2}
		};
	
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public Threat() {
		super(1, values[0]);
		this.isPossibleInFight = true;
		this.isPossibleNormal = false;
	}
	@Override
	public int getType() {
	
		return Spell.SPELL_THREAT;
	}
	
	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public String toString() {
		return getName();
	}



	@Override
	public String getText() {
		
		return JDEnv.getResourceBundle().getString("spell_threat_text");
	}
	
	@Override
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

	@Override
	public void sorcer(Figure mage, Object target) {
		if(target instanceof Figure) {
			int value = getStrength(mage,(Figure)target);
			((Figure)target).putFrightening(new Frightening(mage, value,
					Frightening.TYPE_THREAT));
			
		}

	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_threat_name");

	}

}
