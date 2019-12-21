package de.jdungeon.libgdx.adapter;

import android.view.MotionEvent;

import de.jdungeon.game.ScrollMotion;

public class LibgdxScrollMotion implements ScrollMotion {

	private final MotionEvent startEvent;
	private final FloatDimension movement;

	public LibgdxScrollMotion(MotionEvent startEvent, FloatDimension movement) {
		super();
		this.startEvent = startEvent;
		this.movement = movement;
	}

	public de.jdungeon.game.MotionEvent getStartEvent() {
		return new LibgdxMotionEvent(startEvent);
	}

	public FloatDimension getMovement() {
		return movement;
	}

}
