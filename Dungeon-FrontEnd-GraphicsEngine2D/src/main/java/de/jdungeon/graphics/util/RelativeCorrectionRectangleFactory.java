package de.jdungeon.graphics.util;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.06.17.
 */
public class RelativeCorrectionRectangleFactory implements RectangleFactory {

	private final int roomOffsetX;
	private final int roomOffsetY;

	public RelativeCorrectionRectangleFactory(int roomOffsetX, int roomOffsetY) {
		this.roomOffsetX = roomOffsetX;
		this.roomOffsetY = roomOffsetY;
	}

	@Override
	public DrawingRectangle create(int xAbsolute, int yAbsolute, int width, int height) {
		return new RelativeRectangle(xAbsolute - roomOffsetX, yAbsolute - roomOffsetY, width, height);
	}

	@Override
	public DrawingRectangle create(JDPoint pointAbsolute, int width, int height) {
		return create(pointAbsolute.getX(), pointAbsolute.getY(), width, height);
	}

	@Override
	public DrawingRectangle create(JDPoint pointAbsolute, JDDimension d) {
		return create(pointAbsolute.getX(), pointAbsolute.getY(), d.getWidth(), d.getHeight());
	}
}
