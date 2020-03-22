/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import spell.AbstractSpell;
import spell.SpellInfo;
import figure.Figure;
import figure.FigureInfo;

public class SpellPercept extends OpticalPercept {
	
	private Figure f;
	private AbstractSpell spell;
	private boolean begins = false;
	
	public SpellPercept(Figure f, AbstractSpell s, int round) {
		super(f.getLocation(), round);
		this.f = f;
		this.spell = s;
	}
	
	public SpellPercept(Figure f, AbstractSpell s, boolean begins, int round) {
		super(f.getLocation(), round);
		this.f = f;
		this.spell = s;
		this.begins = begins;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public SpellInfo getSpell() {
		return new SpellInfo(spell,viewer.getRoomVisibility());
	}

	public boolean isBegins() {
		return begins;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}

}
