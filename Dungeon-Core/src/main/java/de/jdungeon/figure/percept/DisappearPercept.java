package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class DisappearPercept extends SimpleActorPercept {

    private final Room from;

    public DisappearPercept(Figure f, Room from, int round) {
        super(f, from.getNumber(), round);
        this.from = from;
    }

    public RoomInfo getRoom(FigureInfo viewer) {
        return RoomInfo.makeRoomInfo(from, viewer.getVisMap());
    }


}
