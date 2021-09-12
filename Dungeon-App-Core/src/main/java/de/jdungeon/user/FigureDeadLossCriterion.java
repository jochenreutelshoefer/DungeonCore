package de.jdungeon.user;

import de.jdungeon.figure.Figure;

public class FigureDeadLossCriterion extends LossCriterion {

	private Figure figure;

	public FigureDeadLossCriterion(Figure figure) {
		this.figure = figure;
	}

	@Override
	public boolean isMet(DefaultDungeonSession session) {
		return figure.isDead();
	}

	@Override
	public String getMessage() {
		return figure.getName()+" ist tot!";
	}
}
