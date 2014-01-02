package de.jdungeon.androidapp.movieSequence;

import android.util.Pair;

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

	@Override
	public int getScale(float timePassed) {
		return scaler.getScale(timePassed);
	}

	@Override
	public Pair<Float, Float> getViewportPosition(float timePassed) {
		return scroller.getViewportPosition(timePassed);
	}

	@Override
	public boolean isFinished(float timePassed) {
		timeCounter += timePassed;
		return timeCounter > duration;
	}

}
