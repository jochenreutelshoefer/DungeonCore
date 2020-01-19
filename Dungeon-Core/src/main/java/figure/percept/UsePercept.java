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
import item.interfaces.Usable;

public class UsePercept extends OpticalPercept {
	
	private final Figure actor;

	public UsePercept(Figure actor, Usable o) {
		super(actor.getLocation());
		this.actor = actor;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(actor,viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}
	
	

}
