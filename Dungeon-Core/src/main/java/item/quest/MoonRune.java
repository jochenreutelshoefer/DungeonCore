package item.quest;

import dungeon.RoomEntity;
import figure.Figure;
import item.Item;
import item.interfaces.Locatable;
import item.interfaces.Usable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class MoonRune extends Item implements Usable, Locatable {

	public static final int COST = 5;

	public MoonRune() {
		super(10, true);
	}

	@Override
	public String getText() {
		return "Mondrune";
	}

	@Override
	public String toString() {
		return getText();
	}

	@Override
	public int dustCosts() {
		return COST;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		if(!f.canPayDust(COST)) return false;
		f.payDust(COST);
		f.heal(15, round);
		return true;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f.canPayDust(COST);
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
}
