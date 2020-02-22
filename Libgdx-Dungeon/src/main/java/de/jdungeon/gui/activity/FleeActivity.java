package de.jdungeon.gui.activity;

import ai.DefaultMonsterIntelligence;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.action.StepAction;
import game.RoomInfoEntity;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class FleeActivity extends AbstractExecutableActivity {
	private final ActionAssembler actionAssembler;

	public FleeActivity(ActionAssembler actionAssembler) {
		this.actionAssembler = actionAssembler;
	}

	@Override
	public void execute() {
		PositionInRoomInfo pos = actionAssembler.getFigure().getPos();
		RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
		if (possibleFleeDirection != null) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			actionAssembler.plugActions(actionAssembler.getActionAssembler().wannaFlee());
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
		RoomInfo roomInfo = actionAssembler.getFigure().getRoomInfo();
		if(roomInfo == null) return false;
		PositionInRoomInfo pos = actionAssembler.getFigure().getPos();
		if(pos == null) return false;
		Boolean fightRunning = roomInfo.fightRunning();
		return fightRunning != null &&  fightRunning && (pos.getPossibleFleeDirection() != null || DefaultMonsterIntelligence.getStepActionToDoor(actionAssembler.getFigure()) != null);
	}

	@Override
	public Object getObject() {
		return this;
	}

}
