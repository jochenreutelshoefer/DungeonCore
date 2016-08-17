package de.jdungeon.util;

import android.view.MotionEvent;

public class ScrollMotion {

	private final MotionEvent startEvent;
	private final FloatDimension movement;

	public ScrollMotion(MotionEvent startEvent, FloatDimension movement) {
		super();
		this.startEvent = startEvent;
		this.movement = movement;
	}

	public MotionEvent getStartEvent() {
		return startEvent;
	}

	public FloatDimension getMovement() {
		return movement;
	}

}
