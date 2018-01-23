/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package spell;

import dungeon.RoomEntity;
import game.RoomInfoEntity;
import item.Item;

import java.util.List;

import figure.Figure;
import figure.FigureInfo;
import figure.percept.TextPercept;
import game.JDEnv;



public class Search extends AbstractTargetSpell {
	
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
		return AbstractSpell.SPELL_SEARCH;
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
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_search_text");
		}
	
	public Search() {
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	public Search(int level) {
			
		super(level,values[level-1]);
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	@Override
	public void sorcer(Figure mage, RoomEntity target) {
		
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
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	/**
	 * @see AbstractSpell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_search_name");
	}

}
