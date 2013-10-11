/*
 * Created on 08.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D;

import java.awt.Point;
import java.util.Vector;

import dungeon.RoomInfo;

public class RoomGraphicObjectContainer {
	
	RoomInfo room;
	
	GraphicObject shrine;
	Vector monster = new Vector();
	Vector items = new Vector();
	Vector doors = new Vector();
	GraphicObject chest;
	GraphicObject spot;
	Vector walls = new Vector();
	Vector lastWalls = new Vector();
	Vector positions = new Vector();
	
	public RoomGraphicObjectContainer(RoomInfo room) {
		this.room = room;
	}
	
	public void reset() {
		shrine = null;
		monster = new Vector();
		items = new Vector();
		doors = new Vector();
		chest = null;
		spot = null;
		walls = new Vector();
		lastWalls = new Vector();
		positions = new Vector();
	}
	
//	public GraphicObject getClickedObject(Point p) {
//		
//	}

}
