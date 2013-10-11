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

public class ItemDroppedPercept extends OpticalPercept {
	
	private List items;
	private Figure f;
	
	public ItemDroppedPercept(List items, Figure f) {
		this.items = items;
		this.f = f;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	public List getItems() {
		List l = new LinkedList();
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			ItemInfo info = ItemInfo.makeItemInfo(element,this.viewer.getRoomVisibility());
			l.add(info);
		}
		return l;
	}
	
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility()));
		return l;
	}
	

}
