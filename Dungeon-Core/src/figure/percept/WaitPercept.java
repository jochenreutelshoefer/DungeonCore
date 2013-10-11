package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;

public class WaitPercept extends OpticalPercept {

	
	private Figure f;
	
	public WaitPercept(Figure f) {
		this.f = f;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,f.getRoomVisibility());
	}
	
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getFigure());
		
		return l;
	}
	
}
