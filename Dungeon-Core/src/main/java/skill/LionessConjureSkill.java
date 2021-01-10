package skill;

import dungeon.Position;
import dungeon.Room;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.other.Lioness;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public class LionessConjureSkill extends SimpleSkill {

	public LionessConjureSkill() {
		super(8);
	}

	@Override
	protected boolean isPossibleFight() {
		return false;
	}

	@Override
	protected boolean isPossibleNonFight() {
		return true;
	}

	@Override
	public ActionResult doExecute(SimpleSkillAction action, boolean doIt, int round) {
		Figure mage = action.getActor();

		// check if there is a free position in the room
		Room roomInfo = mage.getRoomInfo();
		Position[] positions = roomInfo.getPositions();
		boolean freePositionAvailable = false;
		for (Position position : positions) {
			if (position.getFigure() == null) {
				freePositionAvailable = true;
				break;
			}
		}
		if (!freePositionAvailable) {
			return ActionResult.POSITION;
		}

		if (doIt) {
			// insert lioness into room
			Lioness lioness = Lioness.createLioness(10000, mage.getRoom().getDungeon(),
					FigureInfo.makeFigureInfo(mage, mage.getRoomVisibility()));
			mage.getActualDungeon().insertFigure(lioness);
			Room room = mage.getRoom();
			int targetPosition = Position.getFreePositionNear(mage.getRoom(), mage.getPositionInRoom());
			room.figureEntersAtPosition(lioness, targetPosition, round);
			return ActionResult.DONE;
		}

		return ActionResult.POSSIBLE;
	}
}
