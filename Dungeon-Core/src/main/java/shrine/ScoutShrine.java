package shrine;

import java.beans.Visibility;
import java.util.ArrayList;
import java.util.List;

import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomEntity;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.action.ScoutAction;
import figure.action.ScoutResult;
import util.JDColor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 05.01.19.
 */
public class ScoutShrine extends Shrine {

	private final List<Room> roomsList;

	public ScoutShrine(Room room) {
		super(room);
		this.roomsList = new ArrayList<>();
		final JDPoint point = room.getPoint();
		for(int x = -1; x < 2; x++) {
			for(int y = -1; y < 2; y++) {
				JDPoint other = new JDPoint(point.getX()+x, point.getY()+y);
				final Room otherRoom = room.getDungeon().getRoom(other);
				if(otherRoom != null && !otherRoom.isWall()) {
					roomsList.add(otherRoom);
				}
			}
		}
	}

	public ScoutShrine(List<Room> roomsList) {
		this.roomsList = roomsList;
	}

	@Override
	public int getShrineIndex() {
		// deprecated
		return -1;
	}

	@Override
	public void turn(int round) {
		// do nothing
	}

	@Override
	public JDColor getColor() {
		// deprecated
		return null;
	}

	@Override
	public String getStory() {
		return "Macht die Umgebung offenbar.";
	}

	@Override
	public String toString() {
		return getText();
	}

	@Override
	public String getText() {
		return "Aussichtsturm";
	}

	@Override
	public String getStatus() {
		return "";
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta) {

		ScoutResult result = new ScoutResult(f, RoomObservationStatus.VISIBILITY_ITEMS);
		for (Room room : roomsList) {
			f.getRoomVisibility().addVisibilityModifier(room.getNumber(), result);
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
