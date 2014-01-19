/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;

import java.util.List;

import dungeon.Room;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.percept.TextPercept;
import game.JDEnv;

public class Spy extends NoTargetSpell {


//	public int [] diff = { 6 , 13 };
//	public int [] diffMin = { 10 , 15};
	
	public static int [][] values = { {10,6,5,10,1},
								{15,10,7,30,2}
								};
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public Spy(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = false;
		
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
		
		return true;
	}
	
	@Override
	public int getType() {
		return Spell.SPELL_SPY;
	}
	
	public Spy(int level) {
			
		super(level,values[level-1]);
		
		isPossibleNormal = true;
		isPossibleInFight = false;
	}
	
//	public boolean fightModus(){
//		return isPossibleInFight;
//	}
//	
//	public boolean normalModus(){
//		return isPossibleNormal;
//	}
//	
//	public int getLernCost() {
//			return level;
//		}
//	
//	/**
//	 * @see Spell#getDifficulty(int)
//	 */
//	public int getDifficulty(int level) {
//		return diff[level-1];
//	}
//
//	/**
//	 * @see Spell#getDifficultyMin(int)
//	 */
//	public int getDifficultyMin(int level) {
//		return diff[level-1];
//	}

	/**
	 * @see Spell#getCost(int)
	 */
	public int getCost(int level) {
		return level * 8;
	}

	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	@Override
	public void sorcer(Figure mage) {
				
				List rooms = mage.getRoom().getNeighboursWithDoor();
				
				for(int i = 0; i < rooms.size(); i++) {
					Room toView = (Room)rooms.get(i);
					mage.getRoomObservationStatus(toView.getLocation()).setVisibilityStatus(RoomObservationStatus.VISIBILITY_FIGURES);
					mage.addScoutedRoom(toView);
				}
			
				
				String str = JDEnv.getResourceBundle().getString("spell_spy_cast");
				mage.tellPercept(new TextPercept(str));
				
	}

	/**
	 * @see Spell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_spy_name");
	}
	
	@Override
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_spy_text");
			return s;
		}
	
	
	

	
}
