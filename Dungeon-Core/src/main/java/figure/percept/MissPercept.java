/*
 * Created on 17.01.2006
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

public class MissPercept extends OpticalPercept {

	private final Figure attacker;
	private final Figure victim;
	
	public MissPercept(Figure a, Figure b, int round) {
		super(b.getLocation(), round);
		attacker = a;
		victim = b;
	}

	public FigureInfo getAttacker() {
		return FigureInfo.makeFigureInfo(attacker,viewer.getRoomVisibility());
	}
	public FigureInfo getVictim() {
		return FigureInfo.makeFigureInfo(victim, viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getAttacker());
		l.add(getVictim());
		return l;
	}
	
	
	
	

}
