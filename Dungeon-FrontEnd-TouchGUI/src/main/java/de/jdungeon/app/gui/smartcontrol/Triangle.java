package de.jdungeon.app.gui.smartcontrol;

import dungeon.JDPoint;

import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public class Triangle implements Drawable {

	private final JDPoint[] points;
	protected final GUIElement parent;

	public Triangle(JDPoint[] points, GUIElement parent) {
		this.points = points;
		this.parent = parent;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		int parentX = parent.getPositionOnScreen().getX();
		int parentY = parent.getPositionOnScreen().getY();
		g.drawTriangle(parentX + points[0].getX(),
				parentY + points[0].getY(),
				parentX + points[1].getX(),
				parentY + points[1].getY(),
				parentX + points[2].getX(),
				parentY + points[2].getY(), Colors.WHITE);
	}
}
