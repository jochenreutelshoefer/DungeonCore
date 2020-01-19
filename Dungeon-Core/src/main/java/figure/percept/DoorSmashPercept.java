package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;

public class DoorSmashPercept extends OpticalPercept {
	
	private final Figure victim;
	private final int value;

	public DoorSmashPercept(Figure f, int value) {
		super(f.getLocation());
		this.value = value;
		this.victim = f;
	}
	
	public int getValue() {
		return value;
	}
	public FigureInfo getVictim() {
		return FigureInfo.makeFigureInfo(victim,viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<FigureInfo>();
		l.add(getVictim());
		return l;
	}
}
