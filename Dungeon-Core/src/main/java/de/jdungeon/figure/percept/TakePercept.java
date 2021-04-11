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

public class TakePercept extends OpticalPercept {
	
	private final Item it;
	private final Figure f;
	
	public TakePercept(Figure f, Item it, int round) {
		super(f.getRoomNumber(), round);
		this.f = f;
		this.it = it;
	}
	
	public FigureInfo getFigure() {
		return FigureInfo.makeFigureInfo(f,viewer.getRoomVisibility());
	}
	
	
	
	public ItemInfo getItem() {
		return ItemInfo.makeItemInfo(it,this.viewer.getRoomVisibility());
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		List<FigureInfo> l = new LinkedList<>();
		l.add(getFigure());
		return l;
	}
	
}
