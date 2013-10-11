/*
 * Created on 26.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.memory.MemoryObject;
import game.InfoEntity;
import gui.Paragraph;

public class PositionInRoomInfo extends InfoEntity{ 
	
	Position pos;
	public PositionInRoomInfo(Position p,DungeonVisibilityMap map) {
		super(map);
		pos = p;
	}
	
	public int getIndex(){
		return pos.getIndex();
	}
	
	
	public JDPoint getLocation() {
		return pos.getLocation();
	}
	
	public int getNextIndex() {
		return pos.getNext().getIndex();
	}
	
	public boolean isOccupied() {
		return pos.getFigure() != null;
	}
	
	public int getLastIndex() {
		return pos.getLast().getIndex();
	}
	
	public FigureInfo getFigure() {
		if( pos.getFigure() == null) {
			return null;
		}
		else {
			return FigureInfo.makeFigureInfo(pos.getFigure(), map);
		}
	}
	
	public Paragraph[] getParagraphs() {
		return null;
	}

	public MemoryObject getMemoryObject(FigureInfo info) {
		
		return null;
	}

}
