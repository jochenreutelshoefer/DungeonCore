package item;

import java.util.List;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.percept.TextPercept;
import game.JDEnv;
import item.interfaces.Locatable;
import item.interfaces.Usable;
import item.quest.Thing;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class VisibilityCheatBall extends Thing implements Usable {

	public VisibilityCheatBall() {
		super("Omniscience", null);
	}

	@Override
	public String getText() {
		return "Macht alles sichtbar";
	}

	@Override
	public boolean use(Figure f, Object target, boolean meta) {
		Dungeon dungeon = f.getActualDungeon();

		for(int x = 0; x < dungeon.getSize().getX(); x++) {
			for (int y = 0; y < dungeon.getSize().getY(); y++) {
				Room toView = dungeon.getRoom(new JDPoint(x, y));
				f.getRoomObservationStatus(toView.getLocation()).setVisibilityStatus(RoomObservationStatus.VISIBILITY_ITEMS);
				f.addScoutedRoom(toView);
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
}
