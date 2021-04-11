package de.jdungeon.app.movieSequence;


/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class ZoomSequence implements ChangeScaleSequence {

	private final float startScale;
	private final float targetScale;
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
	public float getScale(float timePassed) {
		timeCounter += timePassed;

		if (timeCounter > duration)
			return targetScale;

		return (startScale + CalculationUtils.getRatio(scaleDiff, timeCounter, duration));
	}
}
