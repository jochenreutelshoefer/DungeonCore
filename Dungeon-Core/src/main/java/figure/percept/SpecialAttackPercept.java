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

public class SpecialAttackPercept extends OpticalPercept {
	
	private final Figure target;
	private final int type;
	private final Figure attacker;
	public SpecialAttackPercept(int type,Figure t,Figure a) {
		this.type = type;
		target = t;
		attacker = a;
	}
	
	public FigureInfo getTarget() {
		return FigureInfo.makeFigureInfo(target,viewer.getRoomVisibility());
	}
	public FigureInfo getAttacker() {
		return FigureInfo.makeFigureInfo(attacker,viewer.getRoomVisibility());
	}
	
	/**
	 * @return Returns the type.
	 */
	public int getType() {
		return type;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getAttacker());
		l.add(getTarget());
		return l;
	}

}
