package ai;

import dungeon.JDPoint;
import dungeon.Position;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.DoNothingAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.04.16.
 */
public class GuardPositionBehaviour extends AbstractMonsterBehaviour {

	private Position pos;
	DefaultMonsterIntelligence defaultFightAI = new DefaultMonsterIntelligence();

	public GuardPositionBehaviour(Position pos) {
		this.pos = pos;
	}

	@Override
	public void tellPercept(Percept p) {

	}

	@Override
	public void setFigure(FigureInfo info) {
		super.setFigure(info);
		defaultFightAI.setFigure(info);
	}

	@Override
	public Action chooseFightAction() {
		int distance = pos.getDistance(info.getPositionInRoomIndex());
		// TODO: wenn gegner gleich nah POS, dann richtung POS
		if(distance > 1) {
			int rightDist = Position.getDistanceFromTo(info.getPositionInRoomIndex(), pos.getIndex(),
					true);
			int leftDist = Position.getDistanceFromTo(info.getPositionInRoomIndex(), pos.getIndex(),
					false);
			if (rightDist > leftDist) {
				// TODO: check if is blocked
				return new StepAction(info.getPos().getLastIndex());
			} else {
				// TODO: check if is blocked
				return new StepAction(info.getPos().getNextIndex());
			}
		}
		return defaultFightAI.chooseFightAction();
	}

	@Override
	public Action chooseMovementAction() {
		if(info.getRoomNumber().equals(pos.getLocation())) {
			if(info.getPositionInRoomIndex() == pos.getIndex()) {
				// we are in position, hence nothing to do
				return new DoNothingAction();

			} else {
				// we step to our guard position
				return new StepAction(pos.getIndex());
			}
		} else {
			// we make our way towards the guarding position
			return new MoveAction(new RouteInstruction(pos.getRoom()).getWay(info.getRoomNumber()));
		}
	}
}
