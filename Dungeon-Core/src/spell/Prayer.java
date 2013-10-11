package spell;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;

public class Prayer extends Spell {

	public static int[][] values = { { 1, 1, 1, 10,1 }, {
		7, 5, 5, 20,2 }
};
	
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	private static String suffix = "prayer";
	
	public Prayer()
	{
		super();
		this.setLevel(1);
		this.isPossibleNormal = true;
	}
	public int getType() {
		
		return SPELL_PRAYER;
	}

	public String getText() {
		
		return JDEnv.getString("spell_"+suffix+"_text");
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	public boolean isApplicable(Figure mage, Object target) {
		
		return mage instanceof Hero;
	}

	public void sorcer(Figure mage, Object target) {
		mage.setBonusLive(true);
		mage.setRespawnRoom(mage.getRoom());

	}

	public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

	

}
