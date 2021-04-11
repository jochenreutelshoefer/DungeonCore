package de.jdungeon.app.gui;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.ScrollMotion;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public abstract class DrawGUIElement implements GUIElement{

	@Override
	public boolean needsRepaint() {
		return true;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public JDPoint getPositionOnScreen() {
		return null;
	}

	@Override
	public JDDimension getDimension() {
		return null;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		return false;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		return false;
	}

	@Override
	public void handleDoubleTapEvent(MotionEvent touch) {

	}

	@Override
	public void handleLongPressEvent(MotionEvent touch) {

	}

	@Override
	public boolean handleScrollEvent(ScrollMotion scrolling) {
		return false;
	}

	@Override
	public void update(float time) {

	}

	@Override
	public StandardScreen getScreen() {
		return null;
	}
}
