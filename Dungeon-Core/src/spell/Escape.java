/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 * 
 */

package spell;

import figure.Figure;
import figure.action.FleeAction;
import game.JDEnv;


public class Escape extends NoTargetSpell {
	
//	public int [] diff = { 3 , 8 };
//	public int [] diffMin = { 6 , 12};
	
	public static int [][] values = { {3,4,5,10,1},
								{6,12,7,30,1}
								};
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public Escape(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength,lerncost);
		isPossibleNormal = false;
		isPossibleInFight = true;
		
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
	public int getType() {
		return Spell.SPELL_ESCAPE;
	}
	
	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_escape_text");
		return s;
	}
	
	@Override
	public boolean isApplicable(Figure mage, Object target) {
		
		return true;
	}
	
//	public int getLernCost() {
//			return 1*level;
//	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	public Escape(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_escape_name");
	}


	/**
	 * @see Spell#fire(fighter, Object, int)
	 */
	@Override
	public void sorcer(Figure mage) {
		
				mage.setEscape(level);
				mage.incFightAP(1);
				mage.handleFleeAction(new FleeAction(false),true);
				
	}

	@Override
	public String toString() {
		return getName();
	}
}
