package de.jdungeon.app;

import de.jdungeon.graphics.util.DrawingRectangle;

import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.gui.Paragraphable;
import de.jdungeon.util.JDDimension;

public class DrawUtils {

	private static final boolean useTileDrawingCache = false;

	private static void highlight(Graphics g, JDPoint viewportPosition, Object clickedObject, Paragraphable highlightedEntity, DrawingRectangle rectangle, int roomOffsetX, int roomOffsetY) {
		if (highlightedEntity != null) {
			if (clickedObject.equals(highlightedEntity)) {
				int x1 = rectangle.getX(roomOffsetX) - viewportPosition.getX();
				int y1 = rectangle.getY(roomOffsetY) - viewportPosition.getY();
				int x2 = x1 + rectangle.getWidth();
				int y2 = y1 + rectangle.getHeight();

				drawRectangle(g, Colors.YELLOW, x1, y1, x2, y2);
			}
		}
	}
	public static void drawRectangle(Graphics g, Color yellow, JDPoint point, JDDimension dimension) {
		g.drawRect(point.getX(), point.getY(), dimension.getWidth(), dimension.getHeight(), yellow);
	}

	public static void fillRectangle(Graphics g, Color yellow, JDPoint point, JDDimension dimension) {
		g.fillRect(point.getX(), point.getY(), dimension.getWidth(), dimension.getHeight(), yellow);
	}

	public static void drawRectangle(Graphics g, de.jdungeon.game.Color yellow, int x1, int y1, int x2, int y2) {
		// TODO: refactor
		g.drawLine(x1, y1, x2, y1, yellow);
		g.drawLine(x1, y1, x1, y2, yellow);
		g.drawLine(x1, y2, x2, y2, yellow);
		g.drawLine(x2, y1, x2, y2, yellow);
	}


}
