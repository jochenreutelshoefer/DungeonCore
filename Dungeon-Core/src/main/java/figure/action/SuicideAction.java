package figure.action;

import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;

public class SuicideAction extends AbstractExecutableAction {

	private final Figure figure;

	public SuicideAction(FigureInfo info) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		figure.getKilled(-1);
		return ActionResult.DONE;
	}
}
