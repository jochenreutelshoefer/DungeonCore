/*
 * Created on 09.01.2006
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

public class DiePercept extends OpticalPercept {

    private final Figure f;
    private final Room from;
    private int damage = -1;

    public DiePercept(Figure f, Room from, int round) {
        super(f.getRoomNumber(), round);
        this.f = f;
        this.from = from;
    }

    public DiePercept(Figure f, Room from, int damage, int round) {
        super(f.getRoomNumber(), round);
        this.f = f;
        this.from = from;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public FigureInfo getFigure(FigureInfo viewer) {
        return FigureInfo.makeFigureInfo(f, viewer.getVisMap());
    }

    public RoomInfo getRoom(FigureInfo viewer) {
        return RoomInfo.makeRoomInfo(from, viewer.getVisMap());
    }

    @Override
    public List<FigureInfo> getInvolvedFigures(FigureInfo viewer) {
        List<FigureInfo> l = new LinkedList<>();
        l.add(getFigure(viewer));
        return l;
    }

}
