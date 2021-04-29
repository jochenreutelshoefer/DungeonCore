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
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Monster;
import de.jdungeon.figure.monster.UndeadMonster;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.dungeon.RoomInfoEntity;

public class Light extends AbstractTargetSpell implements TargetSpell{

	public static int[][] values = { { 8, 5, 5, 4 ,1}, { 12, 12, 9, 8 ,2} };

	public Light(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
	}

	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_light_text");
	}
	
	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return false;
	}
	
	@Override
	public boolean isPossibleFight() {
		return true;
	}

	public Light(int level) {
		super(level, values[level - 1]);
	}

	public Light() {
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_LIGHT;
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {

		if (target instanceof Monster) {
			Figure m = (Figure) target;

			int bal = mage.getKnowledgeBalance(m);
			int res = 0;
			if (bal < -1) {

			} else if (bal == -1) {
				res = (strength / 5) ;

			} else if (bal == 0) {
				res = (strength / 2) ;

			} else if (bal == 1) {
				res = (strength / 1);

			} else if (bal > 1) {
				res = (strength * 2) ;
			}
			String str = JDEnv.getResourceBundle().getString("spell_light_cast")+" " + m.getName() + " (" + res + ")";
			mage.tellPercept(new TextPercept(str, round));
			if (m instanceof UndeadMonster) {
				res *= 1.6;
			}
			m.incBlinded(res);
		}
	}

	@Override
	public String getHeaderName() {
		return JDEnv.getResourceBundle().getString("spell_light_name");
	}

}
