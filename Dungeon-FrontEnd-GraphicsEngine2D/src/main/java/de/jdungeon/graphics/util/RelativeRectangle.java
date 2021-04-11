package de.jdungeon.graphics.util;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.06.17.
 */
public class RelativeRectangle implements DrawingRectangle {

	private final int width;
	private final int height;
	private final int offsetInRoomX;
	private final int offsetInRoomY;

	public RelativeRectangle(int offsetInRoomX, int offsetInRoomY, int width, int height) {
		this.width = width;
		this.height = height;
		this.offsetInRoomX = offsetInRoomX;
		this.offsetInRoomY = offsetInRoomY;
	}

	public RelativeRectangle(JDPoint pointRelativeInRoom, int width, int height) {
		this.offsetInRoomX = pointRelativeInRoom.getX();
		this.offsetInRoomY = pointRelativeInRoom.getY();
		this.width = width;
		this.height = height;
	}

	public RelativeRectangle(JDPoint pointRelativeInRoom, JDDimension dimension) {
		this.offsetInRoomX = pointRelativeInRoom.getX();
		this.offsetInRoomY = pointRelativeInRoom.getY();
		this.width = dimension.getWidth();
		this.height = dimension.getHeight();
	}

	@Override
	public boolean containsPoint(JDPoint p, int roomOffsetX, int roomOffsetY) {
			return p.getX() >= roomOffsetX + offsetInRoomX
					&& p.getX() <= roomOffsetX + offsetInRoomX + width
					&& p.getY() >= roomOffsetY + offsetInRoomY
					&& p.getY() <= roomOffsetY + offsetInRoomY + height;
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
