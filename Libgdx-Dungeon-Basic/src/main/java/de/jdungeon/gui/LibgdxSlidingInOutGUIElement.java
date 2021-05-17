package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.02.20.
 */
public abstract class LibgdxSlidingInOutGUIElement extends AbstractLibgdxGUIElement {

	private int totalSlidingRange;

	enum SlideState {
		IN_POSITION,
		OUT_POSITION,
		SLIDING_IN,
		SLIDING_OUT
	}

	private static final float SLIDE_IN_DURATION = 0.1f;
	private static final float SLIDE_OUT_DURATION = 0.5f;
	private float timer = 0;

	private JDPoint targetOutPos;
	private JDPoint targetInPos;

	private SlideState currentState;

	public LibgdxSlidingInOutGUIElement(JDPoint position, JDDimension dimension,
										JDPoint targetPos) {
		super(position, dimension);
		init(position, targetPos);
	}

	private void init(JDPoint position, JDPoint targetPos) {
		this.targetOutPos = targetPos;
		this.targetInPos = position;
		totalSlidingRange = targetInPos.getX() - targetOutPos.getX();

		currentState = SlideState.IN_POSITION;
		this.slideOut();
	}

	public LibgdxSlidingInOutGUIElement(JDPoint position, JDDimension dimension,
										JDPoint targetPos, String backgroundName) {
		super(position, dimension, backgroundName);
		init(position, targetPos);
	}

	@Override
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		if (dx < 0 && currentState == SlideState.IN_POSITION) {
			slideOut();
			return true;
		}
		if (dx > 0 && currentState == SlideState.OUT_POSITION) {
			slideIn();
			return true;
		}
		return false;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {

		/*
		 * if it is out of screen a touch on the visible border will open it
		 */
		slideIn();

		return true;
	}

	protected void slideIn() {
		if (currentState != SlideState.SLIDING_IN && currentState != SlideState.IN_POSITION) {
			currentState = SlideState.SLIDING_IN;
			timer = 0;
		}
	}

	public void slideOut() {
		if (currentState != SlideState.SLIDING_OUT && currentState != SlideState.OUT_POSITION) {
			currentState = SlideState.SLIDING_OUT;
			timer = 0;
		}
	}

	protected int getCurrentX() {

		if (currentState == SlideState.SLIDING_OUT) {
			float percentageDone = timer / SLIDE_OUT_DURATION;
			return targetInPos.getX() - (int) (percentageDone * totalSlidingRange);
		}
		if (currentState == SlideState.SLIDING_IN) {
			float percentageDone = timer / SLIDE_IN_DURATION;
			return targetOutPos.getX() + (int) (percentageDone * totalSlidingRange);
		}
		return position.getX();
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
	public void paint(SpriteBatch batch, float deltaTime) {
		animationUpdate(deltaTime);
	}

	@Override
	public void update(float deltaTime, int round) {
		//animationUpdate(time);
	}

	protected void animationUpdate(float time) {
		timer += time;

		// we're done sliding
		if (currentState == SlideState.SLIDING_OUT) {
			if (timer > SLIDE_OUT_DURATION) {
				timer = 0;
				currentState = SlideState.OUT_POSITION;
				this.position = targetOutPos;
			}
		}
		if (currentState == SlideState.SLIDING_IN) {
			if (timer > SLIDE_IN_DURATION) {
				timer = 0;
				currentState = SlideState.IN_POSITION;
				this.position = targetInPos;
			}
		}
	}
}
