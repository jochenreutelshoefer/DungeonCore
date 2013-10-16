package spell;

import item.*;
import game.DungeonGame;
import game.JDEnv;

import java.util.*;

import figure.Figure;
import figure.monster.Monster;
import figure.percept.TextPercept;
import gui.*;



public class Steal extends Spell {
	
	public static int [][] values = { {6,4,8,10,1},
								{15,13,12,25,2}
								};


	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Steal(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public int getType() {
		return Spell.SPELL_STEAL;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_steal_text");
			return s;
	}
	
	
//	public boolean fightModus(){
//		return isPossibleInFight;
//	}
	
//	public boolean normalModus(){
//		return isPossibleNormal;
//	}
	
	
	public Steal(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
//	public int getLernCost() {
//			return 1;
//		}
	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		
		if(target instanceof Figure) {
			Figure m = (Figure)target;
		
		List<Item> list = m.getItems();
		int max = -1;
		Item best = null;
		for(int i = 0; i < list.size(); i++) {
			Item it = (Item)list.get(i);
			int k = it.getWorth();	
			if(k > max) {
				max = k;
				best = it;	
			}
		}
		if(list.size() > 0) {
			m.removeItem(best);	
			mage.takeItem(best,mage.getRoom());
			String str = (m.getName()+ JDEnv.getResourceBundle().getString("spell_steal_cast")+":"+best.toString());
			mage.tellPercept(new TextPercept(str));
		}
		
		}
		
	}

	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_steal_name");
	}

}

