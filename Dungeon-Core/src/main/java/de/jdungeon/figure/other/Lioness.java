package de.jdungeon.figure.other;

import java.util.List;

import de.jdungeon.ai.AI;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.FigureControl;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.npc.RescuedNPCAI;
import de.jdungeon.item.Item;
import de.jdungeon.item.interfaces.ItemOwner;

public class Lioness extends ConjuredMagicFigure {

	private Lioness(int value, Dungeon d, AI ai) {
		super(value, 300, ai);
		this.setActualDungeon(d);
		createVisibilityMap(d);
		MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(this,
				getViwMap());
		// todo: create AI which has its leader set on creation
		ai.setFigure(info);
		this.control = new FigureControl(info, ai);
		// TODO: factor out - bilingual
		this.name = "Löwin";
	}

	public static Lioness createLioness(int value, Dungeon d, FigureInfo master) {
		AI ai = new RescuedNPCAI();
		Lioness lioness = new Lioness(value, d, ai);
		FigureInfo figureInfo = FigureInfo.makeFigureInfo(lioness, lioness.getViwMap());
		ai.setFigure(figureInfo);
		d.insertFigure(lioness);
		return lioness;
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Lioness;
	}

	@Override
	public boolean addItems(List<Item> l, ItemOwner donator) {
		return false;
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
		return 3;
	}

	@Override
	protected int getHealthDamageBalance() {
		return 1;
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
