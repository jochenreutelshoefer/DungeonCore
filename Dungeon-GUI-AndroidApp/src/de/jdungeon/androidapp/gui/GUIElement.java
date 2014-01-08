package de.jdungeon.androidapp.gui;

import util.JDDimension;
import android.view.MotionEvent;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.ScrollMotion;
import dungeon.JDPoint;

public interface GUIElement {

	boolean isVisible();

	JDPoint getPositionOnScreen();

	JDDimension getDimension();
	
	boolean hasPoint(JDPoint p);
	
	void handleTouchEvent(TouchEvent touch);

	void handleDoubleTapEvent(MotionEvent touch);

	void handleLongPressEvent(MotionEvent touch);

	void handleScrollEvent(ScrollMotion scrolling);

	void paint(Graphics g, JDPoint viewportPosition);

	void update(float time);
	
	GameScreen getScreen();

}
