/*
 * Created on 27.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import java.awt.Point;


public class Dir {
	
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
	
	public static int getOppositDir(int dir) {
		return ((dir+1)%4)+1;
	}
	
	
	public static int getDirFromToIfNeighbour(Point from, Point to) {
		int x1 = from.x;
		int y1 = from.y;
		int x2 = to.x;
		int y2 = to.y;
		
		if(x1 == x2) {
			if(y1 == y2+1) {
				return Dir.NORTH;
			}
			if(y1+1 == y2) {
				return Dir.SOUTH;
			}
		}
		if(y1 == y2) {
			if(x1 == x2 + 1) {
				return Dir.WEST;
			}
			if(x1+1 == x2) {
				return Dir.EAST;
			}
		}
		
		return -1;
		
	}
	
	public static int getDirFromToIfNeighbour(JDPoint from, Point to) {
		return getDirFromToIfNeighbour(new Point(from.getX(),from.getY()),to);
	}
	
	public static int getDirFromToIfNeighbour(JDPoint from, JDPoint to) {
		return getDirFromToIfNeighbour(new Point(from.getX(),from.getY()),new Point(to.getX(),to.getY()));
	}

}
