package de.jdungeon.androidapp.movieSequence;

import de.jdungeon.util.Pair;

public interface ChangeViewportSequence {

	Pair<Float, Float> getViewportPosition(float timePassed);

}
