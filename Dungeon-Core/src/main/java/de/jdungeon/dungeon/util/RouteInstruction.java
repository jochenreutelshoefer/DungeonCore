package de.jdungeon.dungeon.util;

import de.jdungeon.figure.DungeonVisibilityMap;
//import dungeon.*;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;

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
		
		private final int val;
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
			return null;
		}

		public static Direction fromPoints(JDPoint p1, JDPoint p2) {
			if(p1.getX() == p2.getX()) {
				if(p1.getY() == p2.getY() - 1) {
					return Direction.South;
				}
				if(p1.getY() == p2.getY() + 1) {
					return Direction.North;
				}
			}
			if(p1.getY() == p2.getY()) {
				if(p1.getX() == p2.getX() + 1) {
					return Direction.West;
				}
				if(p1.getX() == p2.getX() - 1) {
					return Direction.East;
				}
			}
			// this points are not next to each other
			return null;
		}

	};
	
	private JDPoint destination;

	
	private int direction;


	public RouteInstruction(JDPoint r) {
		destination = r;
	}
	
	public RouteInstruction(RoomInfo r) {
		destination = r.getNumber();
	}


	public int getWay(Room start, DungeonVisibilityMap visMap) {
		if(destination != null) {
			return DungeonUtils.getFirstStepFromTo(start.getDungeon(), start.getPoint(), destination, visMap).getValue();
		}
		else {
			return direction;
		}
		
	}

	public int getWay(JDPoint start, DungeonVisibilityMap visMap) {
		if(destination != null) {
			return DungeonUtils.getFirstStepFromTo(visMap.getDungeon(), start, destination, visMap).getValue();
		}
		else {
			return direction;
		}

	}


	public int getWay(RoomInfo start, DungeonVisibilityMap visMap) {
		return getWay(start.getNumber(), visMap);
	}
	
	public static int turnRight(int dir) {
		int x = dir - 1;
		x = (x + 1) % 4;
		return x + 1;
	}

	public static boolean validDir(int dir) {
		if ((dir != 1) && (dir != 2) && (dir != 3) && (dir != 4)) {
			return false;
		}
		return true;
	}

	public static int turnLeft(int dir) {
		int x = dir - 1;
		x = ((x - 1) + 4) % 4;
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
		return RouteInstruction.direction(RouteInstruction.turnOpp(dir.getValue()));
	}

	public static int turnOpp(int dir) {
		int x = dir - 1;
		x = (x + 2) % 4;
		return x + 1;
	}

	public RouteInstruction(int i) {
		direction = i;
	}

	public int getDirection() {
		return direction;
	}

	public JDPoint getDestination() {
		return destination;
	}

	@Override
	public String toString() {
		if(destination != null) {
			return("RouteInstruction To : "+ destination);
		}
		else {
			return("RouteInstruction nach: "+ direction);
		}
	}
}
