/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import fight.Slap;
import figure.Figure;
import figure.FigureInfo;

public class ShieldBlockPercept extends OpticalPercept {
	
	private Figure f;

	public ShieldBlockPercept(Figure f, int round) {
		super(f.getLocation(), round);
		this.f = f;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f, viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}
	
}
