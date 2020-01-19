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

public class FleePercept extends OpticalPercept {
	
	private Figure f;
	private Room from;
	private int dir;
	private boolean success;
	
	public FleePercept(Figure f, Room from, int dir,boolean suc) {
		super(f.getLocation());
		this.f = f;
		this.from = from;
		this.dir = dir;
		this.success = suc;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public RoomInfo getRoom() {
		return RoomInfo.makeRoomInfo(from, viewer.getRoomVisibility());
	}

	public boolean isSuccess() {
		return success;
	}

	public int getDir() {
		return dir;
	}
	
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility()));
		
		return l;
	}

}
