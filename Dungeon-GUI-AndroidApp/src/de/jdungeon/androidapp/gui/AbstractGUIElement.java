package de.jdungeon.androidapp.gui;

import util.JDDimension;
import android.view.MotionEvent;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.ScrollMotion;
import dungeon.JDPoint;

public abstract class AbstractGUIElement implements GUIElement {

	protected final JDPoint position;
	protected final JDDimension dimension;
	protected GameScreen screen = null;

	public AbstractGUIElement(JDPoint position, JDDimension dimension,
			GameScreen screen) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.screen = screen;
	}

	public AbstractGUIElement(JDPoint position, JDDimension dimension) {
		super();
		this.position = position;
		this.dimension = dimension;
	}

	public AbstractGUIElement(JDPoint position, JDDimension dimension,
			GUIElement parent) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.screen = parent.getScreen();
	}

	@Override
	public GameScreen getScreen() {
		return screen;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		return p.getX() >= position.getX()
				&& p.getX() <= position.getX() + dimension.getWidth()
				&& p.getY() >= position.getY()
				&& p.getY() <= position.getY() + dimension.getHeight();
	}

	@Override
	public void handleDoubleTapEvent(MotionEvent touch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleLongPressEvent(MotionEvent touch) {
		// TODO Auto-generated method stub

	}

	@Override
	public JDPoint getPositionOnScreen() {
		return position;
	}

	@Override
	public JDDimension getDimension() {
		return dimension;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		// TODO Auto-generated method stub

	}


}
