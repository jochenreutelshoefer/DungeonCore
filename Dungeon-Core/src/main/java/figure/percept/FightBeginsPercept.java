package figure.percept;

import java.util.List;

import figure.FigureInfo;

public class FightBeginsPercept extends OpticalPercept {

	private final List<FigureInfo> figures;

	public FightBeginsPercept(List<FigureInfo> figures) {
		super(figures.iterator().next().getRoomNumber());
		this.figures = figures;
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return figures;
	}
	
}
