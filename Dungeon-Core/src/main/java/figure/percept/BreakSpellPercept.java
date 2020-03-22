package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;

public class BreakSpellPercept extends OpticalPercept {

	private final Figure f;

	public BreakSpellPercept(Figure f, int round) {
		super(f.getLocation(), round);
		this.f = f;
		
	}
	
	
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}

	


}
