package de.jdungeon.ai;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.HeroInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.04.16.
 */
public abstract class AbstractMonsterBehaviour implements AI {

	protected FigureInfo info;

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return f instanceof HeroInfo;
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {

	}

	@Override
	public void setFigure(FigureInfo info) {

		this.info = info;
	}

}
