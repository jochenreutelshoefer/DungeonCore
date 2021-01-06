package figure.other;

import java.util.List;

import ai.AI;
import dungeon.Dungeon;
import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.FigurePresentation;
import figure.monster.MonsterInfo;
import figure.npc.RescuedNPCAI;
import item.Item;
import item.interfaces.ItemOwner;

public class Lioness extends ConjuredMagicFigure {

	private Lioness(int value, Dungeon d, AI ai) {
		super(value, 200, ai);
		this.setActualDungeon(d);
		createVisibilityMap(d);
		MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(this,
				getRoomVisibility());
		// todo: create AI which has its leader set on creation
		ai.setFigure(info);
		this.control = new FigureControl(info, ai);
		// TODO: factor out - bilingual
		this.name = "Löwin";
	}

	public static Lioness createLioness(int value, Dungeon d, FigureInfo master) {
		AI ai = new RescuedNPCAI();
		Lioness lioness = new Lioness(value, d, ai);
		FigureInfo figureInfo = FigureInfo.makeFigureInfo(lioness, lioness.getRoomVisibility());
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
