package figure;

import ai.AI;
import figure.hero.HeroInfo;
import game.JDGUI;

public class HeroControlWithSpectator extends FigureControlWithSpectator {

	protected HeroInfo hero;


	public HeroControlWithSpectator(HeroInfo info, AI ai, JDGUI spectator) {
		super(info, ai, spectator);
		this.hero = info;

		/*
		 * Initial delay value of actions for smooth gui display
		 */
		this.setDelay(300);
	}

	protected HeroInfo self() {
		return hero;
	}



}
