/*
 * Created on 16.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import item.Item;
import item.ItemInfo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;
import item.interfaces.ItemOwner;

public class ItemDroppedPercept extends OpticalPercept {
	
	private final List<Item> items;
	private final ItemOwner f;
	
	public ItemDroppedPercept(List<Item> items, ItemOwner f) {
		super(f.getLocation());
		this.items = items;
		this.f = f;
	}
	
	public FigureInfo getFigure() {
		if(f instanceof Figure) {
			return FigureInfo.makeFigureInfo((Figure)f,viewer.getRoomVisibility());
		}
		return null;
	}
	
	public List<ItemInfo> getItems() {
		List<ItemInfo> l = new LinkedList<>();
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			ItemInfo info = ItemInfo.makeItemInfo(element,this.viewer.getRoomVisibility());
			l.add(info);
		}
		return l;
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		if(f instanceof Figure) {
			l.add(FigureInfo.makeFigureInfo((Figure)f, viewer.getRoomVisibility()));
		}
		return l;

	}
	

}
