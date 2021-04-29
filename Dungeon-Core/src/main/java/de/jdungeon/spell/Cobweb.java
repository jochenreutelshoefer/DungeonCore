package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.game.JDEnv;
import de.jdungeon.dungeon.RoomInfoEntity;

@Deprecated
public class Cobweb extends AbstractTargetSpell implements TargetSpell{
	
	public static int[][] values = { { 1, 1, 10, 30,1 }, { 15, 13, 12, 25,1 } };
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
	public Cobweb(int level) {
		super(level, values[level-1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public Cobweb(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
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
	public void sorcer(Figure f, RoomEntity target, int round) {
		//((Figure)target).setCobwebbed(this.de.jdungeon.level * this.getStrength());
	}
	
	@Override
	public String getHeaderName() {
		return JDEnv.getString("spell_net_name");
	}
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_COBWEB;
	}
	
	@Override
	public boolean isApplicable(Figure f, RoomEntity target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	@Override
	public String getText() {
		String s = JDEnv.getResourceBundle().getString("spell_net_text");
		return s;
	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

}
