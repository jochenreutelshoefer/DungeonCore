package figure;

import ai.AI;
import figure.hero.HeroInfo;
import game.JDGUI;

public class HeroControlWithSpectator extends FigureControlWithSpectator {

	protected HeroInfo hero;

	public HeroControlWithSpectator(HeroInfo info, AI ai, JDGUI spectator) {
		super(info, ai, spectator);
		this.hero = info;
	}

	protected HeroInfo self() {
		return hero;
	}

}
