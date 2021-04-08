package switchPos;

import dungeon.Position;
import dungeon.Room;
import figure.Figure;
import figure.FigureInfo;
import figure.action.AbstractExecutableAction;
import figure.action.result.ActionResult;
import figure.percept.StepPercept;

/**
 * Action that accepts a switch position proposition posed by an allied figure.
 * In fight situations, only neighbour positions can be switched.
 */
public class AcceptSwitchPositionAction extends AbstractExecutableAction {

    private final Figure requestingFigure;
    private final Figure requestedFigure;

    public AcceptSwitchPositionAction(FigureInfo requestingFigure, FigureInfo requestedFigure) {
        if (requestedFigure.equals(requestingFigure)) {
            throw new IllegalArgumentException("For switching places, figures must not be the same: " + requestedFigure + " - " + requestingFigure);
        }
        this.requestingFigure = requestingFigure.getMap().getDungeon().getFigureIndex().get(requestingFigure.getFigureID());
        this.requestedFigure = requestedFigure.getMap().getDungeon().getFigureIndex().get(requestedFigure.getFigureID());
    }

    @Override
    public ActionResult handle(boolean doIt, int round) {
        Room room = requestingFigure.getRoom();
        Position requestedFigurePos = requestedFigure.getPos();
        Position requestingFigurePos = requestingFigure.getPos();
        // it only works if the two figures stand on positions next to each other (distance == 1)
        if (!room.fightRunning() || requestedFigurePos.getDistanceTo(requestingFigurePos) == 1) {
            if (doIt) {
                // just switch places
                int requestedFigurePosIndex = requestedFigurePos.getIndex();
                int requestingFigurePosIndex = requestingFigurePos.getIndex();
                requestedFigurePos.setFigure(null);
                requestingFigurePos.setFigure(null);
                requestingFigure.setPos(null);
                requestedFigure.setPos(null);

                // look direction during movement
                int moveDirRequester = Position.getDirFromTo(requestedFigurePosIndex, requestingFigurePosIndex);
                int moveDirRequested = Position.getDirFromTo(requestingFigurePosIndex, requestedFigurePosIndex);

                room.figureEntersAtPosition(requestingFigure, moveDirRequested, requestedFigurePosIndex, round);
                room.figureEntersAtPosition(requestedFigure, moveDirRequester, requestingFigurePosIndex, round);

                // distribute percept to trigger animation on UI
                room.distributePercept(new StepPercept(requestingFigure, requestingFigurePosIndex, requestedFigurePosIndex, round));
                room.distributePercept(new StepPercept(requestedFigure, requestedFigurePosIndex, requestingFigurePosIndex, round));
                SwitchPositionRequestManager.getInstance().removePosSwitchRequest(new SwitchPositionRequestManager.SwitchPosRequest(requestingFigure, requestedFigure));
                return ActionResult.DONE;
            } else {
                return ActionResult.POSSIBLE;
            }
        }
        return ActionResult.POSITION;
    }
}
