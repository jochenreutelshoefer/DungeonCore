package de.jdungeon.androidapp.movieSequence;

public class TrivialScaleSequence implements ChangeScaleSequence {

	private final int scale;

	public TrivialScaleSequence(int scale) {
		this.scale = scale;
	}

	@Override
	public int getScale(float timePassed) {
		return scale;
	}

}
