package de.jdungeon.androidapp.movieSequence;


public interface MovieSequence extends ChangeScaleSequence,
		ChangeViewportSequence {

	boolean isFinished(float timePassed);

}
