package de.jdungeon.androidapp.movieSequence;

import android.util.Pair;

public class StraightLineScroller implements ChangeViewportSequence {

	private final Pair<Float, Float> startPos;
	private final Pair<Float, Float> targetPos;
	private final float duration;
	private final float diffX;
	private final float diffY;
	private float timeCounter = 0;

	public StraightLineScroller(Pair<Float, Float> startPos,
			Pair<Float, Float> targetPos, float duration) {
		this.startPos = startPos;
		this.targetPos = targetPos;
		this.duration = duration;
		diffX = targetPos.first - startPos.first;
		diffY = targetPos.second - startPos.second;
	}

	@Override
	public Pair<Float, Float> getViewportPosition(float timePassed) {
		timeCounter += timePassed;

		if (timeCounter > duration)
			return targetPos;

		return new Pair<Float, Float>(startPos.first
				+ CalculationUtils.getRatio(diffX, timeCounter, duration),
				startPos.second
						+ CalculationUtils.getRatio(diffY, timeCounter,
								duration));
	}

}
