/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class ScoutPercept extends SimpleActorPercept {

    private final Room from;
    private final int dir;

    public ScoutPercept(Figure f, Room from, int dir, int round) {
        super(f, from.getNumber(), round);
        this.from = from;
        this.dir = dir;
    }

    public RoomInfo getRoom(FigureInfo viewer) {
        return RoomInfo.makeRoomInfo(from, viewer.getVisMap());
    }

    public int getDir() {
        return dir;
    }


}
