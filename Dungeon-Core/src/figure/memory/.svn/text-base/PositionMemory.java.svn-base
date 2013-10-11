package figure.memory;

import dungeon.Position;
import dungeon.PositionInRoomInfo;
import figure.FigureInfo;

public class PositionMemory extends MemoryObject {
	
	private FigureMemory figure;
	
	
	public PositionMemory(Position pos,FigureInfo info) {
		if(pos.getFigure() != null) {
			figure = pos.getFigure().getMemoryObject(info);
		}
		
	}


	public FigureMemory getFigure() {
		return figure;
	}

}
