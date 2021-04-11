package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.06.17.
 */
public abstract class AbstractTargetSpell extends AbstractSpell implements TargetSpell {

	public AbstractTargetSpell() {
		super();
	}

	public AbstractTargetSpell(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength, learnCost);
	}

	public AbstractTargetSpell(int level, int[] values) {
		super(level, values);
	}

	@Override
	public TargetScope getTargetScope() {
		return AbstractTargetScope.createDefaultScope(getTargetClass());
	}

	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return target.getInteractionPositions().contains(mage.getPos());
	}

}
