/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import figure.FigureInfo;

public class TextPercept extends Percept {
	
	private final String text;
	
	public TextPercept(String s, int round) {
		super(round);
		text = s;
	}
	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return Collections.emptyList();
	}

}
