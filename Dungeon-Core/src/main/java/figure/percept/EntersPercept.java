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

public class EntersPercept extends OpticalPercept {
	
	private final Room from;
	private final Figure figure;
	
	public RoomInfo getFrom() {
		return new RoomInfo(from,viewer.getRoomVisibility());
	}


	public RoomInfo getTo() {
		return new RoomInfo(from,viewer.getRoomVisibility());
	}


	public EntersPercept(Figure f, Room r1, int round) {
		super(r1.getNumber(), round);
		if(f == null) {
			throw new IllegalArgumentException("Figure may not be null");
		}
		figure = f;
		from = r1;
	}
	
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(figure,viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}
	
	

}
