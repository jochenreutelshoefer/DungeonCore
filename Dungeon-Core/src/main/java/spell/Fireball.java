/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;

import fight.Slap;
import fight.SlapResult;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.InfoEntity;
import game.JDEnv;
import util.Arith;
import dungeon.Position;

public class Fireball extends Spell implements TargetSpell{


	public static int[][] VALUES = { { 9, 6, 5, 9, 1 }, { 17, 13, 7, 24, 2 } };

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	public Fireball(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
	}
	
	@Override
	public boolean isPossibleFight() {
		return true;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return false;
	}
	
	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return true;
	}
	
	@Override
	public int getType() {
		return Spell.SPELL_FIREBALL;
	}
	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	

	@Override
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



	/**
	 * @see Spell#sorcer(fighter, Object, int)
	 */
	@Override
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
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_fireball_name");
	}

}
