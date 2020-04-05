package ai;

import dungeon.Position;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import figure.action.EndRoundAction;
import figure.action.MoveAction;
import figure.action.StepAction;
import figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.04.16.
 */
public class GuardPositionBehaviour extends AbstractMonsterBehaviour {

	private final Position pos;
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
				int lastIndex = info.getPos().getLastIndex();
				FigureInfo otherFigure = info.getRoomInfo().getPositionInRoom(lastIndex).getFigure();
				if(otherFigure != null) {
					// someone in the way
					if(otherFigure.isHostile(this.info)) {
						return new AttackAction(this.info, otherFigure.getFighterID());
					} else {
						return new StepAction(info, info.getPos().getNextIndex());
					}
				}
				return new StepAction(info, lastIndex);
			} else {
				int nextIndex = info.getPos().getNextIndex();
				FigureInfo otherFigure = info.getRoomInfo().getPositionInRoom(nextIndex).getFigure();
				if(otherFigure != null) {
					// someone in the way
					if(otherFigure.isHostile(this.info)) {
						return new AttackAction(this.info, otherFigure.getFighterID());
					} else {
						return new StepAction(info, info.getPos().getLastIndex());
					}
				}
				return new StepAction(info, nextIndex);
			}
		}
		return defaultFightAI.chooseFightAction();
	}

	@Override
	public Action chooseMovementAction() {
		if(info.getRoomNumber().equals(pos.getLocation())) {
			if(info.getPositionInRoomIndex() == pos.getIndex()) {
				// we are in position, hence nothing to do
				return new EndRoundAction();

			} else {
				// we step to our guard position
				return new StepAction(info, pos.getIndex());
			}
		} else {
			// we make our way towards the guarding position
			return new MoveAction(this.info, this.info.getRoomNumber(), new RouteInstruction(pos.getRoom().getNumber()).getWay(info.getRoomNumber(), this.info.getMap()));
		}
	}
}
