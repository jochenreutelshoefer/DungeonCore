package dungeon;

/**
 * Eine Klasse fuer X und Y Koordinate mit einigen Zusatzmethoden 
 * bzgl. Richtungen und Entfernungen
 *
 */
public class JDPoint{

	
	private int x;

	private int y;
	
	public static JDPoint [][] points;
	
	public static void setPoints(JDPoint[][] p) {
		points = p;
	}
	
	public static JDPoint getPoint(int x, int y) {
		return points [x][y];
	}



    public JDPoint(int x, int y){
    	this.x = x;
    	this.y = y;
    }
    
    public JDPoint(double x, double y){
    	this.x = (int)x;
    	this.y = (int)y;
    }

    public boolean equals(JDPoint p){
	if((p.getX() == this.x) && (p.getY() == this.y)) return true;
	else return false;
    }

    public boolean equals(int x, int y){
	if((this.x == x)&&(this.y == y)) return true;
	else return false;
    }

    public String toString(){
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
		     
    public String relativeTo(JDPoint p){
	if(p.getX() == this.x){
	    if(p.getY() == this.y + 1)return ("south");
	    if(p.getY() == this.y - 1)return ("north");
	}
	else if(p.getY() == this.y){
	    if(p.getX() == this.x + 1)return ("east");
	    if(p.getX() == this.x - 1)return ("west");
	}
	return ("notNeighbour");
    }
    
    public static JDPoint walkDir(JDPoint start, int dir, int dist) {
    	////System.out.println("Marchieren von: "+start.toString());
    	////System.out.println("in Richtung: "+dir);
    	////System.out.println("Um :" +dist);
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
	
