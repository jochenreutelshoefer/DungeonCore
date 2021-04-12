package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;

import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public class LibgdxTriangle implements LibgdxDrawable {

	private final JDPoint[] points;
	protected final LibgdxGUIElement parent;

	public LibgdxTriangle(JDPoint[] points, LibgdxGUIElement parent) {
		this.points = points;
		this.parent = parent;
	}

	@Override
	public void paint(ShapeRenderer renderer) {
		int parentX = parent.getPositionOnScreen().getX();
		int parentY = parent.getPositionOnScreen().getY();
		renderer.setColor(Color.WHITE);
		renderer.triangle(parentX + points[0].getX(),
				parentY + points[0].getY(),
				parentX + points[1].getX(),
				parentY + points[1].getY(),
				parentX + points[2].getX(),
				parentY + points[2].getY());
	}

	@Override
	public void paint(SpriteBatch batch) {

	}
}
