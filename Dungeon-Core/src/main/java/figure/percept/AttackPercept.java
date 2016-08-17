/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import fight.Slap;
import fight.SlapResult;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;

public class AttackPercept extends OpticalPercept {
	
	private Figure attacker;
	private Figure victim;
	private Slap slap;
	
	public AttackPercept(Figure a, Figure b, Slap s) {
		attacker = a;
		victim = b;
		slap = s;
		
	}	
	

	public FigureInfo getAttacker() {
		return FigureInfo.makeFigureInfo(attacker,viewer.getRoomVisibility());
	}
	public FigureInfo getVictim() {
		return FigureInfo.makeFigureInfo(victim, viewer.getRoomVisibility());
	}
	
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getVictim());
		l.add(getAttacker());
		return l;
	}
	
	

}
