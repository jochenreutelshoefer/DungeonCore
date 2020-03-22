package figure.percept;

import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;

public class DisappearPercept extends OpticalPercept {

	private final Figure f;
	private final Room from;
	
	public DisappearPercept(Figure f, Room from, int round) {
		super(from.getNumber(), round);
		this.f = f;
		this.from = from;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public RoomInfo getRoom() {
		return RoomInfo.makeRoomInfo(from, viewer.getRoomVisibility());
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<FigureInfo>();
		l.add(getFigure());
		return l;
	}

}
