package gui.bot;

import ai.AttitudeDefaultHero;
import ai.GuiAI;
import figure.FigureInfo;
import figure.hero.HeroInfo;

public abstract class AbstractHeroBotAI extends GuiAI {

	public AbstractHeroBotAI() {
		super(new AttitudeDefaultHero());
	}

	protected HeroInfo hero;
	
	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			this.hero = ((HeroInfo) f);
		}
	}


}
