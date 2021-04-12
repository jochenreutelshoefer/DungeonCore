package de.jdungeon.gui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.Dir;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.gui.activity.SkillActivityProvider;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.04.20.
 */
public class ScoutActivity extends AbstractExecutableActivity {


	public ScoutActivity(PlayerController playerController) {
		super(playerController);
	}

	@Override
	public Object getObject() {
		return SkillActivityProvider.SCOUT;
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt, Object target) {
		List<Action> actions = scoutingActivity(target);
		return new SimpleActivityPlan(this, actions);
	}

	private List<Action> scoutingActivity(Object highlightedEntity) {
		ActionAssembler actionAssembler = playerController.getActionAssembler();
		FigureInfo figure = actionAssembler.getFigure();

		if (highlightedEntity != null) {
			if (highlightedEntity instanceof RoomInfo) {
				int directionToScout = Dir.getDirFromToIfNeighbour(figure.getRoomNumber(), ((RoomInfo) highlightedEntity).getNumber());
				return actionAssembler.wannaScout(directionToScout);
			}
			else if (highlightedEntity instanceof DoorInfo) {
				int directionToScout = ((DoorInfo) highlightedEntity).getDir(figure.getRoomNumber());
				return actionAssembler.wannaScout(directionToScout);
			}
			else if (highlightedEntity instanceof RouteInstruction.Direction) {
				DoorInfo door = figure.getRoomInfo().getDoor((RouteInstruction.Direction)highlightedEntity);
				if (door != null) {
					return actionAssembler.wannaScout(((RouteInstruction.Direction)highlightedEntity).getValue());
				}
			}
		}
		else {
			PositionInRoomInfo pos = figure.getPos();
			if(pos == null) {
				// hero dead, de.jdungeon.game over but de.jdungeon.gui still active
				return Collections.emptyList();
			}
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();

			if (possibleFleeDirection != null) {
				DoorInfo door = figure.getRoomInfo().getDoor(possibleFleeDirection);
				if (door != null) {
					return actionAssembler.wannaScout(possibleFleeDirection.getValue());
				}
			}
		}
		return Collections.emptyList();
	}


	@Override
	public ActionResult possible(Object target) {
		ActionAssembler actionAssembler = playerController.getActionAssembler();
		final RoomInfo roomInfo = actionAssembler.getFigure().getRoomInfo();
		if (roomInfo == null) {
			// should not happen
			return ActionResult.UNKNOWN;
		}

		// check de.jdungeon.fight mode
		Boolean fightRunning = roomInfo.fightRunning();
		boolean fight = fightRunning != null && fightRunning;
		if(fight) {
			return ActionResult.MODE;
		}

		RoomInfo targetRoom = findTargetRoom(target);
		if (targetRoom == null) return ActionResult.WRONG_TARGET;
		if(targetRoom.getConnectionDirectionTo(roomInfo)== -1)  return ActionResult.WRONG_TARGET;
		int scoutDir = roomInfo.getDirectionTo(targetRoom);
		DoorInfo door = roomInfo.getDoor(scoutDir);
		PositionInRoomInfo scoutPosition = door.getPositionAtDoor(roomInfo, false);


		if(door.getOtherRoom(roomInfo).getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FIGURES) {
			// already visible
			return ActionResult.WRONG_TARGET;
		}

		boolean ok = (!scoutPosition.isOccupied() || actionAssembler.getFigure().equals(scoutPosition.getFigure()));
		if(ok) {
			ActivityPlan executionPlan = createExecutionPlan(false, target);
			if(executionPlan != null && executionPlan.getLength() > 0) {
				return ActionResult.POSSIBLE;
			} else {
				// should not happen
				return ActionResult.UNKNOWN;
			}
		}
		else {
			return ActionResult.POSITION;
		}
	}

	private RoomInfo findTargetRoom(Object highlightedEntity) {
		// case we have a target object
		ActionAssembler actionAssembler = playerController.getActionAssembler();
		FigureInfo figure = actionAssembler.getFigure();
		RoomInfo figureRoom = figure.getRoomInfo();
		if (highlightedEntity != null) {
			if (highlightedEntity instanceof RoomInfo) {
				return ((RoomInfo) highlightedEntity);
			}
			else if (highlightedEntity instanceof DoorInfo) {
				DoorInfo door = (DoorInfo) highlightedEntity;
				return door.getOtherRoom(figureRoom);
			} else if(highlightedEntity instanceof RouteInstruction.Direction) {
				RouteInstruction.Direction direction = (RouteInstruction.Direction) highlightedEntity;
				DoorInfo door = figure.getRoomInfo().getDoor(direction);
				if(door == null) {
					// no door to scout in this direction
					return null;
				}
				return figureRoom.getNeighbourRoom(direction);
			}
		}
		// case we have no target object
		else {
			PositionInRoomInfo pos = figure.getPos();
			if(pos == null) {
				// hero dead, de.jdungeon.game over but de.jdungeon.gui still active
				return null;
			}

			// we wanna find out some unique scouting target

			// if we stand next to a door, we scout this door
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
			if (possibleFleeDirection != null) {
				return figureRoom.getNeighbourRoom(possibleFleeDirection);
			}

			// check for some neighbour room target
			DoorInfo[] doors = figureRoom.getDoors();
			List<RoomInfo> potentialTargets = new ArrayList<>();
			for (DoorInfo door : doors) {
				if(door != null) {
					RoomInfo otherRoom = door.getOtherRoom(figureRoom);
					if(otherRoom != null && otherRoom.getVisibilityStatus() < RoomObservationStatus.VISIBILITY_FIGURES) {
						potentialTargets.add(otherRoom);
					}
				}
			}
			if(potentialTargets.size() == 1) {
				// we found a unique target
				return potentialTargets.get(0);
			}
		}
		return null;
	}
}

