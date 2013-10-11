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
import game.Game;
import game.JDEnv;
import figure.Figure;
import figure.hero.Hero;
import figure.monster.Monster;
import figure.monster.UndeadMonster;
import figure.percept.TextPercept;
import gui.*;

public class Light extends Spell implements TargetSpell{

	public static int[][] values = { { 8, 5, 5, 4 ,1}, { 12, 12, 9, 8 ,2} };

	public Light(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
	}

	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_light_text");
		return s;
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	public boolean isPossibleNormal() {
		return false;
	}
	
	public boolean isPossibleFight() {
		return true;
	}

	public Light(int level) {
		super(level, values[level - 1]);
	}
	
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	public int getType() {
		return Spell.SPELL_LIGHT;
	}

	public boolean fightModus() {
		return isPossibleFight();
	}

	public boolean normalModus() {
		return isPossibleNormal();
	}

	

	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	public void sorcer(Figure mage, Object target) {

		if (target instanceof Monster) {
			Figure m = (Figure) target;

			int bal = mage.getKnowledgeBalance(m);
			int res = 0;
			if (bal < -1) {

			} else if (bal == -1) {
				res = (strength / 5) / m.getLevel();

			} else if (bal == 0) {
				res = (strength / 2) / m.getLevel();

			} else if (bal == 1) {
				res = (strength / 1) / m.getLevel();

			} else if (bal > 1) {
				res = (strength * 2) / m.getLevel();

			}
			String str = JDEnv.getResourceBundle().getString("spell_light_cast")+" " + m.getName() + " (" + res + ")";
			mage.tellPercept(new TextPercept(str));
			if (m instanceof UndeadMonster) {
				res *= 1.6;
			}
			m.incBlinded(res);
		}
	}

	/**
	 * @see Spell#getName()
	 */
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_light_name");
	}

}
