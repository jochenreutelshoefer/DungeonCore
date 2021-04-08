package switchPos;

import figure.Figure;
import figure.FigureInfo;
import figure.action.AbstractExecutableAction;
import figure.action.result.ActionResult;

/**
 * Action that allows to propose a position switch to an allied figure.
 * In fight situations, only neighbour positions can be switched.
 */
public class ProposeSwitchPositionAction extends AbstractExecutableAction {

    private final Figure requestingFigure;
    private final Figure requestedFigure;

    public ProposeSwitchPositionAction(FigureInfo requestingFigure, FigureInfo requestedFigure) {
        if (requestedFigure.equals(requestingFigure)) {
            throw new IllegalArgumentException("For switching places, figures must not be the same: " + requestedFigure + " - " + requestingFigure);
        }
        this.requestingFigure = requestingFigure.getMap().getDungeon().getFigureIndex().get(requestingFigure.getFigureID());
        this.requestedFigure = requestedFigure.getMap().getDungeon().getFigureIndex().get(requestedFigure.getFigureID());
    }

    @Override
    public ActionResult handle(boolean doIt, int round) {
        if (doIt) {
            SwitchPositionRequestManager.SwitchPosRequest request = new SwitchPositionRequestManager.SwitchPosRequest(requestingFigure, requestedFigure);
            SwitchPositionRequestManager.getInstance().addPosSwitchRequest(request);
            return ActionResult.DONE;
        } else {
            return ActionResult.POSSIBLE;
        }
    }
}
