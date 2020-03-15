package figure.percept;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;

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


	public LeavesPercept(Figure f, Room r1 , RouteInstruction.Direction direction) {
		super(r1.getNumber());
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
