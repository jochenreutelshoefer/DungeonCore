package de.jdungeon.app.gui;

import de.jdungeon.util.JDDimension;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.ScrollMotion;

import de.jdungeon.dungeon.JDPoint;

public interface GUIElement {

	boolean isVisible();

	boolean needsRepaint();

	JDPoint getPositionOnScreen();

	JDDimension getDimension();
	
	boolean hasPoint(JDPoint p);
	
	boolean handleTouchEvent(TouchEvent touch);

	void handleDoubleTapEvent(MotionEvent touch);

	void handleLongPressEvent(MotionEvent touch);

	boolean handleScrollEvent(ScrollMotion scrolling);

	void paint(Graphics g, JDPoint viewportPosition);

	void update(float time);
	
	StandardScreen getScreen();

	Game getGame();

}
