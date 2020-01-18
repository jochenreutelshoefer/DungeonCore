package de.jdungeon.world;

import de.jdungeon.CameraHelper;
import de.jdungeon.app.movieSequence.CameraFlightSequence;
import de.jdungeon.app.movieSequence.CameraFlightSequenceManager;
import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 18.01.20.
 */
public class LibgdxCameraFlightSequenceManager extends CameraFlightSequenceManager {

	public static int SCALE_COMPATIBILITY_FACTOR = 100;

	private final CameraHelper cameraHelper;

	public LibgdxCameraFlightSequenceManager(CameraHelper cameraHelper) {
		this.cameraHelper = cameraHelper;
	}

	public void update(float deltaTime) {
		CameraFlightSequence currentSequence = getCurrentSequence(deltaTime);
		if(currentSequence != null) {
			int scale = currentSequence.getScale(deltaTime);
			cameraHelper.setZoom(((float)scale)/SCALE_COMPATIBILITY_FACTOR);

			Pair<Float, Float> viewportPosition = currentSequence.getViewportPosition(deltaTime);
			cameraHelper.setPosition(viewportPosition.getA(), viewportPosition.getB());
		}
	}
}
