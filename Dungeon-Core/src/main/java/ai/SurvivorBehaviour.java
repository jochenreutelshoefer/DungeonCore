package ai;

import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.HealthLevel;
import figure.action.Action;
import figure.action.MoveAction;
import figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class SurvivorBehaviour extends AbstractAI {

	private FigureInfo figure;
	private final HeroPositionLog heroLog = new HeroPositionLog();
	private AbstractAI defaultAI;

	public SurvivorBehaviour(FigureInfo figure) {
		super(new AttitudeMonsterDefault());
		this.figure = figure;
		defaultAI = new DefaultMonsterIntelligence();
		defaultAI.setFigure(figure);

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
		if (figure.getHealthLevel() < HealthLevel.Good.getValue()) {
			return DefaultMonsterIntelligence.getFleeAction(figure);
		}
		return defaultAI.chooseFightAction();
	}

	@Override
	public Action chooseMovementAction() {
		heroLog.processPercepts();

		if (figure.getHealthLevel() < HealthLevel.Good.getValue()) {
			JDPoint location = this.figure.getRoomInfo().getLocation();
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

			// TODO: implement reusable multi-round strategy 'travel <DIRECTION>'

			if(dir != null) {
				DoorInfo door = figure.getRoomInfo().getDoor(dir);
				if(door != null && door.isPassable()) {
					return new MoveAction(dir);
				}
 			}
		}

		return defaultAI.chooseMovementAction();
	}
}
