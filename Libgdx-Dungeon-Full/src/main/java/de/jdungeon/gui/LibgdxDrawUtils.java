package de.jdungeon.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class LibgdxDrawUtils {

	public static void drawRectangle(ShapeRenderer renderer, Color color, JDPoint point, JDDimension dimension) {
		renderer.set(ShapeRenderer.ShapeType.Line);
		renderer.setColor(color);
		renderer.rect(point.getX(), point.getY(), dimension.getWidth(), dimension.getHeight());
	}

	public static void fillRectangle(ShapeRenderer renderer,  Color color, JDPoint point, JDDimension dimension) {
		renderer.set(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(point.getX(), point.getY(), dimension.getWidth(), dimension.getHeight());
	}

	public static void drawRectangle(ShapeRenderer renderer, Color color, int x1, int y1, int x2, int y2) {
		renderer.set(ShapeRenderer.ShapeType.Line);
		renderer.setColor(color);
		renderer.line(x1, y1, x2, y1);
		renderer.line(x1, y1, x1, y2);
		renderer.line(x1, y2, x2, y2);
		renderer.line(x2, y1, x2, y2);
	}
}
