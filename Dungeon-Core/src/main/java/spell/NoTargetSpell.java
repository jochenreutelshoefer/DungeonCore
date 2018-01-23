package spell;

import dungeon.RoomEntity;
import figure.Figure;
import game.InfoEntity;

public abstract class NoTargetSpell extends AbstractSpell {

	public NoTargetSpell(int level, int diffMin, int diff, int cost,
			int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength, learnCost);
	}

	public NoTargetSpell(int level, int[] values) {
		super(level, values);
	}

	public NoTargetSpell() {
		super();
	}


	@Override
	public void sorcer(Figure mage, RoomEntity target) {
		sorcer(mage);

	}

	protected abstract void sorcer(Figure mage);

}
