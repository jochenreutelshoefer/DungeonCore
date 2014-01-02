package de.jdungeon.androidapp.gui;

import util.JDDimension;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.FloatDimension;
import dungeon.JDPoint;

public interface GUIElement {

	boolean isVisible();

	JDPoint getPositionOnScreen();

	JDDimension getDimension();
	
	void handleTouchEvent(TouchEvent touch);

	void handleScrollEvent(FloatDimension scrolling);

	void paint(Graphics g, JDPoint viewportPosition);

	void update(float time);

}
