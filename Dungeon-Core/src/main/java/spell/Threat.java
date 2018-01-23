package spell;

import dungeon.RoomEntity;
import dungeon.util.InfoUnitUnwrapper;
import fight.Frightening;
import figure.Figure;
import figure.FigureInfo;
import game.JDEnv;
import game.RoomInfoEntity;

public class Threat extends AbstractTargetSpell implements TargetSpell{

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
	
		return AbstractSpell.SPELL_THREAT;
	}
	
	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
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
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if (target instanceof Figure) {
			Figure m = (Figure) target;
			int balance = mage.getKnowledgeBalance(m);
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
	public void sorcer(Figure mage, RoomEntity target) {
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
