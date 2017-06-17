package graphics.util;

import dungeon.JDPoint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.06.17.
 */
public class RelativeRectangle implements DrawingRectangle {

	private final int width;
	private final int height;
	private final int offsetInRoomX;
	private final int offsetInRoomY;

	public RelativeRectangle(int width, int height, int offsetInRoomX, int offsetInRoomY) {
		this.width = width;
		this.height = height;
		this.offsetInRoomX = offsetInRoomX;
		this.offsetInRoomY = offsetInRoomY;
	}

	@Override
	public boolean containsPoint(JDPoint p, int roomOffsetX, int roomOffsetY) {
			return p.getX() >= roomOffsetX && p.getX() <= roomOffsetX + width && p.getY() >= roomOffsetY
					&& p.getY() <= roomOffsetY + height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getX(int roomOffsetX) {
		return offsetInRoomX + roomOffsetX;
	}

	@Override
	public int getY(int roomOffsetY) {
		return offsetInRoomY + roomOffsetY;
	}
}
