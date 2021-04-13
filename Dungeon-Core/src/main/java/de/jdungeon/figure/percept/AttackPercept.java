/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Position;
import de.jdungeon.skill.attack.Slap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class AttackPercept extends OpticalPercept {
	
	private final Figure attacker;
	private final Figure victim;
	private final Slap slap;
	
	public AttackPercept(Figure a, Figure b, Slap s, int round) {
		super(b.getRoomNumber(), round);
		attacker = a;
		victim = b;
		slap = s;
		
	}	

	public Position.Pos getFromPos() {
		return Position.Pos.fromValue(attacker.getPos().getIndex());
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
		l.add(getVictim());
		l.add(getAttacker());
		return l;
	}
	
	

}
