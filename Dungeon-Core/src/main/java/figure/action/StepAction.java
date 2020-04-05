/*
 * Created on 01.12.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import dungeon.Position;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;

public class StepAction extends AbstractExecutableAction {

	private int targetIndex = -1;
	private final Figure figure;

	public StepAction(FigureInfo figureInfo, int index) {
		super();
		targetIndex = index;
		figure = figureInfo.getMap().getDungeon().getFigureIndex().get(figureInfo.getFighterID());
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		if (targetIndex == -1) {
			return ActionResult.NO_TARGET;
		}
		if (figure.getRoom().fightRunning()) {
			if (figure.canPayActionPoints(1)) {

				if (figure.isPinnedToGround()) {
					return ActionResult.OTHER;
				}

				Position newPos = figure.getRoom().getPositions()[targetIndex];
				Figure neighbour;

				int oldPosIndex = figure.getPos().getIndex();
				if (figure.getPos().getDistanceTo(newPos) == 1) {
					neighbour = newPos.getFigure();
					if (neighbour == null) {
						if (doIt) {
							figure.doStepTo(targetIndex, oldPosIndex, round);
							figure.payActionPoint(this, round);
							return ActionResult.DONE;
						}
						return ActionResult.POSSIBLE;
					}
					else {
						return ActionResult.WRONG_TARGET;
					}
				}
				return ActionResult.POSITION;
			}
			else {
				return ActionResult.NOAP;
			}
		}
		else {
			if (figure.getActionPoints() < 1) {
				return ActionResult.NOAP;
			}
			Position newPos = figure.getRoom().getPositions()[targetIndex];
			if (newPos.getFigure() != null) {
				return ActionResult.WRONG_TARGET;
			}
			if (doIt) {
				figure.doStepTo(targetIndex, figure.getPos().getIndex(), round);
				figure.payMoveActionPoint(this, round);
				return ActionResult.DONE;
			}
			return ActionResult.POSSIBLE;
		}
	}
}
