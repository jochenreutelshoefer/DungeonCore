package de.jdungeon.world;

import de.jdungeon.app.movieSequence.CameraFlightSequence;
import de.jdungeon.app.movieSequence.CameraFlightSequenceManager;
import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 18.01.20.
 */
public class LibgdxCameraFlightSequenceManager extends CameraFlightSequenceManager {

	/*
	Todo: refactor CameraFlightSequenceManager in a way that only floats are  used to ged rid of this compatibility factor
	 */

	private final CameraHelper cameraHelper;

	public LibgdxCameraFlightSequenceManager(CameraHelper cameraHelper) {
		this.cameraHelper = cameraHelper;
	}

	public void update(float deltaTime) {
		CameraFlightSequence currentSequence = getCurrentSequence(deltaTime);
		if(currentSequence != null) {
			cameraHelper.setZoom(currentSequence.getScale(deltaTime));

			Pair<Float, Float> viewportPosition = currentSequence.getViewportPosition(deltaTime);
			cameraHelper.setPosition(viewportPosition.getA(), viewportPosition.getB());
		}
	}
}
