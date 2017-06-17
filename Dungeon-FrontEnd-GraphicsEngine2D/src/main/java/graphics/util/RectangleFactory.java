package graphics.util;

import dungeon.JDPoint;
import util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.06.17.
 */
public interface RectangleFactory {

	DrawingRectangle create(int x, int y, int width, int height) ;

	DrawingRectangle create(JDPoint p, int width, int height) ;

	DrawingRectangle create(JDPoint p, JDDimension d) ;
}
