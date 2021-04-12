package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public interface LibgdxGUIElement {

	boolean isVisible();

	boolean needsRepaint();

	JDPoint getPositionOnScreen();

	JDDimension getDimension();

	boolean hasPoint(JDPoint p);

	void paint(ShapeRenderer shapeRenderer);

	void paint(SpriteBatch batch);

	void update(float time);

	boolean handleClickEvent(int screenX, int screenY);

	boolean handlePanEvent(float x, float y, float dx, float dy);
}