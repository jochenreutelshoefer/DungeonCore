/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;

public class StepPercept extends OpticalPercept {
	
	private Figure f;
	private int from;
	private int to;
	
	public StepPercept(Figure f, int from, int to) {
		this.f = f;
		this.from = from;
		this.to = to;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public int getFromIndex() {
		return from;
	}
	
	public int getToIndex() {
		
		return to;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}

}
