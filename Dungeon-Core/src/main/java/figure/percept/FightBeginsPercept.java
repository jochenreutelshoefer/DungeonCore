package figure.percept;

import java.util.List;

import figure.FigureInfo;

public class FightBeginsPercept extends Percept {

	private List<FigureInfo> figures;

	public FightBeginsPercept(List<FigureInfo> figures) {
		this.figures = figures;
	}
	
	public List<FigureInfo> getInvolvedFigures() {
		return figures;
	}
	
}
