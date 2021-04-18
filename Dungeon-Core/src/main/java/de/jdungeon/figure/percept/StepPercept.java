/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class StepPercept extends SimpleActorPercept {
	
	private int from;
	private int to;
	
	public StepPercept(Figure f, int from, int to, int round) {
		super(f, f.getRoomNumber(), round);
		this.from = from;
		this.to = to;
	}
	
	public int getFromIndex() {
		return from;
	}
	
	public int getToIndex() {
		return to;
	}

}
