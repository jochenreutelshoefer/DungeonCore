/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.dungeon.RoomInfoEntity;

public class Convince extends AbstractTargetSpell implements TargetSpell{

	public static int[][] values = { { 7, 4, 7, 30,1 }, {
			12, 12, 9, 20,1 }
	};

	private final boolean isPossibleNormal = false;
	private final boolean isPossibleInFight = true;
	
	public Convince(
		int level,
		int diffMin,
		int diff,
		int cost,
		int strength) {
		super(level, diffMin, diff, cost, strength,1);
	}
	
	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	@Override
	public int getType() {
		return AbstractSpell.SPELL_CONVINCE;
	}
	
	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	@Override
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
	}

	public Convince() {
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {

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
		mage.tellPercept(new TextPercept(str, round));
		
		m.getReflexReactionUnit().setConvinced(mage,res);
		}
	}

	/**
	 * @see AbstractSpell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_convince_name");
	}

}
