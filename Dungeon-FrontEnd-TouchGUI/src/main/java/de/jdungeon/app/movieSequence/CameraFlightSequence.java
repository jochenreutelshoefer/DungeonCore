package de.jdungeon.app.movieSequence;


public interface CameraFlightSequence extends ChangeScaleSequence,
		ChangeViewportSequence {

	boolean isFinished(float timePassed);

	String getTitle();

}
