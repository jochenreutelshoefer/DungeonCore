package de.jdungeon.figure.percept;

import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.02.20.
 */
public class LeavesPercept extends OpticalPercept {

	private final Room from;

	private final RouteInstruction.Direction direction;

	private final Figure figure;
	public RoomInfo getFrom() {
		return new RoomInfo(from,viewer.getRoomVisibility());
	}


	public RoomInfo getTo() {
		return new RoomInfo(from.getNeighbourRoom(direction),viewer.getRoomVisibility());
	}


	public LeavesPercept(Figure f, Room r1 , RouteInstruction.Direction direction, int round) {
		super(r1.getNumber(), round);
		figure = f;
		from = r1;
		this.direction = direction;
	}


	public RouteInstruction.Direction getDirection() {
		return direction;
	}

	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(figure,viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return Collections.singletonList(getFigure());
	}

}