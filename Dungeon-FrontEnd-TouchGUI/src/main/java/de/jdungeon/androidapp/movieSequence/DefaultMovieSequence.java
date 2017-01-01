package de.jdungeon.androidapp.movieSequence;

import de.jdungeon.util.Pair;

public class DefaultMovieSequence implements MovieSequence {

	private final ChangeScaleSequence scaler;
	private final ChangeViewportSequence scroller;
	private final float duration;
	private float timeCounter = 0;

	public DefaultMovieSequence(ChangeScaleSequence scaler,
			ChangeViewportSequence scroller, float duration) {
		this.scaler = scaler;
		this.scroller = scroller;
		this.duration = duration;
	}

	public int getScale(float timePassed) {
		return scaler.getScale(timePassed);
	}

	public Pair<Float, Float> getViewportPosition(float timePassed) {
		return scroller.getViewportPosition(timePassed);
	}

	public boolean isFinished(float timePassed) {
		timeCounter += timePassed;
		return timeCounter > duration;
	}

}
