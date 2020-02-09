package item;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.VisibilityModifier;
import item.interfaces.Usable;
import item.quest.Thing;

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
	public boolean use(Figure f, RoomEntity target, boolean meta) {
		Dungeon dungeon = f.getActualDungeon();
		user = f;
		for (int x = 0; x < dungeon.getSize().getX(); x++) {
			for (int y = 0; y < dungeon.getSize().getY(); y++) {
				JDPoint point = new JDPoint(x, y);
				Room toView = dungeon.getRoom(point);
				if (toView != null) {
					f.getRoomVisibility().addVisibilityModifier(toView.getLocation(), this);
					// TODO: implement that the vis modifier will be removed if the item is dropped
				}
			}
		}
		return true;
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
		// the user needs to posses the item, otherwise it will not be active anymore
		if(this.getOwner().equals(user)) {
			return true;
		}
		return false;
	}
}
