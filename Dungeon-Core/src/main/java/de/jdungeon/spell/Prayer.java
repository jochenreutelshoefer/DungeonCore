package de.jdungeon.spell;

import de.jdungeon.figure.Figure;
import de.jdungeon.game.JDEnv;

public class Prayer extends NoTargetSpell {

	public static int[][] values = { { 1, 1, 1, 10,1 }, {
		7, 5, 5, 20,2 }
};
	
	
	private final boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	private static final String suffix = "prayer";
	
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
	public void sorcer(Figure mage, int round) {
		mage.setBonusLive(true);
		mage.setRespawnRoom(mage.getRoom());

	}

	@Override
	public String getHeaderName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

	

}
