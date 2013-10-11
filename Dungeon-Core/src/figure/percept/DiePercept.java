/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;

public class DiePercept extends OpticalPercept {
	
	private Figure f;
	private Room from;
	
	public DiePercept(Figure f, Room from) {
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
