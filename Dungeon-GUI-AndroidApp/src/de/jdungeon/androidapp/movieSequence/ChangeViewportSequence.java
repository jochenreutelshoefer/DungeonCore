package de.jdungeon.androidapp.movieSequence;

import android.util.Pair;

public interface ChangeViewportSequence {

	Pair<Float, Float> getViewportPosition(float timePassed);

}
