package de.jdungeon.app.movieSequence;

import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.04.16.
 */
public class NonScrollerSequence implements ChangeViewportSequence {

	private final Pair<Float, Float> viewportPosition;

	public NonScrollerSequence(Pair<Float, Float> viewportPosition) {
		this.viewportPosition = viewportPosition;
	}

	public Pair<Float, Float> getViewportPosition(float timePassed) {
		return viewportPosition;
	}
}
