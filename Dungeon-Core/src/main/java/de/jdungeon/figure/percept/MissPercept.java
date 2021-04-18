/*
 * Created on 17.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class MissPercept extends OpticalPercept {

    private final Figure attacker;
    private final Figure victim;

    public MissPercept(Figure a, Figure b, int round) {
        super(b.getRoomNumber(), round);
        attacker = a;
        victim = b;
    }

    public FigureInfo getAttacker(FigureInfo viewer) {
        return FigureInfo.makeFigureInfo(attacker, viewer.getVisMap());
    }

    public FigureInfo getVictim(FigureInfo viewer) {
        return FigureInfo.makeFigureInfo(victim, viewer.getVisMap());
    }

    @Override
    public List<FigureInfo> getInvolvedFigures(FigureInfo viewer) {
        List<FigureInfo> l = new LinkedList<>();
        l.add(getAttacker(viewer));
        l.add(getVictim(viewer));
        return l;
    }


}
