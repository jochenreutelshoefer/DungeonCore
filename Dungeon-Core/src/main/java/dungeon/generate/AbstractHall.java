package dungeon.generate;

import dungeon.Dungeon;
import dungeon.JDPoint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class AbstractHall implements Hall {

	protected final JDPoint startPoint;
	protected Dungeon dungeon;


	public AbstractHall(JDPoint p, Dungeon dungeon) {
		this.startPoint = p;
		this.dungeon = dungeon;
	}

	public boolean validateNet() {
		return DungeonFillUtils.validateNet(dungeon.getAllRooms(), dungeon.getRoom(startPoint));
	}

}
