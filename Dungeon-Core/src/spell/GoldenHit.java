/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;



import dungeon.Position;
import figure.Figure;
import figure.percept.TextPercept;
import gui.*;
import game.Game;
import game.JDEnv;
public class GoldenHit extends Spell implements TargetSpell{
	
	public int [] diff = { 1 , 4 };
	public int [] diffMin = { 5 , 8};
	
	public static int [][] values = { {1,1,5,10,1},
								{7,5,5,20,2}
								};
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public GoldenHit(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength, lerncost);
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	

	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	public int getType() {
		return Spell.SPELL_GOLDENHIT;
	}
	
	public String getText() {
			String s = JDEnv.getResourceBundle().getString("spell_goldenHit_text");
			return s;
		}
	
//	public int getLernCost() {
//			return level;
//		}
	
	public GoldenHit(int level) {
			
		super(level,values[level-1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_goldenHit_name");
	}
	
	public boolean fightModus(){
		return isPossibleInFight;
	}
	
	public boolean normalModus(){
		return isPossibleNormal;
	}
	
	
//	public int getDifficulty(int level) {
//		return diff[level - 1];	
//	}
	
//	public int getDifficultyMin(int level) {
//		return diffMin[level - 1];	
//	}
	
//	public int getCost(int level) {
//		return 5*level;
//	}
	
	public void sorcer(Figure mage, Object target) {
		
				((Figure)mage).setGolden_hit(level);
				//setzt nur die Trefferwahrscheinlichkeit f�r den n�chsten Schlag hoch
				
				//und l�st dann einen Schlag aus
				String str = JDEnv.getResourceBundle().getString("spell_goldenHit_cast");
				mage.tellPercept(new TextPercept(str));
				
				if(target instanceof Figure){
				mage.attack((Figure)target);
				}
				else {
					System.out.println("kein fighter als target bei golden_hit!");
				}
			
				
	}
	
	
	public String toString(){
		return getName();
	}
		

}
