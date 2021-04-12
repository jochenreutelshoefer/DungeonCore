package de.jdungeon.gui.activity;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.ai.DefaultMonsterIntelligence;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.action.result.ActionResult;

import de.jdungeon.app.ActionAssembler;
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
	public ActivityPlan createExecutionPlan(boolean doIt, Object target) {
		List<Action> actions = new ArrayList<>();
		ActionAssembler actionAssembler = playerController.getActionAssembler();
		PositionInRoomInfo pos = actionAssembler.getFigure().getPos();
		RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
		if (possibleFleeDirection != null) {
			actions.addAll(actionAssembler.getActionAssemblerHelper().wannaFlee());
		} else {
			StepAction stepActionToDoor = DefaultMonsterIntelligence.getStepActionToDoor(actionAssembler.getFigure());
			if(stepActionToDoor != null) {
				actions.add(stepActionToDoor);
			}
		}
		return new SimpleActivityPlan(this, actions);
	}

	@Override
	public ActionResult possible(Object target) {
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
