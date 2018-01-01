package figure.other;

import ai.AbstractAI;

import dungeon.Dungeon;
import dungeon.Room;

import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.monster.MonsterInfo;

public class Fir extends ConjuredMagicFigure {

	public Fir(int value, Dungeon d) {
		super(value, 25);
		construcHelp(value);
		this.setActualDungeon(d);
		createVisibilityMap(d);
		MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(this,
				this.getRoomVisibility());
		AbstractAI ai = new FirAI(info);
		ai.setFigure(info);
		this.control = new FigureControl(info, ai);
		// TODO: factor out - bilingual
		this.name = "Fichte";
	}
	
	@Override
	public boolean disappearAtEndOfFight() {
		return true;
	}

	@Override
	protected int getCHANCE_TO_HIT() {
		return 30;
	}

	@Override
	protected int getSCATTER() {
		return 0;
	}

	@Override
	protected int getHEALTH_DAMAGE_BALANCE() {
		return 2;
	}

	@Override
	public int hunting() {
		return 0;
	}

	public void heroHasFleen(Room r, int monsterCnt) {
		// this.dispose?
	}

	@Override
	protected double getAntiFleeFactor() {
		return 0;
	}

	@Override
	protected boolean makeSpecialAttack(Figure target) {
		return false;
	}

}
