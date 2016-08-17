/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package spell;

import item.Item;

import java.util.List;

import figure.Figure;
import figure.FigureInfo;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;



public class Search extends Spell {
	
	public static int [][] values = { {5,1,2,10,1},
								{15,13,12,25,2}
								};

	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;


	public Search(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	@Override
	public int getType() {
		return Spell.SPELL_SEARCH;
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
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
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
	@Override
	public void sorcer(Figure mage, Object target) {
		
		if(target instanceof Figure) {
			Figure m = (Figure)target;
		
		String s = new String();
		List<Item> list = m.getItems();
		for(int i = 0; i < list.size(); i++) {
			Item it = list.get(i);
			s += it.toString()+"\n";	
		}
		if(list.size() == 0) {
			s = JDEnv.getResourceBundle().getString("spell_search_cast_nothing");	
		}
		
		String str = m.getName()+JDEnv.getResourceBundle().getString("spell_search_cast_done")+"\n"+s;
		mage.tellPercept(new TextPercept(str));
		}
		
	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	/**
	 * @see Spell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_search_name");
	}

}
