/*
 * Created on 09.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class FightEndedPercept extends OpticalPercept {

	private final List<Figure> figures;

	public FightEndedPercept(List<Figure> figures, int round) {
		super(figures.iterator().next().getRoomNumber(), round);
		this.figures = figures;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures(FigureInfo viewer) {
		List<FigureInfo> figureInfos = new ArrayList<>();
		for (Figure figure : figures) {
			figureInfos.add(FigureInfo.makeFigureInfo(figure, viewer.getVisMap()));
		}
		return figureInfos;
	}
}
