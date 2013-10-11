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

import java.awt.event.*;


import dungeon.RouteInstruction;
import game.Game;
import game.JDEnv;
import figure.Figure;
import figure.action.FleeAction;


public class Escape extends Spell {
	
//	public int [] diff = { 3 , 8 };
//	public int [] diffMin = { 6 , 12};
	
	public static int [][] values = { {3,4,5,10,1},
								{6,12,7,30,1}
								};
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Escape(int level, int diffMin, int diff, int cost,int strength, int lerncost) {
		super(level,diffMin, diff, cost,strength,lerncost);
		isPossibleNormal = false;
		isPossibleInFight = true;
		
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public int getType() {
		return Spell.SPELL_ESCAPE;
	}
	
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_escape_text");
		return s;
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		
		return true;
	}
	
//	public int getLernCost() {
//			return 1*level;
//	}
	
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

//	/**
//	 * @see Spell#getDifficulty(int)
//	 */
//	public int getDifficulty(int level) {
//		return diff[level -1] ;
//	}
//	
//	public int getDifficultyMin(int level) {
//		return diffMin[level -1] ;
//	}
	
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_escape_name");
	}

	/**
	 * @see Spell#getCost(int)
	 */
//	public int getCost(int level) {
//		return level*5;
//	}

	/**
	 * @see Spell#fire(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		
				
		
				((Figure)mage).setEscape(level);
				((Figure)mage).incFightAP(1);
				((Figure)mage).handleFleeAction(new FleeAction(false),true);
				
				//DirectionChoiceView dcv = new DirectionChoiceView(f, "Richtung w�hlen", this);
				//geht dann nach tellDirection per Knopfauswahl
	}

//	public void tellDirection(int i, Figure f) {
//		ActionEvent ae = null;
//		if(i == RouteInstruction.NORTH) {
//			 ae = new ActionEvent(f.getSteuerung().north,0,"");
//			
//		}
//		else if(i == RouteInstruction.SOUTH) {
//			ae = new ActionEvent(f.getSteuerung().south,0,"");
//			
//		}
//		else if(i == RouteInstruction.EAST) {
//			ae = new ActionEvent(f.getSteuerung().east,0,"");
//		}
//		else if(i == RouteInstruction.WEST) {
//			ae = new ActionEvent(f.getSteuerung().west,0,"");
//		}
//		//System.out.println("Zauberflucht!");
//		f.newStatement("Mit einem magischen Windsto� entfliehst Du Deinen Feinden!",2);
//		f.getSteuerung().actionPerformed(ae);
			
			
//	}
	public String toString() {
		return getName();
	}
}
