/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.item.interfaces.Usable;

public class UsePercept extends SimpleActorPercept {
	

	public UsePercept(Figure actor, Usable o, int round) {
		super(actor, actor.getRoomNumber(), round);
	}
	

}
