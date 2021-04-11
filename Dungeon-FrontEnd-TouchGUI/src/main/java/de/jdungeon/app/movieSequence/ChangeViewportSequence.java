package de.jdungeon.app.movieSequence;

import de.jdungeon.util.Pair;

public interface ChangeViewportSequence {

	Pair<Float, Float> getViewportPosition(float timePassed);

	/**
	 * The position where the camera flight sequence will end.
	 * This can be used to compute which subsequent camera flight are needed.
	 *
	 * @return target position after the end of the sequence
	 */
	Pair<Float, Float> getTargetPosition();

}
