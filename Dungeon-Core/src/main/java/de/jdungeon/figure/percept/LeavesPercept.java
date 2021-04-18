package de.jdungeon.figure.percept;

import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.02.20.
 */
public class LeavesPercept extends SimpleActorPercept {

    private final Room from;

    private final RouteInstruction.Direction direction;

    public RoomInfo getFrom(FigureInfo viewer) {
        return new RoomInfo(from, viewer.getVisMap());
    }


    public RoomInfo getTo(FigureInfo viewer) {
        return new RoomInfo(from.getNeighbourRoom(direction), viewer.getVisMap());
    }


    public LeavesPercept(Figure f, Room r1, RouteInstruction.Direction direction, int round) {
        super(f, r1.getNumber(), round);
        from = r1;
        this.direction = direction;
    }


    public RouteInstruction.Direction getDirection() {
        return direction;
    }

}
