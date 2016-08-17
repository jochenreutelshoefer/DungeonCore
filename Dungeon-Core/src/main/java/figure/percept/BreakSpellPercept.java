package figure.percept;

import java.util.LinkedList;
import java.util.List;

import spell.Spell;
import spell.SpellInfo;
import figure.Figure;
import figure.FigureInfo;

public class BreakSpellPercept extends OpticalPercept {

	private Figure f;

	public BreakSpellPercept(Figure f) {
		this.f = f;
		
	}
	
	
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}

	


}
