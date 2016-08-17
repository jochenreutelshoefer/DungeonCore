package de.jdungeon.androidapp.movieSequence;

import android.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class NonScrollerSequence implements ChangeViewportSequence {

	Pair<Float, Float> viewportPosition;

	public NonScrollerSequence(Pair<Float, Float> viewportPosition) {
		this.viewportPosition = viewportPosition;
	}

	@Override
	public Pair<Float, Float> getViewportPosition(float timePassed) {
		return viewportPosition;
	}
}
