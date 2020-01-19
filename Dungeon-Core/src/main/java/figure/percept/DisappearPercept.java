package figure.percept;

import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;

public class DisappearPercept extends OpticalPercept {

	private Figure f;
	private Room from;
	
	public DisappearPercept(Figure f, Room from) {
		super(from.getNumber());
		this.f = f;
		this.from = from;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public RoomInfo getRoom() {
		return RoomInfo.makeRoomInfo(from, viewer.getRoomVisibility());
	}
	
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<FigureInfo>();
		l.add(getFigure());
		return l;
	}

}
