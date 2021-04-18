/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;

public class ShieldBlockPercept extends SimpleActorPercept {


    public ShieldBlockPercept(Figure f, int round) {
        super(f, f.getRoomNumber(), round);
    }


}
