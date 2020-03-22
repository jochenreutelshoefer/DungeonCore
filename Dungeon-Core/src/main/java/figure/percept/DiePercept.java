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
	
	private final Figure f;
	private final Room from;
	private int damage = -1;

	public DiePercept(Figure f, Room from, int round) {
		super(f.getLocation(), round);
		this.f = f;
		this.from = from;
	}
	
	public DiePercept(Figure f, Room from, int damage, int round) {
		super(f.getLocation(), round);
		this.f = f;
		this.from = from;
		this.damage = damage;
	}
	
	public int getDamage() {
		return damage;
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
