package dungeon.generate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Room;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class RectArea implements Area {

	private JDPoint leftUpperCornerPosition;
	private int sizeX;
	private int sizeY;
	private Dungeon dungeon;

	public RectArea(JDPoint leftUpperCornerPosition, int sizeX, int sizeY, Dungeon dungeon) {
		this.leftUpperCornerPosition = leftUpperCornerPosition;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.dungeon = dungeon;
	}

	@Override
	public Collection<Room> getRooms() {
		Set<Room> rooms = new HashSet<Room>();
		for (int x = leftUpperCornerPosition.getX(); x < leftUpperCornerPosition.getX() + sizeX; x++) {
			for (int y = leftUpperCornerPosition.getY(); y < leftUpperCornerPosition.getY() + sizeY; y++) {
				rooms.add(dungeon.getRoomNr(x ,y));
			}
		}
		return rooms;
	}

	@Override
	public JDPoint getPosition() {
		return leftUpperCornerPosition;
	}
}
