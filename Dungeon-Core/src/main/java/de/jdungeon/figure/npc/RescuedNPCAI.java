package de.jdungeon.figure.npc;

import java.util.List;

import de.jdungeon.ai.AI;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.Path;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.switchPos.CoopSwitchPosBehaviour;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.EndRoundAction;
import de.jdungeon.figure.action.MoveAction;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.percept.EntersPercept;
import de.jdungeon.figure.percept.FleePercept;
import de.jdungeon.figure.percept.LeavesPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.skill.attack.AttackSkill;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.08.16.
 */
public class RescuedNPCAI implements AI {

    enum State {
        prisoner,
        following,
        lost
    }

    private FigureInfo info;
    private HeroInfo leader;
    private JDPoint lastKnownLeaderPosition = null;
    private State currentState = State.prisoner;

    @Override
    public void tellPercept(Percept p) {
        if (currentState == State.prisoner) {
            // waiting for a hero to come and rescue
            List<FigureInfo> involvedFigures = p.getInvolvedFigures(this.info);
            for (FigureInfo involvedFigure : involvedFigures) {
                if (involvedFigure instanceof HeroInfo && involvedFigure.getFigureID() != this.info.getFigureID()) {
                    if (involvedFigure.getRoomInfo().getRoomNumber().equals(this.info.getRoomNumber())) {
                        currentState = State.following;
                        leader = (HeroInfo) involvedFigure;
                        lastKnownLeaderPosition = info.getRoomNumber();
                    }
                }
            }
        }

        if (currentState == State.following) {

            if (p.getInvolvedFigures(this.info) != null && p.getInvolvedFigures(info).contains(leader)) {
                // we assume that we have visibility of the leader's room as we have perceived the percept...
                // note: might fail for de.jdungeon.audio perceptions for instance

                if (p instanceof LeavesPercept) {
                    // LeavesPercept needs special treatment as it knows a more specific positon than the place where it occurs..
                    RouteInstruction.Direction direction = ((LeavesPercept) p).getDirection();
                    RoomInfo from = ((LeavesPercept) p).getFrom(this.info);
                    RoomInfo neighbourRoom = from.getNeighbourRoom(direction);
                    this.lastKnownLeaderPosition = neighbourRoom.getNumber();
                } else {
                    JDPoint roomNumber = leader.getRoomNumber();
                    // TODO: fix HeroInfo methods to filter available information properly by VisMap
                    if (roomNumber != null) {
                        lastKnownLeaderPosition = roomNumber;
                    }
                }
            }

            // the following percepts overwrite the information observed above

            // if the leader moves out of the room recognize target room
            if (p instanceof EntersPercept) {
                if (((EntersPercept) p).getFigure(this.info).equals(leader)) {
                    RoomInfo movedTo = ((EntersPercept) p).getTo(this.info);
                    lastKnownLeaderPosition = movedTo.getNumber();
                }
            }
            if (p instanceof FleePercept) {
                FleePercept fleePercept = (FleePercept) p;
                if (fleePercept.getFigure(this.info).equals(leader) &&
                        fleePercept.isSuccess()) {
                    RoomInfo fledTo = fleePercept.getRoom(this.info).getNeighbourRoom(fleePercept.getDir());
                    lastKnownLeaderPosition = fledTo.getNumber();
                }
            }
        }
    }

    @Override
    public boolean isHostileTo(FigureInfo f) {
        if (f instanceof MonsterInfo) {
            return true;
        }
        if (f.equals(leader)) {
            return false;
        }
        return false;
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

    @Override
    public Action chooseFightAction() {
        int minDistanceToEnemy = Integer.MAX_VALUE;
        List<FigureInfo> figureInfos = info.getRoomInfo().getFigureInfos();
        FigureInfo target = null;

        // if we can help a friend, we will do
        Action coopAction = new CoopSwitchPosBehaviour(this.info).chooseFightAction();
        if (coopAction != null) return coopAction;

        // further, we attack some random enemy
        for (FigureInfo figureInfo : figureInfos) {
            if (figureInfo.equals(info)) {
                // do not attack yourself
                continue;
            }
            if (info.isHostile(figureInfo)) {
                int distance = Position.getMinDistanceFromTo(this.info.getPositionInRoomIndex(), figureInfo.getPositionInRoomIndex());
                if (distance < minDistanceToEnemy) {
                    minDistanceToEnemy = distance;
                    target = figureInfo;
                }
            }
        }
        if (target != null) {
            return this.info.getSkill(AttackSkill.class)
                    .newActionFor(info)
                    .target(target)
                    .get();
        }
        return null;
    }

    private boolean leaderInRoom() {
        if (leader != null) {
            List<FigureInfo> roomFigures = info.getRoomInfo().getFigureInfos();
            if (roomFigures.contains(leader)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Action chooseMovementAction() {

        if (currentState == State.prisoner) {
            return new EndRoundAction();
        }

        if (currentState == State.following) {
            if (leaderInRoom()) {
                // if we can help a friend, we will do
                Action coopAction = new CoopSwitchPosBehaviour(this.info).chooseMovementAction();
                if (coopAction != null) return coopAction;

                int leaderPosIndex = leader.getPositionInRoomIndex();
                int positionRightOfLeader = Position.getNextIndex(leaderPosIndex, true);
                PositionInRoomInfo positionNextOnTheRight = info.getRoomInfo().getPositionInRoom(positionRightOfLeader);
                if (!positionNextOnTheRight.isOccupied() && !(info.getPositionInRoomIndex() == positionNextOnTheRight.getIndex())) {
                    return new StepAction(this.info, positionRightOfLeader);
                }
                int positionLeftOfLeader = Position.getNextIndex(leaderPosIndex, false);
                PositionInRoomInfo leftPosition = info.getRoomInfo().getPositionInRoom(positionLeftOfLeader);
                if (!leftPosition.isOccupied()
                        && !(info.getPositionInRoomIndex() == leftPosition.getIndex())) {
                    return new StepAction(this.info, positionLeftOfLeader);
                }
                // otherwise we just wait
                return new EndRoundAction();
            } else {
                return followLeader();
            }
        }

        if (currentState == State.lost) {
            return followLeader();
        }
        return new EndRoundAction();
    }

    private Action followLeader() {
        if (!this.info.getRoomInfo().getRoomNumber().equals(lastKnownLeaderPosition)) {
            Path shortestWayFromTo = info.getShortestWayFromTo(info.getRoomInfo().getNumber(), lastKnownLeaderPosition);
            RoomInfo nextRoomToGo = shortestWayFromTo.get(1);
            JDPoint firstMove = nextRoomToGo.getNumber();
            RouteInstruction.Direction directionToMove = info.getRoomNumber().relativeTo(firstMove);
            // TODO: make it easier for AI to step towards the door for a move
            int positionInRoomAtDoor = Figure.getDirPos(directionToMove.getValue());
            if (info.getPositionInRoomIndex() == positionInRoomAtDoor) {
                return new MoveAction(this.info, this.info.getRoomNumber(), directionToMove);
            } else {
                return new StepAction(this.info, positionInRoomAtDoor);
            }
        } else {
            // we wait here for the hero to hopefully return
            return new EndRoundAction();
        }
    }
}
