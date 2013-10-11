package figure.other;

import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.monster.MonsterInfo;
import item.Item;
import item.interfaces.ItemOwner;

import java.util.List;

import dungeon.Dungeon;

import ai.AI;

public class Lioness extends ConjuredMagicFigure {

	public Lioness(int value, Dungeon d, FigureInfo master) {
		super(value, 100);
		createVisibilityMap(d);
		MonsterInfo info = (MonsterInfo) FigureInfo.makeFigureInfo(this,
				this.roomVisibility);
		AI ai = new LionessAI(info, master);
		ai.setFigure(info);
		this.control = new FigureControl(info, ai);
		// TODO: factor out - bilingual
		this.name = "Löwin";
	}

	@Override
	public boolean addItems(List<Item> l, ItemOwner donator) {
		return false;
	}

	@Override
	public boolean disappearAtEndOfFight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getCHANCE_TO_HIT() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getSCATTER() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getHEALTH_DAMAGE_BALANCE() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hunting() {
		return 0;
	}

	@Override
	protected double getAntiFleeFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected boolean makeSpecialAttack(Figure target) {
		// TODO Auto-generated method stub
		return false;
	}

}
