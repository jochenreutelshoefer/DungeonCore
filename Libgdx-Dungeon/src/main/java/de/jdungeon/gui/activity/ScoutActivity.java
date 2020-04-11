package de.jdungeon.gui.activity;

import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import game.RoomInfoEntity;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.app.gui.activity.SkillActivityProvider;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class ScoutActivity extends AbstractExecutableActivity {

	private final ActionAssembler actionAssembler;
	private final RouteInstruction.Direction direction;

	public ScoutActivity(ActionAssembler actionAssembler, RouteInstruction.Direction direction) {
		this.actionAssembler = actionAssembler;
		this.direction = direction;
	}

	@Override
	public Object getObject() {
		return SkillActivityProvider.SCOUT;
	}

	@Override
	public void execute() {
		actionAssembler.scoutingActivity(actionAssembler.getFigure().getRoomInfo().getDoor(direction));
	}

	@Override
	public boolean isCurrentlyPossible() {
		final RoomInfo roomInfo = actionAssembler.getFigure().getRoomInfo();
		if (roomInfo == null) {
			return false;
		}
		Boolean fightRunning = roomInfo.fightRunning();
		DoorInfo door = roomInfo.getDoor(direction);
		if (door == null) return false;
		PositionInRoomInfo scoutPosition = door.getPositionAtDoor(roomInfo, false);
		return fightRunning != null && !fightRunning && (!scoutPosition.isOccupied() || actionAssembler.getFigure()
				.equals(scoutPosition.getFigure()));
	}
}
