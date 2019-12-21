package de.jdungeon.libgdx.adapter;

import android.view.MotionEvent;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.16.
 */
public class LibgdxMotionEvent implements de.jdungeon.game.MotionEvent{

	private final MotionEvent event;

	public LibgdxMotionEvent(MotionEvent event) {
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
