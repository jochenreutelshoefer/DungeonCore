package figure.action;

import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.Position;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.percept.InfoPercept;
import spell.KeyLocator;

public class LockAction extends AbstractExecutableAction {

	private final DoorInfo door;
	private final Figure figure;

	public LockAction(FigureInfo info, DoorInfo door) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		this.door = door;
	}

	public DoorInfo getDoor() {
		return door;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		DoorInfo info = getDoor();
		RouteInstruction.Direction direction = info.getDirection(figure.getRoom().getLocation());
		if (direction == null) {
			return ActionResult.WRONG_TARGET;
		}
		Door d = figure.getRoom().getDoor(direction);
		if (figure.isAbleToLockDoor()) {
			Position positionAtDoor = d.getPositionAtDoor(figure.getRoom(), false);
			if (!figure.getPos().equals(positionAtDoor)) {
				return ActionResult.POSITION;
			}

			boolean wasLocked = d.getLocked();
			boolean ok = figure.tryUnlockDoor(d, doIt);

			if (ok) {

				if (wasLocked) {
					figure.tellPercept(new InfoPercept(InfoPercept.UNLOCKED_DOOR, round));
				}
				else {
					figure.tellPercept(new InfoPercept(InfoPercept.LOCKED_DOOR, round));
				}
				if (!figure.canPayActionPoints(1)) {
					return ActionResult.NOAP;
				}
				if (doIt) {
					figure.payActionPoint(this, round);
					return ActionResult.DONE;
				}
				return ActionResult.POSSIBLE;
			}
			else if (info.isKeylocatable(figure)) {
				if (!figure.canPayActionPoints(1)) {
					return ActionResult.NOAP;
				}
				if (doIt) {
					KeyLocator.tellKeyLocation(figure, d, round);
					figure.payActionPoint(this, round);
					return ActionResult.DONE;
				}
				return ActionResult.POSSIBLE;
			}
			return ActionResult.ITEM;
		}

		return ActionResult.OTHER;
	}
}
