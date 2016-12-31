package de.jdungeon.util;

import android.view.MotionEvent;

import de.jdungeon.implementation.AndroidMotionEvent;

public class ScrollMotion {

	private final MotionEvent startEvent;
	private final FloatDimension movement;

	public ScrollMotion(MotionEvent startEvent, FloatDimension movement) {
		super();
		this.startEvent = startEvent;
		this.movement = movement;
	}

	public de.jdungeon.game.MotionEvent getStartEvent() {
		return new AndroidMotionEvent(startEvent);
	}

	public FloatDimension getMovement() {
		return movement;
	}

}
