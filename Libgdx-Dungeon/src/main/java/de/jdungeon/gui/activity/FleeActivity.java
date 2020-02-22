package de.jdungeon.gui.activity;

import dungeon.PositionInRoomInfo;
import dungeon.util.RouteInstruction;
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
		}
	}

	@Override
	public boolean isCurrentlyPossible() {
		return actionAssembler.getFigure().getPos().getPossibleFleeDirection() != null;
	}

	@Override
	public Object getObject() {
		return this;
	}

}
