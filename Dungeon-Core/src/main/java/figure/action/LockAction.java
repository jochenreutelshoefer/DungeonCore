package figure.action;

import dungeon.Door;
import dungeon.DoorInfo;
import dungeon.Position;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import item.Item;
import item.Key;

import java.util.List;

public class LockAction extends AbstractExecutableAction {

    private final DoorInfo door;
    private final Figure figure;

    public LockAction(FigureInfo info, DoorInfo door) {
        figure = info.getMap().getDungeon().getFigureIndex().get(info.getFigureID());
        this.door = door;
    }

    public DoorInfo getDoor() {
        return door;
    }

    private ActionResult tryToggleDoorLockStateUsingKey(Door door, int round, boolean doIt) {
        List<Item> items = figure.getItems();
        for (int i = 0; i < items.size(); i++) {
            Item item = (items.get(i));
            if (item instanceof Key) {
                Key key = (Key) item;
                return key.use(figure, door, false, round, doIt);
            }
        }
        return ActionResult.ITEM;
    }

    @Override
    public ActionResult handle(boolean doIt, int round) {
        DoorInfo info = getDoor();
        RouteInstruction.Direction direction = info.getDirection(figure.getRoom().getRoomNumber());
        if (direction == null) {
            return ActionResult.WRONG_TARGET;
        }
        Door d = figure.getRoom().getDoor(direction);
        Position positionAtDoor = d.getPositionAtDoor(figure.getRoom(), false);
        if (!figure.getPos().equals(positionAtDoor)) {
            return ActionResult.POSITION;
        }
        if (!figure.canPayActionPoints(1)) {
            return ActionResult.NOAP;
        }
        boolean wasLocked = d.getLocked();
        ActionResult ok = tryToggleDoorLockStateUsingKey(d, round, doIt);

        if (ok == ActionResult.DONE) {
            figure.payActionPoint(this, round);
            return ActionResult.DONE;
        }
        return ok;
        // todo: re-implement KeyLocator Spell
			/*
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
			*/
    }
}
