package de.jdungeon.app.movieSequence;

import de.jdungeon.util.Pair;

public interface CameraFlightSequence extends ChangeScaleSequence,
		ChangeViewportSequence {

	/**
	 * States whether the sequence if finished
	 *
	 * @param timePassed current time stamp
	 * @return true if finished
	 */
	boolean isFinished(float timePassed);

	/**
	 * Title of the sequence
	 *
	 * @return title
	 */
	String getTitle();

	/**
	 * The position where the camera flight sequence will end.
	 * This can be used to compute which subsequent camera flight are needed.
	 *
	 * @return target position after the end of the sequence
	 */
	Pair<Float, Float> getTargetPosition();

}
