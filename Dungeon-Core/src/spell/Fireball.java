/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;

import java.util.*;

import dungeon.Position;

import util.Arith;
import game.DungeonGame;
import game.JDEnv;
import fight.Slap;
import fight.SlapResult;
import figure.Figure;
import figure.hero.Hero;
import figure.monster.Monster;
import figure.percept.TextPercept;
import gui.*;

public class Fireball extends Spell implements TargetSpell{

	//public static int[] diffArray = { 6, 13 };

	//public static int[] diffMinArray = { 10, 15 };
	
	//diffMin , diff, cost, strength, lerncost

	public static int[][] VALUES = { { 10, 6, 5, 9 ,1}, { 17, 13, 7, 24,2 } };

	// public fireball_spell() {
	// target = spell.TARGET_SELF;
	// normal = false;
	// fight = true;
	// level = 1;
	// worth = 10;
	// }

	public Fireball(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
	}
	
	public boolean isPossibleFight() {
		return true;
	}
	
	public boolean isPossibleNormal() {
		return false;
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	public int getType() {
		return Spell.SPELL_FIREBALL;
	}
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	

	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_fireball_text");
		return s;
	}

//	public int getLernCost() {
//		return level;
//	}

	public boolean fightModus() {
		return isPossibleFight();
	}

	public boolean normalModus() {
		return isPossibleNormal();
	}

	public Fireball(int level) {

		super(level, VALUES[level - 1]);
		valueSet = VALUES[level-1];
		this.level = level;
	}

//	/**
//	 * @see Spell#getDifficulty(int)
//	 */
//	public int getDifficulty() {
//		return diff;
//	}
//
//	/**
//	 * @see Spell#getDifficultyMin(int)
//	 */
//	public int getDifficultyMin() {
//		return diffMin;
//	}
//
//	/**
//	 * @see Spell#getCost(int)
//	 */
//	public int getCost(int level) {
//		return level * 8;
//	}

	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {

		
		if (target instanceof Figure) {
			Figure m = (Figure) target;
			int tarPos = m.getPositionInRoom();
			int dir = Position.getDirFromTo(mage.getPositionInRoom(),tarPos);
			mage.setLookDir(dir);
			int value = (int) Arith.gauss(strength, 0.5);
			Slap slap = new Slap(mage, 0, 0, 150);
			slap.setValueFire(value);
			SlapResult s = m.getSlap(slap);
			((Hero) mage).receiveSlapResult(s);
			String str = JDEnv.getResourceBundle().getString("spell_fireball_cast")+"!(" + value
					+ ")";
			mage.tellPercept(new TextPercept(str));

		}

	}

	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_fireball_name");
	}

}
