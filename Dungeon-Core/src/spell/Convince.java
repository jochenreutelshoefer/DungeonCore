/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package spell;

import java.util.*;
import game.DungeonGame;
import game.JDEnv;
import figure.Figure;
import figure.hero.Hero;
import figure.monster.Monster;
import figure.percept.TextPercept;
import gui.*;
public class Convince extends Spell implements TargetSpell{

	public static int[][] values = { { 7, 4, 7, 30,1 }, {
			12, 12, 9, 20,1 }
	};

	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Convince(
		int level,
		int diffMin,
		int diff,
		int cost,
		int strength) {
		super(level, diffMin, diff, cost, strength,1);
		isPossibleNormal = false;
		isPossibleInFight = true;

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
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	public int getType() {
		return Spell.SPELL_CONVINCE;
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_convince_text");
			return s;
	}
	
//	public int getLernCost() {
//			return 1;
//	}

	public Convince(int level) {

		super(level, values[level - 1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target/*, int l*/) {

		if(target instanceof Monster) {
			Monster m = (Monster)target;
		
		
		int bal = mage.getKnowledgeBalance(m);
		int res = 0;
		if(bal < -1) {
			
		}else if(bal == -1) {
			res = (strength / 5);	
			
		}
		else if(bal == 0) {
			res = (strength / 2);	
			
		}
		else if(bal == 1) {
			res = (strength / 1);	
			
		}
		else if(bal > 1) {
			res = (strength * 2);	
			
		}
		String str = JDEnv.getResourceBundle().getString("spell_convince_cast")+" "+m.getName()+" ("+res+")";
		mage.tellPercept(new TextPercept(str));
		
		m.getReflexReactionUnit().setConvinced(mage,res);
		}
	}

	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_convince_name");
	}

}
