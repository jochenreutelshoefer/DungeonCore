package shrine;

import dungeon.JDPoint;
import dungeon.RoomEntity;
import figure.Figure;
import figure.RoomObservationStatus;
import figure.percept.TextPercept;
import item.quest.MoonRune;
import util.JDColor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class MoonRuneFinderShrine extends Location {

	private static int COST = 3;
	private final MoonRune rune;

	public MoonRuneFinderShrine(MoonRune rune) {
		this.rune = rune;
	}

	@Override
	public int getShrineIndex() {
		return 0;
	}

	@Override
	public void turn(int round) {

	}

	@Override
	public JDColor getColor() {
		return null;
	}

	@Override
	public String getStory() {
		return "Ein alter Druide.";
	}

	@Override
	public String toString() {
		return "Alter Druide";
	}

	@Override
	public String getText() {
		return "Alter Druide";
	}

	@Override
	public String getStatus() {
		return "Status Druide";
	}

	@Override
	public int dustCosts() {
		return COST;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta, int round) {
		if(!f.canPayDust(COST)) return false;

		f.payDust(COST);

		JDPoint location = rune.getLocation();
		tellDirection(location, f, round);

		return true;
	}

	private void tellDirection(JDPoint location, Figure f, int round) {
		// TODO: factor out text
		f.tellPercept(new TextPercept("Die Mondrune befindet sich im Moment bei" + ": " + location, round));
		f.getRoomVisibility().setVisibilityStatus(location, RoomObservationStatus.VISIBILITY_ITEMS);
	}

	@Override
	public boolean usableOnce() {
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f.canPayDust(COST);
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
}
