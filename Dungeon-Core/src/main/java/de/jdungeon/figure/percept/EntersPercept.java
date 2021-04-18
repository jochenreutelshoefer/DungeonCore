/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class EntersPercept extends SimpleActorPercept {

    private final Room from;

    public RoomInfo getFrom(FigureInfo viewer) {
        return new RoomInfo(from, viewer.getVisMap());
    }

    public RoomInfo getTo(FigureInfo viewer) {
        return new RoomInfo(from, viewer.getVisMap());
    }


    public EntersPercept(Figure f, Room r1, int round) {
        super(f, r1.getNumber(), round);
        from = r1;
    }


}
