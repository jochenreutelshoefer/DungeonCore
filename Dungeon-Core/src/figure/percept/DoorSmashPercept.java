package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;

public class DoorSmashPercept extends OpticalPercept {
	
	private Figure victim;
	private int value;
	public DoorSmashPercept(Figure f, int value) {
		this.value = value;
		this.victim = f;
	}
	
	public int getValue() {
		return value;
	}
	public FigureInfo getVictim() {
		return FigureInfo.makeFigureInfo(victim,viewer.getRoomVisibility());
	}

	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getVictim());
		
		return l;
	}
}
