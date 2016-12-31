package de.jdungeon.implementation;

import android.view.MotionEvent;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.16.
 */
public class AndroidMotionEvent implements de.jdungeon.game.MotionEvent{

	private final MotionEvent event;

	public AndroidMotionEvent(MotionEvent event) {
		this.event = event;
	}

	public MotionEvent getEvent() {
		return event;
	}

	@Override
	public float getRawX() {
		return event.getRawX();
	}

	@Override
	public float getRawY() {
		return event.getRawY();
	}
}
