package de.jdungeon.figure.percept;

import de.jdungeon.figure.Figure;

public class BreakSpellPercept extends SimpleActorPercept {

    public BreakSpellPercept(Figure f, int round) {
        super(f, f.getRoomNumber(), round);

    }

}
