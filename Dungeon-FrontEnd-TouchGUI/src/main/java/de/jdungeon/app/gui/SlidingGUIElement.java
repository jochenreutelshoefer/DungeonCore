package de.jdungeon.app.gui;

import util.JDDimension;
import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.ScrollMotion;

import dungeon.JDPoint;

public abstract class SlidingGUIElement extends AbstractGUIElement {

	protected final static int SLIDE_OUT_STEPS = 20;
	protected int slideStep = -1;
	protected final JDPoint targetPos;

	public SlidingGUIElement(JDPoint position, JDDimension dimension,
							 JDPoint targetPos, Game game) {
		super(position, dimension, game);
		this.targetPos = targetPos;

		this.slideOut();
	}

	protected void slideOut() {
		this.slideStep = SLIDE_OUT_STEPS;
	}

	@Override
	public boolean handleScrollEvent(ScrollMotion scrolling) {
		if (scrolling.getMovement().getX() > 0) {
			slideOut();
			return true;
		}
		return false;
	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {

		/*
		 * if it is out of screen a touch on the visible border will open it
		 */
		if (getCurrentX() == targetPos.getX()) {
			this.slideStep = -1;
		}
		return true;
	}

	protected void slideIn() {
		this.slideStep = -1;
	}

	protected int getCurrentX() {
		int x = position.getX();
		int diffX = targetPos.getX() - position.getX();
		if (slideStep >= 0) {
			x = x + ((SLIDE_OUT_STEPS - slideStep) * diffX / SLIDE_OUT_STEPS);
		}
		return x;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		return p.getX() >= this.getCurrentX()
				&& p.getX() <= this.getCurrentX() + dimension.getWidth()
				&& p.getY() >= position.getY()
				&& p.getY() <= position.getY() + dimension.getHeight();
	}

	@Override
	public void update(float time) {
		if (slideStep > 0) {
			slideStep -= 1;
		}
	}

}
