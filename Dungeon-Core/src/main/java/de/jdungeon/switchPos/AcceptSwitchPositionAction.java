package de.jdungeon.switchPos;

import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.AbstractExecutableAction;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.StepPercept;

/**
 * Action that accepts a switch position proposition posed by an allied de.jdungeon.figure.
 * In de.jdungeon.fight situations, only neighbour positions can be switched.
 */
public class AcceptSwitchPositionAction extends AbstractExecutableAction {

    private final Figure requestingFigure;
    private final Figure requestedFigure;

    public AcceptSwitchPositionAction(FigureInfo requestingFigure, FigureInfo requestedFigure) {
        if (requestedFigure.equals(requestingFigure)) {
            throw new IllegalArgumentException("For switching places, figures must not be the same: " + requestedFigure + " - " + requestingFigure);
        }
        this.requestingFigure = requestingFigure.getVisMap().getDungeon().getFigureIndex().get(requestingFigure.getFigureID());
        this.requestedFigure = requestedFigure.getVisMap().getDungeon().getFigureIndex().get(requestedFigure.getFigureID());
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

                // distribute percept to trigger de.jdungeon.animation on UI
                room.distributePercept(new StepPercept(requestingFigure, requestingFigurePosIndex, requestedFigurePosIndex, round));
                room.distributePercept(new StepPercept(requestedFigure, requestedFigurePosIndex, requestingFigurePosIndex, round));
                room.getDungeon().getSwitchPositionRequestManager().removePosSwitchRequest(new SwitchPositionRequestManager.SwitchPosRequest(requestingFigure, requestedFigure));
                return ActionResult.DONE;
            } else {
                return ActionResult.POSSIBLE;
            }
        }
        return ActionResult.POSITION;
    }
}
