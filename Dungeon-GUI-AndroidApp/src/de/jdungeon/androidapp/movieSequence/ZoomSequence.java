package de.jdungeon.androidapp.movieSequence;

import android.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class ZoomSequence implements ChangeScaleSequence {

	private final float startScale;
	private float targetScale;
	private final float duration;
	private final float scaleDiff;
	private float timeCounter = 0;

	public ZoomSequence(float startScale, float targetScale, float duration) {
		this.startScale = startScale;
		this.targetScale = targetScale;
		this.duration = duration;
		this.scaleDiff = targetScale - startScale;
	}


	@Override
	public int getScale(float timePassed) {
		timeCounter += timePassed;

		if (timeCounter > duration)
			return (int) targetScale;

		return (int) (startScale
				+ CalculationUtils.getRatio(scaleDiff, timeCounter, duration));
	}
}
