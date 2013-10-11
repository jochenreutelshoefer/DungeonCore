package spell;
import figure.Figure;
import figure.percept.TextPercept;
import game.Game;
import game.JDEnv;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

//import java.awt.event.*;

public class Heal extends Spell {
	
//	public static int [] diff = { 4 , 7 };
//	public static int [] diffMin = { 8 , 14};
	
	public static int [][] values = { {9,5,7,30,1},
								{7,11,9,45,2}
								};
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
//	public heal_spell() {
//		target = spell.TARGET_SELF;
//		normal = true;
//		fight = true;
//		level = 1;
//		worth = 15;
//	}
	
	public Heal(int l) {
		super(l,values[l-1]);
		isPossibleNormal = true;
		isPossibleInFight = true;
		
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		
		return true;
	}
	
	public int getType() {
		return Spell.SPELL_HEAL;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_heal_text");
			return s;
	}
	
//	public int getLernCost() {
//			return level;
//		}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	public Heal(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = true;
	}
	
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_heal_name");
	}


	

	/**
	 * @see Spell#fire(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		
				mage.heal(strength);
				mage.healPoisonings();
				String str = JDEnv.getResourceBundle().getString("spell_heal_cast");
				mage.tellPercept(new TextPercept(str));
				
	}

	
	
}

