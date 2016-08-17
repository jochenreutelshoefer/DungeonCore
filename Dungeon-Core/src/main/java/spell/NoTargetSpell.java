package spell;

import figure.Figure;
import game.InfoEntity;

public abstract class NoTargetSpell extends Spell {

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
	public Class<? extends InfoEntity> getTargetClass() {
		return null;
	}

	@Override
	public void sorcer(Figure mage, Object target) {
		sorcer(mage);

	}

	protected abstract void sorcer(Figure mage);

}
