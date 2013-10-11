/*
 * Created on 09.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigureInfo;
import item.Item;
import item.ItemInfo;

public class TakePercept extends OpticalPercept {
	
	private Item it;
	private Figure f;
	
	public TakePercept(Figure f, Item it) {
		this.f = f;
		this.it = it;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	
	
	public ItemInfo getItem() {
		return ItemInfo.makeItemInfo(it,this.viewer.getRoomVisibility());
	}
	
	public List getInvolvedFigures() {
		List l = new LinkedList();
		l.add(getFigure());
		
		return l;
	}
	
}
