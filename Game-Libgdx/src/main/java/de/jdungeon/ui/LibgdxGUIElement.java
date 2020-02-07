package de.jdungeon.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.game.Game;

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

	Game getGame();

	boolean handleClickEvent(int screenX, int screenY);
}
