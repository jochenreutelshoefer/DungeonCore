/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package de.jdungeon.spell;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.skill.FleeSkill;
import de.jdungeon.skill.SimpleSkillAction;

public class Escape extends NoTargetSpell {

	public static int[][] values = {
			{ 3, 4, 5, 10, 1 },
			{ 6, 12, 7, 30, 1 }
	};

	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	public Escape(int level, int diffMin, int diff, int cost, int strength, int lerncost) {
		super(level, diffMin, diff, cost, strength, lerncost);
		isPossibleNormal = false;
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
		return AbstractSpell.SPELL_ESCAPE;
	}

	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("spell_escape_text");
	}

	public Escape(int level) {

		super(level, values[level - 1]);
		this.level = level;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	@Override
	public String getName() {
		return JDEnv.getResourceBundle().getString("spell_escape_name");
	}

	@Override
	public void sorcer(Figure mage, int round) {
		mage.setEscape(level);
		//mage.incActionPoints(1, - 1); // Todo: re-implement speed boost
		FleeSkill skill = mage.getSkill(FleeSkill.class);
		SimpleSkillAction a = skill.newActionFor(FigureInfo.makeFigureInfo(mage, mage.getViwMap())).get();
		skill.doExecute(a, true, -1);
	}

	@Override
	public String toString() {
		return getName();
	}
}
