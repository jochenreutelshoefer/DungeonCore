/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package spell;

import item.*;
import game.Game;
import game.JDEnv;

import java.util.*;

import figure.Figure;
import figure.monster.Monster;
import figure.percept.TextPercept;
import gui.*;



public class Search extends Spell {
	
	public static int [][] values = { {5,1,2,10,1},
								{15,13,12,25,2}
								};

	private boolean isPossibleNormal;
	private boolean isPossibleInFight;


	public Search(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	public int getType() {
		return Spell.SPELL_SEARCH;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_search_text");
			return s;
		}
	
//	public int getLernCost() {
//			return 1;
//		}
	
//	public boolean fightModus(){
//		return isPossibleInFight;
//	}
//	
//	public boolean normalModus(){
//		return isPossibleNormal;
//	}
	
	
	public Search(int level) {
			
		super(level,values[level-1]);
		
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		
		if(target instanceof Figure) {
			Figure m = (Figure)target;
		
		String s = new String();
		List<Item> list = m.getItems();
		for(int i = 0; i < list.size(); i++) {
			Item it = (Item)list.get(i);
			s += it.toString()+"\n";	
		}
		if(list.size() == 0) {
			s = JDEnv.getResourceBundle().getString("spell_search_cast_nothing");	
		}
		
		String str = m.getName()+JDEnv.getResourceBundle().getString("spell_search_cast_done")+"\n"+s;
		mage.tellPercept(new TextPercept(str));
		}
		
	}

	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_search_name");
	}

}
