package de.jdungeon.figure.percept;

import de.jdungeon.figure.Figure;

public class WaitPercept extends SimpleActorPercept {

    public WaitPercept(Figure f, int round) {
        super(f, f.getRoomNumber(), round);
    }

}
