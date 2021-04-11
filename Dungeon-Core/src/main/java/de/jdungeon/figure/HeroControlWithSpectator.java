package de.jdungeon.figure;

import de.jdungeon.ai.AbstractAI;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.game.JDGUI;

public class HeroControlWithSpectator extends FigureControlWithSpectator {

	protected HeroInfo hero;


	public HeroControlWithSpectator(HeroInfo info, AbstractAI ai, JDGUI spectator) {
		super(info, ai, spectator);
		this.hero = info;

		/*
		 * Initial delay value of actions for smooth de.jdungeon.gui display
		 */
		this.setDelay(300);
	}

	protected HeroInfo self() {
		return hero;
	}



}
