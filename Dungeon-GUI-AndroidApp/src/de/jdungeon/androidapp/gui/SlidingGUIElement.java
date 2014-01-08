package de.jdungeon.androidapp.gui;

import util.JDDimension;
import de.jdungeon.androidapp.GameScreen;
import dungeon.JDPoint;

public abstract class SlidingGUIElement extends AbstractGUIElement {

	protected final static int SLIDE_OUT_STEPS = 20;
	protected int slideStep = -1;
	protected final JDPoint targetPos;

	public SlidingGUIElement(JDPoint position, JDDimension dimension,
			JDPoint targetPos, GameScreen screen) {
		super(position, dimension, screen);
		this.targetPos = targetPos;
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
