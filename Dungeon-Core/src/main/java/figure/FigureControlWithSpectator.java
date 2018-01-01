package figure;

import figure.percept.Percept;
import game.JDGUI;
import ai.AbstractAI;

public class FigureControlWithSpectator extends FigureControl {

	private final JDGUI spectator;

	public FigureControlWithSpectator(FigureInfo info, AbstractAI ai, JDGUI spectator) {
		super(info, ai);
		this.spectator = spectator;
	}

	@Override
	public void tellPercept(Percept p) {
		// Log.info("telling percept: " + p.toString());
		spectator.tellPercept(p);
		super.tellPercept(p);
	}

}
