package de.jdungeon.gui.activity;

import java.util.Collections;
import java.util.List;

import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.RoomObservationStatus;
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
public class DirectionScoutActivity extends ScoutActivity {

	private final RouteInstruction.Direction direction;

	public DirectionScoutActivity(PlayerController playerController, RouteInstruction.Direction direction) {
		super(playerController);
		this.direction = direction;
	}

	@Override
	public Object getObject() {
		return SkillActivityProvider.SCOUT;
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt, Object target) {
		return super.createExecutionPlan(doIt, direction);
	}

	@Override
	public ActionResult possible(Object target) {
		return super.possible(direction);
	}

}
