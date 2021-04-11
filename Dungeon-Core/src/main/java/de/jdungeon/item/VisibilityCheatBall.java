package de.jdungeon.item;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.VisibilityModifier;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.item.interfaces.Usable;
import de.jdungeon.item.quest.Thing;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */

public class VisibilityCheatBall extends Thing implements Usable, VisibilityModifier {

	private Figure user = null;

	public VisibilityCheatBall() {
		super("Omniscience", null);
	}

	@Override
	public String getText() {
		return "Macht alles sichtbar";
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		Dungeon dungeon = f.getActualDungeon();
		user = f;
		for (int x = 0; x < dungeon.getSize().getX(); x++) {
			for (int y = 0; y < dungeon.getSize().getY(); y++) {
				JDPoint point = new JDPoint(x, y);
				Room toView = dungeon.getRoom(point);
				if (toView != null) {
					f.getRoomVisibility().addVisibilityModifier(toView.getRoomNumber(), this);
					// TODO: implement that the vis modifier will be removed if the de.jdungeon.item is dropped
				}
			}
		}
		return ActionResult.DONE;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return true;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public int getVisibilityStatus() {
		return RoomObservationStatus.VISIBILITY_ITEMS;
	}

	@Override
	public boolean stillValid() {
		// the de.jdungeon.user needs to posses the de.jdungeon.item, otherwise it will not be active anymore
		if(this.getOwner().equals(user)) {
			return true;
		}
		return false;
	}
}
