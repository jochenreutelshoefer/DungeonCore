/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;

public class TumblingPercept extends OpticalPercept {

	private Figure f;

	public TumblingPercept(Figure f) {
		this.f = f;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getFigure());
		
		return l;
	}
}
