package de.jdungeon.dungeon.util;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.DungeonVisibilityMap;

public  class MapData {
	
	DungeonVisibilityMap map = null;
	Dungeon d = null;
	
	public MapData(Dungeon d) {
		map = DungeonVisibilityMap.getAllVisMap(d);
		this.d = d;
	}
	public MapData(DungeonVisibilityMap map) {
		this.map = map;
		d = map.getDungeon();
	}

	public RoomInfo getRoom(JDPoint p) {
		return RoomInfo.makeRoomInfo(d.getRoom(p).getAccessibleNeighbour(),map);
	}
	
	
	
}
