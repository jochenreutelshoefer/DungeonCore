/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.percept;

import de.jdungeon.item.Item;
import de.jdungeon.item.ItemInfo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.item.interfaces.ItemOwner;

public class ItemDroppedPercept extends SimpleActorPercept {

    private final List<Item> items;

    public ItemDroppedPercept(List<Item> items, Figure f, int round) {
        super(f, f.getRoomNumber(), round);
        this.items = items;
    }

    public List<ItemInfo> getItems(FigureInfo viewer) {
        List<ItemInfo> l = new LinkedList<>();
        for (Iterator iter = items.iterator(); iter.hasNext(); ) {
            Item element = (Item) iter.next();
            ItemInfo info = ItemInfo.makeItemInfo(element, viewer.getVisMap());
            l.add(info);
        }
        return l;
    }


}
