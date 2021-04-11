package de.jdungeon.graphics.util;

import de.jdungeon.dungeon.JDPoint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.06.17.
 */
public interface DrawingRectangle {

	boolean containsPoint(JDPoint p, int roomOffsetX, int roomOffsetY);

	int getWidth();

	int getHeight();

	int getX(int roomOffsetX);

	int getY(int roomOffsetY);

}
