/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public abstract class Percept {
	
	protected int round;
	
	public Percept(int gameRound) {
		round = gameRound;
	}

	public int getRound() {
		return round;
	}
	
	public abstract List<FigureInfo> getInvolvedFigures(FigureInfo viewer);


}
