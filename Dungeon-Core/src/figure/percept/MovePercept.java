/*
 * Created on 08.01.2006
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

public class MovePercept extends OpticalPercept {
	
	private Room from;
	private Room to;
	private Figure figure;
	
	
	
	public RoomInfo getFrom() {
		return new RoomInfo(from,viewer.getRoomVisibility());
	}


	public RoomInfo getTo() {
		return new RoomInfo(to,viewer.getRoomVisibility());
	}


	public MovePercept(Figure f, Room r1, Room r2) {
		figure = f;
		from = r1;
		to = r2;
	}
	
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(figure,viewer.getRoomVisibility());
	}
	
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getFigure());
		
		return l;
	}
	
	

}
