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
import de.jdungeon.skill.attack.Slap;
import de.jdungeon.skill.attack.SlapResult;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.util.Arith;
import de.jdungeon.dungeon.Position;

public class Fireball extends AbstractTargetSpell implements TargetSpell{


	public static int[][] VALUES = { { 9, 6, 5, 9, 1 }, { 17, 13, 7, 24, 2 } };

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
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
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_FIREBALL;
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof FigureInfo) {
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
//		return de.jdungeon.level;
//	}

	public Fireball(int level) {

		super(level, VALUES[level - 1]);
		valueSet = VALUES[level-1];
		this.level = level;
	}

	public Fireball() {
		this.strength = 6;
		this.cost = 5;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {

		if (target instanceof Figure) {
			Figure m = (Figure) target;
			int tarPos = m.getPositionInRoom();
			int dir = Position.getDirFromTo(mage.getPositionInRoom(),tarPos);
			mage.setLookDir(dir);
			int value = (int) Arith.gauss(strength, 0.5);
			Slap slap = new Slap(mage, 0, 0, 150);
			slap.setValueFire(value);
			SlapResult s = m.getSlap(slap, round);
			((Hero) mage).receiveSlapResult(s);


			String str = JDEnv.getResourceBundle().getString("spell_fireball_cast")+"!(" + value
					+ ")";
			mage.tellPercept(new TextPercept(str, round));

		}

	}

	/**
	 * @see AbstractSpell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_fireball_name");
	}

}
