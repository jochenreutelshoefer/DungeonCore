package location.defender;

import dungeon.RoomEntity;
import figure.Figure;
import location.Location;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.04.20.
 */
public class DefenderLocation extends Location {

	@Override
	public void turn(int round) {

	}

	@Override
	public String getStory() {
		return null;
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		return false;
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return false;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
}
