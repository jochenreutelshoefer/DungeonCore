package de.jdungeon.implementation;

import android.view.MotionEvent;

import de.jdungeon.game.ScrollMotion;

public class AndroidScrollMotion implements ScrollMotion {

	private final MotionEvent startEvent;
	private final FloatDimension movement;

	public AndroidScrollMotion(MotionEvent startEvent, FloatDimension movement) {
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
