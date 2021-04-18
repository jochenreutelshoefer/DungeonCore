package de.jdungeon.figure.percept;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

import java.util.LinkedList;
import java.util.List;

public class SimpleActorPercept extends OpticalPercept {

    private Figure actor;

    public SimpleActorPercept(Figure actor, JDPoint room, int round) {
        super(room, round);
        if(actor == null) {
            throw new IllegalArgumentException("Figure may not be null");
        }
        this.actor = actor;
    }

    public FigureInfo getFigure(FigureInfo viewer) {
        return FigureInfo.makeFigureInfo(actor, viewer.getVisMap());
    }


    @Override
    public List<FigureInfo> getInvolvedFigures(FigureInfo viewer) {
        List<FigureInfo> l = new LinkedList<>();
        l.add(getFigure(viewer));
        return l;
    }

}
