/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package spell;

import java.util.List;

import dungeon.Door;
import dungeon.Position;
import fight.Frightening;
import figure.Figure;
import figure.percept.TextPercept;
import game.JDEnv;

public class Thunderstorm extends NoTargetSpell {


	public static int[][] values = { { 10, 5, 9, 10,2 }, { 15, 13, 12, 30,3 } };

	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	public Thunderstorm(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
		isPossibleNormal = true;
		isPossibleInFight = true;

	}
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	@Override
	public int getType() {
		return AbstractSpell.SPELL_THUNDERSTORM;
	}
	

	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString(
				"spell_thunderstorm_text");
		return s;
	}

	public Thunderstorm(int level) {

		super(level, values[level - 1]);
		this.level = level;
		isPossibleNormal = true;
		isPossibleInFight = true;
	}


	@Override
	public void sorcer(Figure mage, int round) {
		if(mage.getRoom().fightRunning()) {
		List<Figure> monster = mage.getRoom().getFight().getFightFigures();
		boolean first = false;
		for (int i = 0; i < monster.size(); i++) {
			Figure m = monster.get(i);
			if (m != mage) {
				if (!first) {
					first = true;
					m.putFrightening(new Frightening(mage, 3, Frightening.TYPE_THUNDER), round);
				} else {
					if (Math.random() < 0.3) {
						m.putFrightening(new Frightening(mage, 2, Frightening.TYPE_THUNDER), round);
					}
				}
			}

		}
		}else {
			Door [] doors = mage.getRoom().getDoors();
			for (int i = 0; i < doors.length; i++) {
				if(doors[i] != null) {
					Position p = doors[i].getPositionAtDoor(mage.getRoom(), true);
					Figure behindFigure = p.getFigure();
					if(behindFigure != null) {
						behindFigure.putFrightening(new Frightening(mage, 2,
								Frightening.TYPE_THUNDER), round);
					}
					
				}
			}
		}
		String str = JDEnv.getResourceBundle().getString(
				"spell_thunderstorm_cast");
		mage.tellPercept(new TextPercept(str, round));
		
	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_thunderstorm_name");
	}

}
