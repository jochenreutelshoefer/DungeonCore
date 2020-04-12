package de.jdungeon.gui.activity;

import java.util.ArrayList;
import java.util.List;

import ai.DefaultMonsterIntelligence;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.StepAction;
import figure.action.result.ActionResult;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class FleeActivity extends AbstractExecutableActivity {


	public FleeActivity(PlayerController controller) {
		super(controller);
	}

	@Override
	public ActivityPlan createExecutionPlan() {
		List<Action> actions = new ArrayList<>();
		ActionAssembler actionAssembler = playerController.getActionAssembler();
		PositionInRoomInfo pos = actionAssembler.getFigure().getPos();
		RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
		if (possibleFleeDirection != null) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			actions.addAll(actionAssembler.getActionAssemblerHelper().wannaFlee());
		} else {
			StepAction stepActionToDoor = DefaultMonsterIntelligence.getStepActionToDoor(actionAssembler.getFigure());
			if(stepActionToDoor != null) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actions.add(stepActionToDoor);
			} else {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
			}
		}
		return new SimpleActivityPlan(this, actions);
	}

	@Override
	public ActionResult possible() {
		FigureInfo figure = playerController.getActionAssembler().getFigure();
		RoomInfo roomInfo = figure.getRoomInfo();
		if(roomInfo == null) return ActionResult.UNKNOWN;
		PositionInRoomInfo pos = figure.getPos();
		if(pos == null) return ActionResult.UNKNOWN;
		Boolean fightRunning = roomInfo.fightRunning();
		boolean mode = fightRunning != null && fightRunning;
		if(!mode) return ActionResult.MODE;
		boolean ok = (pos.getPossibleFleeDirection() != null
					|| DefaultMonsterIntelligence.getStepActionToDoor(figure) != null);
		if(ok ) {
			return ActionResult.POSSIBLE;
		} else {
			return ActionResult.POSITION;
		}

	}

	@Override
	public Object getObject() {
		return this;
	}

}
