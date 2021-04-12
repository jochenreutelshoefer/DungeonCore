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
import de.jdungeon.fight.Slap;
import de.jdungeon.fight.SlapResult;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.monster.Skeleton;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.dungeon.RoomInfoEntity;

public class Bonebreaker extends AbstractTargetSpell {

	public static int[][] values = { { 7, 5, 8, 12, 2 }, { 15, 13, 12, 25, 2 } };

	
	private final boolean isPossibleNormal = false;
	private final boolean isPossibleInFight = true;
	
	public Bonebreaker(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}
	

//	public int getLernCost() {
//		return 2 * de.jdungeon.level;
//	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_BONEBREAKER;
	}

	@Override
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
	}

	public Bonebreaker() {
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {
		
		if (target instanceof Figure) {
			Figure m = (Figure)target;
			int hit = strength / 2 + (int) (Math.random() * (strength / 2));
			
			if (m instanceof Skeleton) {
				hit = 2 * strength + (int) (Math.random() * strength);
			}
			Slap slap = new Slap(mage, 0,0,150);
			slap.setValueMagic(hit);
			SlapResult s = m.getSlap(slap, round);
			((Hero) mage).receiveSlapResult(s);
			
			String str = JDEnv.getResourceBundle().getString("spell_bonebreaker_cast")+"!(" + hit + ")";
			mage.tellPercept(new TextPercept(str, round));
		}

	}

	/**
	 * @see AbstractSpell#getName()
	 */
	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_bonebreaker_name");
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return MonsterInfo.class;
	}

}
