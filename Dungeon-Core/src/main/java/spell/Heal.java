package spell;
import figure.Figure;
import figure.percept.TextPercept;
import game.JDEnv;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */


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
	}

	@Override
	public boolean isApplicable(Figure mage, Object target) {
		
		return true;
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_HEAL;
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_heal_text");
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	public Heal(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
	}
	
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_heal_name");
	}

	@Override
	public void sorcer(Figure mage) {
				mage.heal(strength);
				mage.healPoisonings();
				String str = JDEnv.getResourceBundle().getString("spell_heal_cast");
				mage.tellPercept(new TextPercept(str));
	}

	
	
}

