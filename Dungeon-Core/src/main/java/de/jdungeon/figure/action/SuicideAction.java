package de.jdungeon.figure.action;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;

public class SuicideAction extends AbstractExecutableAction {

	private final Figure figure;

	public SuicideAction(FigureInfo info) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFigureID());
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		figure.getKilled(-1);
		return ActionResult.DONE;
	}
}
