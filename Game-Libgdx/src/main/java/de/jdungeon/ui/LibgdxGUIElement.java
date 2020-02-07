package de.jdungeon.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.game.Game;
import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.ScrollMotion;

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

	void paint(SpriteBatch batch);

	void update(float time);

	Screen getScreen();

	Game getGame();
}
