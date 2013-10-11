/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package spell;

import java.util.LinkedList;

import dungeon.Room;
import game.Game;
import game.JDEnv;
import fight.Slap;
import fight.SlapResult;
import figure.Figure;
import figure.hero.Hero;
import figure.monster.Monster;
import figure.monster.Skeleton;
import figure.percept.TextPercept;
import gui.*;

public class Bonebreaker extends Spell implements TargetSpell {
//	public static int[] diffArray = { 5, 13 };
//
//	public static int[] diffMinArray = { 9, 15 };

	public static int[][] values = { { 8, 5, 8, 12,2 }, { 15, 13, 12, 25,2 } };

	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Bonebreaker(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	

//	public int getLernCost() {
//		return 2 * level;
//	}
	
	public int getType() {
		return Spell.SPELL_BONEBREAKER;
	}

	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_bonebreaker_text");
		return s;
	}

	public boolean fightModus() {
		return isPossibleInFight;
	}

	public boolean normalModus() {
		return isPossibleNormal;
	}

	public Bonebreaker(int level) {

		super(level, values[level - 1]);
		
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	
	public void sorcer(Figure mage, Object target/*, int l*/) {
		
		if (target instanceof Figure) {
			Figure m = (Figure)target;
			int hit = strength / 2 + (int) (Math.random() * (strength / 2));
			
			if (m instanceof Skeleton) {
				hit = 2 * strength + (int) (Math.random() * strength);
			}
			Slap slap = new Slap(mage, 0,0,150);
			slap.setValueMagic(hit);
			SlapResult s = m.getSlap(slap);
			((Hero) mage).receiveSlapResult(s);
			
			String str = JDEnv.getResourceBundle().getString("spell_bonebreaker_cast")+"!(" + hit + ")";
			mage.tellPercept(new TextPercept(str));
		}

	}

	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_bonebreaker_name");
	}

}
