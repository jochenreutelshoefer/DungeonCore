package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Poisoning;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.dungeon.RoomInfoEntity;

public class Poison extends AbstractTargetSpell {

	public static int[][] values = { { 1, 1, 8, 8, 1 }, { 15, 13, 12, 25, 2 } };

	public static final String suffix = "poisoning";

	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	public Poison(int level) {
		super(level, values[level - 1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}

	public Poison(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength, learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}

	@Override
	public int getType() {
		return AbstractSpell.SPELL_POISONING;
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
	public String getText() {
		return JDEnv.getString("spell_" + suffix + "_text");
	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if (target instanceof Figure) {
			return true;
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {
		if (target instanceof Figure) {
			((Figure) target).poison(new Poisoning(mage, this.getStrength(), 8));
		}

	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
	public String getName() {
		return JDEnv.getString("spell_" + suffix + "_name");
	}

}
