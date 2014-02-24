package figure;

import figure.percept.Percept;
import game.JDGUI;
import ai.AI;

public class FigureControlWithSpectator extends FigureControl {

	private final JDGUI spectator;

	public FigureControlWithSpectator(FigureInfo info, AI ai, JDGUI spectator) {
		super(info, ai);
		this.spectator = spectator;
	}

	@Override
	public void tellPercept(Percept p) {
		spectator.tellPercept(p);
		super.tellPercept(p);
	}

}
