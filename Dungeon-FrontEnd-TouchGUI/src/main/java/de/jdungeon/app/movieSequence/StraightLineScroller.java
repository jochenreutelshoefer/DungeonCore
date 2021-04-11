package de.jdungeon.app.movieSequence;

import de.jdungeon.util.Pair;

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
		diffX = targetPos.getA() - startPos.getA();
		diffY = targetPos.getB() - startPos.getB();
	}


	@Override
	public Pair<Float, Float> getTargetPosition() {
		return targetPos;
	}

	@Override
	public Pair<Float, Float> getViewportPosition(float timePassed) {
		timeCounter += timePassed;

		if (timeCounter > duration)
			return targetPos;

		return new Pair<Float, Float>(startPos.getA()
				+ CalculationUtils.getRatio(diffX, timeCounter, duration),
				startPos.getB()
						+ CalculationUtils.getRatio(diffY, timeCounter,
								duration));
	}

}
