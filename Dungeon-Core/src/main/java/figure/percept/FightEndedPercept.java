/*
 * Created on 09.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.List;

import figure.FigureInfo;

public class FightEndedPercept extends OpticalPercept {

	private final List<FigureInfo> figures;

	public FightEndedPercept(List<FigureInfo> figures) {
		super(figures.iterator().next().getRoomNumber());
		this.figures = figures;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return figures;
	}
}
