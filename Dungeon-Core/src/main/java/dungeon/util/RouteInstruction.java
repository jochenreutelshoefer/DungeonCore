package dungeon.util;

import dungeon.Dir;
import game.DungeonGame;
//import dungeon.*;
import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;

/**
 * Stellt einen Ort oder eine Richtung als Ziel einer Bewegung dar
 *
 */
public class RouteInstruction {

	@Deprecated
	public static final int NORTH = 1;
	@Deprecated
	public static final int EAST = 2;
	@Deprecated
	public static final int SOUTH = 3;
	@Deprecated
	public static final int WEST = 4;

	public static Direction direction(int value) {
		if (value == Direction.North.val)
			return Direction.North;
		if (value == Direction.South.val)
			return Direction.South;
		if (value == Direction.West.val)
			return Direction.West;
		if (value == Direction.East.val)
			return Direction.East;

		return null;
	}


	public enum Direction {

		North(1), East(2), South(3), West(4);
		
		private int val;
		Direction(int val) {this.val = val;}

		public int getValue() {
			return val;
		}

		public static Direction opposite(Direction dir) {
			if(dir == Direction.North) {
				return Direction.South;
			}
			if(dir == Direction.East) {
				return Direction.West;
			}
			if(dir == Direction.South) {
				return Direction.North;
			}
			if(dir == Direction.West) {
				return Direction.East;
			}
			throw new IllegalArgumentException();
		}

		public static Direction fromInteger(int val) {
			if(val == Direction.North.val) {
				return Direction.North;
			}
			if(val == Direction.East.val) {
				return Direction.East;
			}
			if(val == Direction.South.val) {
				return Direction.South;
			}
			if(val == Direction.West.val) {
				return Direction.West;
			}
			throw new IllegalArgumentException();
		}



	};
	
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
			int i = DungeonUtils.getFirstStepFromTo2(start.getDungeon(), start,
					destination);
			
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

	public static Direction turnOpp(Direction dir) {
		return RouteInstruction.direction(dir.getValue());
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

	@Override
	public String toString() {
		if(destination != null) {
			return("RouteInstruction To : "+destination.toString());
		}
		else {
			return("RouteInstruction nach: "+ direction);
		}
	}
}
