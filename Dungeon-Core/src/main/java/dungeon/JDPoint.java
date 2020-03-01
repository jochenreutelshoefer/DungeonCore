package dungeon;

import java.io.Serializable;

import dungeon.util.RouteInstruction;

/**
 * Eine Klasse fuer X und Y Koordinate mit einigen Zusatzmethoden 
 * bzgl. Richtungen und Entfernungen
 *
 */
public class JDPoint implements Serializable{

	
	private int x;

	private int y;
	

    public JDPoint(int x, int y){
    	this.x = x;
    	this.y = y;
    }



    public JDPoint(double x, double y){
    	this.x = (int)x;
    	this.y = (int)y;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		JDPoint jdPoint = (JDPoint) o;

		if (x != jdPoint.x) return false;
		return y == jdPoint.y;

	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	@Override
	public String toString() {
	return (" "+Integer.toString(x)+" - "+Integer.toString(y));
    }


	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	
	public void setX(int x) {
		this.x = x;
	}


	public void setY(int y) {
		this.y = y;
	}

    public void incX(){
	x++;
    }
    public void decX(){
	x--;
    }
    public void incY(){
	y++;
    }
    public void decY(){
	y--;
    }

	public boolean isNeighbour(JDPoint p) {
		return relativeTo(p) != null;
	}
		     
    public RouteInstruction.Direction relativeTo(JDPoint p){
	if(p.getX() == this.x){
	    if(p.getY() == this.y + 1)return RouteInstruction.Direction.South;
	    if(p.getY() == this.y - 1)return RouteInstruction.Direction.North;
	}
	else if(p.getY() == this.y){
	    if(p.getX() == this.x + 1)return RouteInstruction.Direction.East;
	    if(p.getX() == this.x - 1)return RouteInstruction.Direction.West;
	}
	return null;
    }
    
    public static JDPoint walkDir(JDPoint start, int dir, int dist) {
    	int x = start.x;
    	int y = start.y;
    	if(dir == RouteInstruction.NORTH) {
    		y -= dist;	    		
    	}
    	if(dir == RouteInstruction.SOUTH) {
    		y += dist;	    		
    	}
    	if(dir == RouteInstruction.WEST) {
    		x -= dist;	    		
    	}
    	if(dir == RouteInstruction.EAST) {
    		x += dist;	    		
    	}
    	return new JDPoint(x,y);
    	
    }
    
    public static int getAbsMaxDistXY(JDPoint a, JDPoint b) {
    	int dx = Math.abs(a.x - b.x);	
    	int dy = Math.abs(a.y - b.y);
    	if(dy > dx ) {
    		return dy;
    	} else {
    		return dx;
    	}
    }
    
    public static int getAbsDist(JDPoint a, JDPoint b) {
		int dx = Math.abs(a.x - b.x);	
		int dy = Math.abs(a.y - b.y);
		return dx + dy;
    }


}
	
