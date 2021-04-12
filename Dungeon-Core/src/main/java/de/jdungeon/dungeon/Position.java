/*
 * Created on 01.12.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.dungeon;

import java.util.Collection;
import java.util.Collections;

import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.memory.PositionMemory;
import de.jdungeon.log.Log;

public class Position extends DungeonWorldObject implements RoomEntity {

    private final Room room;

    private Figure figure;

    private final int index;

    private Position next;

    private Position previous;

    public Position(Room r, int index) {
        this.room = r;
        this.index = index;
    }

    @Override
    public Collection<Position> getInteractionPositions() {
        return Collections.singletonList(this);
    }

    public enum Pos {
        NW(0), // north west
        N(1),  // north
        NE(2), // north east
        E(3), // east
        SE(4), // south east
        S(5), // south
        SW(6), // south west
        W(7); // west

        Pos(int value) {
            this.value = value;
        }

        private final int value;

        public int getValue() {
            return value;
        }

        public static Pos fromValue(int value) {
            for (Pos pos : Pos.values()) {
                if (pos.getValue() == value) {
                    return pos;
                }
            }
            return null;
        }
    }

    /**
     * @return Returns the f.
     */
    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure info) {
        if (info != null) {
            checkFigureAlreadyOnAPosition(info);
        }
        figure = info;
    }

    private void checkFigureAlreadyOnAPosition(Figure info) {
        final Position[] positions = room.getPositions();
        for (Position position : positions) {
            if (info.equals(position.getFigure())) {
                Log.severe("Figure is already in Room!!");
            }
        }
    }

    /**
     * @return Returns the index.
     */
    public int getIndex() {
        return index;
    }

    public void figureEntersHere(Figure fig) {

        // consistency check only
        checkFigureAlreadyOnAPosition(fig);

        // if this is free, then we enter here
        if (figure == null) {
            figure = fig;
            fig.setPos(this);

        } else {
            // otherwise, we look for a nearby free position
            if (Math.random() < 0.5) {
                previous.figureEntersHere(fig);
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
            {0, DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID,
                    DIST_NEAR},
            {DIST_NEAR, 0, DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR,
                    DIST_MID},
            {DIST_MID, DIST_NEAR, 0, DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR,
                    DIST_FAR},
            {DIST_FAR, DIST_MID, DIST_NEAR, 0, DIST_NEAR, DIST_MID, DIST_FAR,
                    DIST_FAR},
            {DIST_FAR, DIST_FAR, DIST_MID, DIST_NEAR, 0, DIST_NEAR, DIST_MID,
                    DIST_FAR},
            {DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID, DIST_NEAR, 0, DIST_NEAR,
                    DIST_MID},
            {DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID, DIST_NEAR, 0,
                    DIST_NEAR},
            {DIST_NEAR, DIST_MID, DIST_FAR, DIST_FAR, DIST_FAR, DIST_MID,
                    DIST_NEAR, 0}};

    public int getDistanceTo(Position p) {
        if (p.getRoom() != room) {
            return -1;
        }
        return getDistance(p.index);
    }

    public int getDistance(int otherIndex) {
        return distMatrix[index][otherIndex];
    }

    public static int getFreePositionNear(Room room, int positionIndex) {
        if (room.getPositions()[positionIndex].getFigure() == null) {
            return positionIndex;
        }
        int counter = 0;
        while (counter < 5) {
            int nextIndexRight = Position.getNextIndex(positionIndex + counter, true);
            if (room.getPositions()[nextIndexRight].getFigure() == null) {
                return nextIndexRight;
            }
            int nextIndexLeft = Position.getNextIndex(positionIndex - counter, false);

            if (room.getPositions()[nextIndexLeft].getFigure() == null) {
                return nextIndexLeft;
            }
            counter++;

        }
        return -1;
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

    public RouteInstruction.Direction getPossibleFleeDirection() {
        if (index == 1) {
            return RouteInstruction.Direction.North;
        }
        if (index == 3) {
            return RouteInstruction.Direction.East;
        }
        if (index == 5) {
            return RouteInstruction.Direction.South;
        }
        if (index == 7) {
            return RouteInstruction.Direction.West;
        }
        return null;

    }

    private static int[][] dirMatrix = {
            {0, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST, Dir.SOUTH, Dir.SOUTH,
                    Dir.SOUTH},
            {Dir.WEST, 0, Dir.EAST, Dir.EAST, Dir.SOUTH, Dir.SOUTH, Dir.SOUTH,
                    Dir.WEST},
            {Dir.WEST, Dir.WEST, 0, Dir.SOUTH, Dir.SOUTH, Dir.SOUTH, Dir.WEST,
                    Dir.WEST},
            {Dir.WEST, Dir.WEST, Dir.NORTH, 0, Dir.SOUTH, Dir.WEST, Dir.WEST,
                    Dir.WEST},
            {Dir.WEST, Dir.NORTH, Dir.NORTH, Dir.NORTH, 0, Dir.WEST, Dir.WEST,
                    Dir.WEST},
            {Dir.NORTH, Dir.NORTH, Dir.NORTH, Dir.EAST, Dir.EAST, 0, Dir.WEST,
                    Dir.WEST},
            {Dir.NORTH, Dir.NORTH, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST, 0,
                    Dir.NORTH},
            {Dir.NORTH, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST, Dir.EAST,
                    Dir.SOUTH, 0}};

    public static int getDirFromTo(int from, int to) {
        return dirMatrix[from][to];
    }

    public void figureLeaves() {
        if (figure != null) {
            DungeonVisibilityMap roomVisibility = figure.getRoomVisibility();
            if (roomVisibility != null) {
                roomVisibility.removeScoutedVisibility(this);
            }
        }
        figure = null;
    }

    public String toString() {
        String figureString = "null";
        if (figure != null) {
            figureString = figure.getName();
        }
        return "PositionInRoom: index:  " + index + " de.jdungeon.figure: " + figureString;
    }

    /**
     * @return Returns the previous.
     */
    public Position getPrevious() {
        return previous;
    }

    /**
     * @return Returns the next.
     */
    public Position getNext() {
        return next;
    }

    public void setPrevious(Position l) {
        previous = l;
    }

    public void setNext(Position n) {
        next = n;
    }

    public InfoEntity makeInfoObject(DungeonVisibilityMap map) {
        // TODO Auto-generated method stub
        return null;
    }

    public JDPoint getRoomNumber() {
        return room.getRoomNumber();
    }

    public PositionMemory getMemoryObject(FigureInfo info) {

        return new PositionMemory(this, info);
    }

}
