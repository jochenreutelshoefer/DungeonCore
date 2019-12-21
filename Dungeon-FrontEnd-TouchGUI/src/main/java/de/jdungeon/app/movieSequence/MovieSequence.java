package de.jdungeon.app.movieSequence;


public interface MovieSequence extends ChangeScaleSequence,
		ChangeViewportSequence {

	boolean isFinished(float timePassed);

}
