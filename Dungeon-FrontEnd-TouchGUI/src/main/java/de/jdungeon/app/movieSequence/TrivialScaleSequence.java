package de.jdungeon.app.movieSequence;

public class TrivialScaleSequence implements ChangeScaleSequence {

	private final float scale;

	public TrivialScaleSequence(float scale) {
		this.scale = scale;
	}

	@Override
	public float getScale(float timePassed) {
		return scale;
	}

}
