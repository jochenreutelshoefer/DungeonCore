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

public class ScoutPercept extends OpticalPercept {
	
	private Figure f;
	private Room from;
	private int dir;
	
	public ScoutPercept(Figure f, Room from, int dir) {
		this.f = f;
		this.from = from;
		this.dir = dir;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public RoomInfo getRoom() {
		return RoomInfo.makeRoomInfo(from, viewer.getRoomVisibility());
	}

	public int getDir() {
		return dir;
	}
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getFigure());
		
		return l;
	}


}
