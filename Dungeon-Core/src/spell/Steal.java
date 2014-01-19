package spell;

import item.Item;

import java.util.List;

import figure.Figure;
import figure.FigureInfo;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;



public class Steal extends Spell {
	
	public static int [][] values = { {6,4,8,10,1},
								{15,13,12,25,2}
								};


	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public Steal(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	
	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
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
		return Spell.SPELL_STEAL;
	}
	
	@Override
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
	@Override
	public void sorcer(Figure mage, Object target) {
		
		if(target instanceof Figure) {
			Figure m = (Figure)target;
		
		List<Item> list = m.getItems();
		int max = -1;
		Item best = null;
		for(int i = 0; i < list.size(); i++) {
			Item it = list.get(i);
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

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	/**
	 * @see Spell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_steal_name");
	}

}

