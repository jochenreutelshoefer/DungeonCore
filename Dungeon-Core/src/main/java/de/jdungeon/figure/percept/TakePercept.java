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
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;

public class TakePercept extends SimpleActorPercept {

    private final Item it;

    public TakePercept(Figure f, Item it, int round) {
        super(f, f.getRoomNumber(), round);
        this.it = it;
    }

    public ItemInfo getItem(FigureInfo viewer) {
        return ItemInfo.makeItemInfo(it, viewer.getVisMap());
    }

}
