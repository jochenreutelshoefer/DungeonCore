package de.jdungeon.app;

import dungeon.RoomInfo;
import figure.FigureInfo;
import game.InfoEntity;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import graphics.util.DrawingRectangle;

import java.util.List;

import animation.AnimationFrame;
import animation.AnimationManager;

import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import gui.Paragraphable;
import util.JDDimension;

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
