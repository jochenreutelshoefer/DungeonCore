/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.Collections;
import java.util.List;

import de.jdungeon.figure.FigureInfo;

public class TextPercept extends Percept {
	
	private final String text;
	
	public TextPercept(String s, int round) {
		super(round);
		text = s;
	}


	/**
	 * @return Returns the de.jdungeon.text.
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return Collections.emptyList();
	}

}
