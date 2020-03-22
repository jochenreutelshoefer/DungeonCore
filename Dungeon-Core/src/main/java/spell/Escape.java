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

import dungeon.RoomEntity;
import figure.Figure;
import figure.action.FleeAction;
import game.JDEnv;


public class Escape extends NoTargetSpell {
	
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
		return AbstractSpell.SPELL_ESCAPE;
	}
	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_escape_text");
	}
	

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


	@Override
	public void sorcer(Figure mage, int round) {
				mage.setEscape(level);
				mage.incActionPoints(1, - 1);
				mage.handleFleeAction(new FleeAction(false),true, - 1);
	}

	@Override
	public String toString() {
		return getName();
	}
}
