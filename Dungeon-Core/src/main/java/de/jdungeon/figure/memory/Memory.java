package de.jdungeon.figure.memory;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;

public class Memory {
	
	private RoomMemory [][] rooms;
	
	public Memory(int x, int y) {
		rooms = new RoomMemory[x][y];
	}
	
	public Memory(JDPoint p) {
		rooms = new RoomMemory[p.getX()][p.getY()];
	}
	
	public void storeRoom(RoomInfo r, int round,FigureInfo info) {
		// TODO Nullpointer r is null!!!
		if(r == null) return;
		JDPoint p = r.getRoomNumber();
		RoomMemory mem = r.getMemoryObject(info);
		mem.setRound(round);
		rooms[p.getX()][p.getY()] = mem;
		
	}
	
	public RoomMemory getMemory(JDPoint p ) {
		return rooms[p.getX()][p.getY()];
	}

}
