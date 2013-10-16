package dungeon;

import game.DungeonGame;
//import dungeon.*;

/**
 * Stellt einen Ort oder eine Richtung als Ziel einer Bewegung dar
 *
 */
public class RouteInstruction {

	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;

	
	private Room destination;

	
	private int direction;


	public RouteInstruction(Room r) {
		destination = r;
	}
	
	public RouteInstruction(RoomInfo r) {
		destination = DungeonGame.getInstance().getDungeon().getRoom(r.getLocation());
	}


	public int getWay(Room start) {
		if(destination != null) {
			int i = start.getDungeon().getFirstStepFromTo2(start,destination);
			
			return i;
		}
		else {
			return direction;
		}
		
	}
	
	public int getWay(JDPoint start) {
		return getWay(destination.getDungeon().getRoom(start));
	}
	
	public int getWay(RoomInfo start) {
		return getWay(start.getLocation());
	}
	
//	public int getWay(room start) {
//		
//		if (destination != null) {
//			
//			if(destination == start) {
//				return 0;
//			}
//
//			int divX =
//				Math.abs(
//					(destination.getNumber().getX())
//						- (start.getNumber().getX()));
//			int divY =
//				Math.abs(
//					(destination.getNumber().getY())
//						- (start.getNumber().getY()));
//
//			int wishDir = 0;
//			boolean likeWalkX = true;
//			if (divX > divY) {
//
//				if (start.getNumber().getX()
//					< destination.getNumber().getY()) {
//					wishDir = EAST;
//				} else {
//					wishDir = WEST;
//				}
//			} else {
//				likeWalkX = false;
//				if (start.getNumber().getY()
//					< destination.getNumber().getY()) {
//					wishDir = SOUTH;
//				} else if (
//					start.getNumber().getY()
//						> destination.getNumber().getY()) {
//					wishDir = NORTH;
//				}
//
//			}
//
//			boolean possible = start.leaveable(wishDir);
//			if (possible) {
//				return wishDir;
//			} else {
//				if (likeWalkX) {
//					if (start.getNumber().getY()
//						< destination.getNumber().getY()) {
//						wishDir = SOUTH;
//					} else if (
//						start.getNumber().getY()
//							> destination.getNumber().getY()) {
//						wishDir = NORTH;
//					}
//				} else {
//					if (start.getNumber().getX()
//						< destination.getNumber().getY()) {
//						wishDir = EAST;
//					} else {
//						wishDir = WEST;
//					}
//				}
//			}
//			possible = start.leaveable(wishDir);
//			if (possible) {
//				return wishDir;
//			} 
//		}
//		return 0;
//	}

	public static int turnRight(int dir) {
		if (!validDir(dir)) {
			//System.out.println("keine gueltige Richtung bei Rechtsdrehung");
		}
		int x = dir - 1;
		x = (x + 1) % 4;
		////System.out.println("turningRight: "+dirToString(dir)+" -> " +dirToString(x+1));
		return x + 1;
	}

	public static boolean validDir(int dir) {
		if ((dir != 1) && (dir != 2) && (dir != 3) && (dir != 4)) {
			return false;
		}
		return true;
	}

	public static int turnLeft(int dir) {
		if (!validDir(dir)) {
			//System.out.println("keine gueltige Richtung bei Linksdrehung");
		}
		int x = dir - 1;
		x = ((x - 1) + 4) % 4;
		////System.out.println("turningLeft: "+dirToString(dir)+" -> " +dirToString(x+1));

		return x + 1;
	}

	public static String dirToString(int dir) {
		if (dir == SOUTH) {
			return "south";
		}
		if (dir == NORTH) {
			return "north";
		}
		if (dir == WEST) {
			return "west";
		}
		if (dir == EAST) {
			return "east";
		}
		return "ungueltige Richtung";

	}

	public static int turnOpp(int dir) {
		int x = dir - 1;
		x = (x + 2) % 4;
		////System.out.println("turningOpp: "+dirToString(dir)+" -> " +dirToString(x+1));

		return x + 1;
	}

	public RouteInstruction(int i) {
		direction = i;
	}

	/**
	 * 
	 * @uml.property name="direction"
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * 
	 * @uml.property name="destination"
	 */
	public Room getDestination() {
		return destination;
	}

	public String toString() {
		if(destination != null) {
			return("RouteInstruction To : "+destination.toString());
		}
		else {
			return("RouteInstruction nach: "+ direction);
		}
	}
}
