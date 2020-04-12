package de.jdungeon.gui.activity;

import java.util.Collections;
import java.util.List;

import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import game.RoomInfoEntity;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.SkillActivityProvider;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class ScoutActivity extends AbstractExecutableActivity {

	private final ActionAssembler actionAssembler;
	private final RouteInstruction.Direction direction;

	public ScoutActivity(PlayerController playerController, RouteInstruction.Direction direction) {
		super(playerController);
		this.actionAssembler = playerController.getActionAssembler();
		this.direction = direction;
	}

	@Override
	public Object getObject() {
		return SkillActivityProvider.SCOUT;
	}

	@Override
	public ActivityPlan createExecutionPlan() {
		List<Action> actions = scoutingActivity(actionAssembler.getFigure().getRoomInfo().getDoor(direction));
		return new SimpleActivityPlan(this, actions);
	}

	public List<Action> scoutingActivity(RoomInfoEntity highlightedEntity) {
		FigureInfo figure = actionAssembler.getFigure();

		if (highlightedEntity != null) {
			if (highlightedEntity instanceof RoomInfo) {
				int directionToScout = Dir.getDirFromToIfNeighbour(
						figure.getRoomNumber(),
						((RoomInfo) highlightedEntity).getNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				List<Action> actions = actionAssembler.wannaScout(directionToScout);
				return actions;
			}
			else if (highlightedEntity instanceof DoorInfo) {
				int directionToScout = ((DoorInfo) highlightedEntity).getDir(figure.getRoomNumber());
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				List<Action> actions = actionAssembler.wannaScout(directionToScout);
				return actions;
			}
		}
		else {
			PositionInRoomInfo pos = figure.getPos();
			if(pos == null) {
				// hero dead, game over but gui still active
				return Collections.emptyList();
			}
			RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();

			if (possibleFleeDirection != null) {
				DoorInfo door = figure.getRoomInfo().getDoor(
						possibleFleeDirection);
				if (door != null) {
					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
					List<Action> actions = actionAssembler.wannaScout(possibleFleeDirection.getValue());
					return actions;
				}
			}
		}
		return Collections.emptyList();
	}


	@Override
	public ActionResult possible() {
		final RoomInfo roomInfo = actionAssembler.getFigure().getRoomInfo();
		if (roomInfo == null) {
			return ActionResult.UNKNOWN;
		}
		Boolean fightRunning = roomInfo.fightRunning();
		DoorInfo door = roomInfo.getDoor(direction);
		if (door == null) return ActionResult.WRONG_TARGET;
		PositionInRoomInfo scoutPosition = door.getPositionAtDoor(roomInfo, false);
		boolean fight = fightRunning != null && fightRunning;
		if(fight) {
			return ActionResult.MODE;
		}

		boolean ok = (!scoutPosition.isOccupied() || actionAssembler.getFigure().equals(scoutPosition.getFigure()));
		if(ok) {
			return ActionResult.POSSIBLE;
		}
		else {
			return ActionResult.POSITION;
		}
	}
}
