/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import dungeon.Position;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;

public class FleePercept extends OpticalPercept {
	
	private final Figure f;
	private final Position from;
	private final int dir;
	private final boolean success;
	
	public FleePercept(Figure f, Position from, int dir, boolean suc, int round) {
		super(f.getRoomNumber(), round);
		this.f = f;
		this.from = from;
		this.dir = dir;
		this.success = suc;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public RoomInfo getRoom() {
		return RoomInfo.makeRoomInfo(from.getRoom(), viewer.getRoomVisibility());
	}

	public boolean isSuccess() {
		return success;
	}

	public int getDir() {
		return dir;
	}
	
	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility()));
		
		return l;
	}

}
