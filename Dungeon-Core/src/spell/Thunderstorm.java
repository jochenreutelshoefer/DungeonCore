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

import dungeon.Door;
import dungeon.Position;
import game.Game;
import game.JDEnv;
import fight.Frightening;
import figure.Figure;
import figure.monster.Monster;
import figure.percept.TextPercept;

public class Thunderstorm extends Spell {

//	public int[] diff = { 5, 13 };
//
//	public int[] diffMin = { 10, 15 };

	public static int[][] values = { { 10, 5, 9, 10,2 }, { 15, 13, 12, 30,3 } };

	private boolean isPossibleNormal;
	private boolean isPossibleInFight;

	public Thunderstorm(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = true;

	}
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	public int getType() {
		return Spell.SPELL_THUNDERSTORM;
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		
		return true;
	}

//	public boolean fightModus() {
//		return isPossibleInFight;
//	}
//
//	public boolean normalModus() {
//		return isPossibleNormal;
//	}

	public String getText() {
		String s = JDEnv.getResourceBundle().getString(
				"spell_thunderstorm_text");
		return s;
	}

//	public int getLernCost() {
//		return 2 * level;
//	}

	public Thunderstorm(int level) {

		super(level, values[level - 1]);
		this.level = level;
		isPossibleNormal = true;
		isPossibleInFight = true;
	}

	/**
	 * @see Spell#getDifficulty(int)
	 */
//	public int getDifficulty(int level) {
//		return diff[level - 1];
//	}
//
//	public int getDifficultyMin(int level) {
//		return diffMin[level - 1];
//	}
//
//	/**
//	 * @see Spell#getCost(int)
//	 */
//	public int getCost(int level) {
//		return level * 15;
//	}

	/**
	 * @see Spell#fire(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {
		if(mage.getRoom().fightRunning()) {
		List<Figure> monster = mage.getRoom().getFight().getFightFigures();
		boolean first = false;
		for (int i = 0; i < monster.size(); i++) {
			Figure m = (Figure) monster.get(i);
			if (m != mage) {
				if (!first) {
					first = true;
					m.putFrightening(new Frightening(mage, 3,
							Frightening.TYPE_THUNDER));
				} else {
					if (Math.random() < 0.3) {
						m.putFrightening(new Frightening(mage, 2,
								Frightening.TYPE_THUNDER));
					}
				}
			}

		}
		}else {
			Door [] doors = mage.getRoom().getDoors();
			for (int i = 0; i < doors.length; i++) {
				if(doors[i] != null) {
					Position p = doors[i].getPositionBehind(mage.getRoom());
					Figure behindFigure = p.getFigure();
					if(behindFigure != null) {
						behindFigure.putFrightening(new Frightening(mage, 2,
								Frightening.TYPE_THUNDER));
					}
					
				}
			}
		}
		String str = JDEnv.getResourceBundle().getString(
				"spell_thunderstorm_cast");
		mage.tellPercept(new TextPercept(str));	
		
	}

	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_thunderstorm_name");
	}

}
