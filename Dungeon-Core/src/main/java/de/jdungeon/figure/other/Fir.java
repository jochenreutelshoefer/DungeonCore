package de.jdungeon.figure.other;

import de.jdungeon.ai.AbstractAI;

import de.jdungeon.dungeon.Dungeon;

import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.figure.monster.MonsterInfo;

public class Fir extends ConjuredMagicFigure {

	public Fir(int value, Dungeon d) {
		super(value, 25);
		construcHelp(value);
		this.setActualDungeon(d);
		createVisibilityMap(d);
		MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(this, this.getRoomVisibility());
		AbstractAI ai = new FirAI(info);
		ai.setFigure(info);
		this.control = new FigureControl(info, ai);
		// TODO: factor out - bilingual
		this.name = "Fichte";
	}


	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Fir;
	}

	@Override
	public boolean disappearAtEndOfFight() {
		return false;
	}

	@Override
	protected int getChangeToHit() {
		return 30;
	}

	@Override
	protected int getDamageVariance() {
		return 0;
	}

	@Override
	protected int getHealthDamageBalance() {
		return 2;
	}

	@Override
	public int hunting() {
		return 0;
	}

	@Override
	protected double getAntiFleeFactor() {
		return 0;
	}

}
