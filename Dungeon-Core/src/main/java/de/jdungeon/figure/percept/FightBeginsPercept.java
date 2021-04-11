package de.jdungeon.figure.percept;

import java.util.List;

import de.jdungeon.figure.FigureInfo;

public class FightBeginsPercept extends OpticalPercept {

	private final List<FigureInfo> figures;

	public FightBeginsPercept(List<FigureInfo> figures, int round) {
		super(figures.iterator().next().getRoomNumber(), round);
		this.figures = figures;
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return figures;
	}
	
}
