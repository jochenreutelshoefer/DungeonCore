/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.Collections;
import java.util.List;

import figure.FigureInfo;

public class InfoPercept extends Percept {
	
	//public static final int TEXT = 0;
	public static final int LOCKED_DOOR = 1;
	public static final int UNLOCKED_DOOR = 2;
	public static final int LEVEL_UP = 3;
	public static final int FOUND_ITEM = 4;
	public static final int SPOTTED_FIGURES = 5;
	public static final int END_ROUND = 6;
	public static final int RESPAWN = 7;
//	public static final int U = 10;
//	public static final int U = 11;
//	public static final int U = 12;
//	public static final int U = 13;
//	public static final int U = 14;
//	public static final int U = 15;
//	public static final int U = 16;
	
	private final int code;
	public InfoPercept(int code, int round) {
		super(round);
		this.code = code;
	}
	/**
	 * @return Returns the code.
	 */
	public int getCode() {
		return code;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return Collections.emptyList();
	}
	

}
