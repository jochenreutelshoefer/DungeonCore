/*
 * Created on 01.12.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package dungeon;

import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.FigureInfo;
import figure.memory.PositionMemory;
import game.InfoEntity;

public class Position extends DungeonWorldObject{

	private Room room;

	private Figure figure;

	private int index;

	private Position next;

	private Position last;

	public Position(Room r, int index) {
		this.room = r;
		this.index = index;
	}

	/**
	 * @return Returns the f.
	 */
	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure info) {
		figure = info;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}

	public void figureEntersHere(Figure fig) {
		if (figure == null) {
			figure = fig;
			fig.setPos(this);

		} else {
			if (Math.random() < 0.5) {
				last.figureEntersHere(fig);
			} else {
				next.figureEntersHere(fig);
			}
		}
	}

	public static int getNextIndex(int index, boolean right) {
		if (right) {
			if (index == 7) {
				return 0;
			} else {
				return index + 1;
			}
		} else {
			if (index == 0) {
				return 7;

			} else {
				return index - 1;
			}
		}
	}

	public Room getRoom() {
		return room;
	}

	public static final int DIST_NEAR = 1;

	public static final int DIST_MID = 2;

	public static final int DIST_FAR = 3;

	public static final int DIST_FAREST = 4;

	private static final int[][] distMatrix = {
			{ 0, DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID,
					DIST_NEAR },
			{ DIST_NEAR, 0, DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR,
					DIST_MID },
			{ DIST_MID, DIST_NEAR, 0, DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR,
					DIST_FAR },
			{ DIST_FAR, DIST_MID, DIST_NEAR, 0, DIST_NEAR, DIST_MID, DIST_FAR,
					DIST_FAR },
			{ DIST_FAR, DIST_FAR, DIST_MID, DIST_NEAR, 0, DIST_NEAR, DIST_MID,
					DIST_FAR },
			{ DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID, DIST_NEAR, 0, DIST_NEAR,
					DIST_MID },
			{ DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID, DIST_NEAR, 0,
					DIST_NEAR },
			{ DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID,
					DIST_NEAR, 0 } };

	public int getDistanceTo(Position p) {
		if (p.getRoom() != room) {
			return -1;
		}
		if (p == this) {
			System.out.println("position mit mehr als 1 figur");
		}
		return getDistance(p.index);

	}

	public int getDistance(int otherIndex) {
		return distMatrix[index][otherIndex];
	}

	public static int getMinDistanceFromTo(int from, int to) {
		int right = getDistanceFromTo(from, to, true);
		int left = getDistanceFromTo(from, to, false);
		if (right < left) {
			return right;
		} else {
			return left;
		}

	}

	public static int getDistanceFromTo(int from, int to, boolean right) {
		int k = from;
		int dist = 0;
		while (k != to) {
			dist++;
			if (right) {
				k = incIndex(k);
			} else {
				k = decIndex(k);
			}
		}
		return dist;

	}

	public static int decIndex(int k) {
		if (k == 0) {
			return 7;
		} else {
			return k - 1;
		}
	}

	public static int incIndex(int k) {
		if (k < 7) {
			return k + 1;
		}
		if (k == 7) {
			return 0;
		}
		return -1;
	}

	public int getPossibleFleeDirection() {
		if (index == 1) {
			return Dir.NORTH;
		}
		if (index == 3) {
			return Dir.EAST;
		}
		if (index == 5) {
			return Dir.SOUTH;
		}
		if (index == 7) {
			return Dir.WEST;
		}
		return 0;

	}

	private static int[][] dirMatrix = {
			{ 0, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST, Dir.SOUTH, Dir.SOUTH,
					Dir.SOUTH },
			{ Dir.WEST, 0, Dir.EAST, Dir.EAST, Dir.SOUTH, Dir.SOUTH, Dir.SOUTH,
					Dir.WEST },
			{ Dir.WEST, Dir.WEST, 0, Dir.SOUTH, Dir.SOUTH, Dir.SOUTH, Dir.WEST,
					Dir.WEST },
			{ Dir.WEST, Dir.WEST, Dir.NORTH, 0, Dir.SOUTH, Dir.WEST, Dir.WEST,
					Dir.WEST },
			{ Dir.WEST, Dir.NORTH, Dir.NORTH, Dir.NORTH, 0, Dir.WEST, Dir.WEST,
					Dir.WEST },
			{ Dir.NORTH, Dir.NORTH, Dir.NORTH, Dir.EAST, Dir.EAST, 0, Dir.WEST,
					Dir.WEST },
			{ Dir.NORTH, Dir.NORTH, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST, 0,
					Dir.NORTH },
			{ Dir.NORTH, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST,
					Dir.SOUTH, 0 } };

	public static int getDirFromTo(int from, int to) {
		return dirMatrix[from][to];
	}

	public void figureLeaves() {
		if (figure != null) {
			figure.resetScoutedRooms();
		}
		figure = null;
	}

	public String toString() {
		String figureString = "null";
		if (figure != null) {
			figureString = figure.getName();
		}
		return "PositionInRoom: index:  " + index + " figure: " + figureString;
	}

	/**
	 * @return Returns the last.
	 */
	public Position getLast() {
		return last;
	}

	/**
	 * @return Returns the next.
	 */
	public Position getNext() {
		return next;
	}

	public void setLast(Position l) {
		last = l;
	}

	public void setNext(Position n) {
		next = n;
	}

	public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
		// TODO Auto-generated method stub
		return null;
	}

	public JDPoint getLocation() {
		return room.getLocation();
	}

	public PositionMemory getMemoryObject(FigureInfo info) {
		
		return new PositionMemory(this,info);
	}

}
