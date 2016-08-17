package ai;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.action.Action;
import figure.hero.HeroInfo;
import figure.percept.Percept;

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
