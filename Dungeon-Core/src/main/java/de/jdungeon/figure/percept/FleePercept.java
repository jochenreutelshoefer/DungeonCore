/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class FleePercept extends SimpleActorPercept {

    private final Position from;
    private final int dir;
    private final boolean success;

    public FleePercept(Figure f, Position from, int dir, boolean suc, int round) {
        super(f, f.getRoomNumber(), round);
        this.from = from;
        this.dir = dir;
        this.success = suc;
    }

    public RoomInfo getRoom(FigureInfo viewer) {
        return RoomInfo.makeRoomInfo(from.getRoom(), viewer.getVisMap());
    }

    public boolean isSuccess() {
        return success;
    }

    public int getDir() {
        return dir;
    }


}
