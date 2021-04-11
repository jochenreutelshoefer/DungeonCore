package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class DoorSmashPercept extends OpticalPercept {
	
	private final Figure victim;

	private final Figure opponent;

	private final JDPoint victimPosition;

	private final JDPoint opponentPosition;

	private final int value;

	public DoorSmashPercept(Figure f, Figure opponent, int value, int round) {
		super(f.getRoomNumber(), round);
		this.opponent = opponent;
		this.value = value;
		this.victim = f;
		opponentPosition = opponent.getRoomNumber();
		victimPosition = f.getRoomNumber();
	}

	public int getValue() {
		return value;
	}
	public FigureInfo getVictim() {
		return FigureInfo.makeFigureInfo(victim,viewer.getRoomVisibility());
	}

	public JDPoint getVictimPosition() {
		return victimPosition;
	}

	public JDPoint getOpponentPosition() {
		return opponentPosition;
	}

	public FigureInfo getOpponent() {
		return FigureInfo.makeFigureInfo(opponent, viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<FigureInfo>();
		l.add(getVictim());
		l.add(getOpponent());
		return l;
	}
}
