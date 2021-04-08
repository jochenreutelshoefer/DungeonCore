package switchPos;

import ai.FightIntelligence;
import ai.MovementIntelligence;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import figure.FigureInfo;
import figure.action.Action;

import java.util.Collection;

/**
 * Allows an AI to accept pos switch propositions brought up by allied figures.
 */
public class CoopSwitchPosBehaviour implements FightIntelligence, MovementIntelligence {

    private final FigureInfo info;

    public CoopSwitchPosBehaviour(FigureInfo info) {
        this.info = info;
    }

    @Override
    public Action chooseFightAction() {
        Collection<SwitchPositionRequestManager.SwitchPosRequest> switchPosRequests = this.info.getRoomInfo().getSwitchPosRequests(this.info);
        if (switchPosRequests == null || switchPosRequests.isEmpty()) {
            return null;
        }
        SwitchPositionRequestManager.SwitchPosRequest switchPosRequest = switchPosRequests.iterator().next();
        Position requestPos = switchPosRequest.getRequestingFigure().getPos();
        PositionInRoomInfo currentPos = this.info.getPos();
        int distance = requestPos.getDistance(currentPos.getIndex());
        if (distance == 1) {
            return new AcceptSwitchPositionAction(
                    FigureInfo.makeFigureInfo(switchPosRequest.getRequestingFigure(), info.getMap()),
                    FigureInfo.makeFigureInfo(switchPosRequest.getRequestedFigure(), info.getMap())
            );
        } else {
            // to far away to switch places!
            return null;
        }
    }

    @Override
    public Action chooseMovementAction() {
        Collection<SwitchPositionRequestManager.SwitchPosRequest> switchPosRequests = this.info.getRoomInfo().getSwitchPosRequests(this.info);
        if (switchPosRequests == null || switchPosRequests.isEmpty()) {
            return null;
        }
        SwitchPositionRequestManager.SwitchPosRequest switchPosRequest = switchPosRequests.iterator().next();
        return new AcceptSwitchPositionAction(
                FigureInfo.makeFigureInfo(switchPosRequest.getRequestingFigure(), info.getMap()),
                FigureInfo.makeFigureInfo(switchPosRequest.getRequestedFigure(), info.getMap())
        );
    }
}
