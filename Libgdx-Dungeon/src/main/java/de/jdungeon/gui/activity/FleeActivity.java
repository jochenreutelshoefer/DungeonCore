package de.jdungeon.gui.activity;

import ai.DefaultMonsterIntelligence;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.action.StepAction;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class FleeActivity extends AbstractExecutableActivity {
	private final PlayerController controller;

	public FleeActivity(PlayerController controller) {
		this.controller = controller;
	}

	@Override
	public void execute() {
		ActionAssembler actionAssembler = controller.getActionAssembler();
		PositionInRoomInfo pos = actionAssembler.getFigure().getPos();
		RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
		if (possibleFleeDirection != null) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaFlee());
		} else {
			StepAction stepActionToDoor = DefaultMonsterIntelligence.getStepActionToDoor(actionAssembler.getFigure());
			if(stepActionToDoor != null) {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
				actionAssembler.plugAction(stepActionToDoor);
			} else {
				AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
			}
		}
	}

	@Override
	public boolean isCurrentlyPossible() {
		RoomInfo roomInfo = controller.getActionAssembler().getFigure().getRoomInfo();
		if(roomInfo == null) return false;
		PositionInRoomInfo pos = controller.getActionAssembler().getFigure().getPos();
		if(pos == null) return false;
		Boolean fightRunning = roomInfo.fightRunning();
		return fightRunning != null &&  fightRunning && (pos.getPossibleFleeDirection() != null || DefaultMonsterIntelligence.getStepActionToDoor(controller.getActionAssembler().getFigure()) != null);
	}

	@Override
	public Object getObject() {
		return this;
	}

}
