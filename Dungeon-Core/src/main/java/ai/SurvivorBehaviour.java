package ai;

import java.util.ArrayList;
import java.util.List;

import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.HealthLevel;
import figure.action.Action;
import figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class SurvivorBehaviour extends AbstractAI {

	private final FigureInfo figure;
	private final HeroPositionLog heroLog;
	private final AbstractAI defaultAI;
	private final ActionAssemblerHelper actionAssembler;
	List<Action> plannedActions = new ArrayList<>();

	public SurvivorBehaviour(FigureInfo figure) {
		super(new AttitudeMonsterDefault());
		this.figure = figure;
		defaultAI = new DefaultMonsterIntelligence();
		defaultAI.setFigure(figure);
		actionAssembler = new ActionAssemblerHelper(figure);
		heroLog = new HeroPositionLog(figure);
	}

	@Override
	protected void processPercept(Percept p) {
		heroLog.tellPecept(p);
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public Action chooseFightAction() {
		if (figure.getHealthLevel().getValue() < HealthLevel.Good.getValue()) {
			return DefaultMonsterIntelligence.getFleeAction(figure);
		}
		return defaultAI.chooseFightAction();
	}

	@Override
	public Action chooseMovementAction() {
		if(!plannedActions.isEmpty()) {
			return plannedActions.remove(0);
		}
		heroLog.processPercepts();

		if (figure.getHealthLevel().getValue() < HealthLevel.Good.getValue()) {
			JDPoint location = this.figure.getRoomInfo().getRoomNumber();
			JDPoint lastHeroPosition = heroLog.getLastHeroPosition();
			int diffX = location.getX() - lastHeroPosition.getX();
			int diffY = location.getY() - lastHeroPosition.getY();
			RouteInstruction.Direction dir = null;
			if(diffX == 1) {
				 dir = RouteInstruction.Direction.West;
			}
			if(diffX == -1) {
				dir = RouteInstruction.Direction.East;
			}
			if(diffY == 1) {
				dir = RouteInstruction.Direction.South;
			}
			if(diffY == -1) {
				 dir = RouteInstruction.Direction.North;
			}

			if(dir != null) {
				DoorInfo door = figure.getRoomInfo().getDoor(dir);
				if(door != null && door.isPassable()) {
					List<Action> actions = actionAssembler.wannaWalk(dir.getValue());
					plannedActions.addAll(actions);
				}
 			}
		}

		return defaultAI.chooseMovementAction();
	}
}
