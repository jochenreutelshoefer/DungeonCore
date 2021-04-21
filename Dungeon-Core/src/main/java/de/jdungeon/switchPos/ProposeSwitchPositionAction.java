package de.jdungeon.switchPos;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.AbstractExecutableAction;
import de.jdungeon.figure.action.result.ActionResult;

/**
 * Action that allows to propose a position switch to an allied de.jdungeon.figure.
 * In de.jdungeon.fight situations, only neighbour positions can be switched.
 */
public class ProposeSwitchPositionAction extends AbstractExecutableAction {

    private final Figure requestingFigure;
    private final Figure requestedFigure;

    public ProposeSwitchPositionAction(FigureInfo requestingFigure, FigureInfo requestedFigure) {
        if (requestedFigure.equals(requestingFigure)) {
            throw new IllegalArgumentException("For switching places, figures must not be the same: " + requestedFigure + " - " + requestingFigure);
        }
        this.requestingFigure = requestingFigure.getVisMap().getDungeon().getFigureIndex().get(requestingFigure.getFigureID());
        this.requestedFigure = requestedFigure.getVisMap().getDungeon().getFigureIndex().get(requestedFigure.getFigureID());
    }

    @Override
    public ActionResult handle(boolean doIt, int round) {
        if (doIt) {
            SwitchPositionRequestManager.SwitchPosRequest request = new SwitchPositionRequestManager.SwitchPosRequest(requestingFigure, requestedFigure);
            requestingFigure.getDungeon().getSwitchPositionRequestManager().addPosSwitchRequest(request);
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }
}
