package figure.percept;

import java.util.LinkedList;
import java.util.List;

import spell.Spell;
import spell.SpellInfo;
import figure.Figure;
import figure.FigureInfo;

public class BreakSpellPercept extends OpticalPercept {

	private Figure f;
	//private Spell spell;
	//private boolean begins = false;
	
	public BreakSpellPercept(Figure f) {
		this.f = f;
		
	}
	
	
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
//	public SpellInfo getSpell() {
//		return new SpellInfo(spell,viewer.getRoomVisibility());
//	}
	
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getFigure());
		
		return l;
	}

	


}
