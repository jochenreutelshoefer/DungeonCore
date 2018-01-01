package figure.other;

import ai.AI;
import figure.Figure;
import figure.FigureControl;
import figure.FigureInfo;
import figure.monster.MonsterInfo;
import figure.npc.RescuedNPCAI;
import item.Item;
import item.interfaces.ItemOwner;

import java.util.List;

import dungeon.Dungeon;

public class Lioness extends ConjuredMagicFigure {

	private Lioness(int value, Dungeon d, FigureInfo master, AI ai) {
		super(value, 100, ai);
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
		return new Lioness(value, d, master, ai);
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
	protected int getCHANCE_TO_HIT() {
		return 30;
	}

	@Override
	protected int getSCATTER() {
		return 3;
	}

	@Override
	protected int getHEALTH_DAMAGE_BALANCE() {
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

	@Override
	protected boolean makeSpecialAttack(Figure target) {
		return false;
	}

}
