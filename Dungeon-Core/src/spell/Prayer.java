package spell;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;

public class Prayer extends NoTargetSpell {

	public static int[][] values = { { 1, 1, 1, 10,1 }, {
		7, 5, 5, 20,2 }
};
	
	
	private final boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	private static String suffix = "prayer";
	
	public Prayer()
	{
		super();
		this.setLevel(1);
		this.isPossibleNormal = true;
	}
	@Override
	public int getType() {
		
		return SPELL_PRAYER;
	}

	@Override
	public String getText() {
		
		return JDEnv.getString("spell_"+suffix+"_text");
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
	public boolean isApplicable(Figure mage, Object target) {
		
		return mage instanceof Hero;
	}

	@Override
	public void sorcer(Figure mage) {
		mage.setBonusLive(true);
		mage.setRespawnRoom(mage.getRoom());

	}

	@Override
	public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

	

}
